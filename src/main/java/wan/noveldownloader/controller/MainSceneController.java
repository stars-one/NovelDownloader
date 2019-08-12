package wan.noveldownloader.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTabPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import wan.noveldownloader.adapter.DownloadingAdapter;
import wan.noveldownloader.bean.DownloadItem;
import wan.noveldownloader.bean.DownloadingData;
import wan.noveldownloader.bean.HasDownloadItem;
import wan.noveldownloader.utils.BaseController;
import wan.noveldownloader.utils.DialogBuilder;
import wan.noveldownloader.utils.JdbcUtil;
import wan.noveldownloader.utils.MyUtils;

public class MainSceneController extends BaseController {


    @FXML
    private TextField tvDownload;

    @FXML
    private JFXButton btnAddTask;

    @FXML
    private JFXButton btnPauseAll;

    @FXML
    private JFXButton btnCancelAll;

    @FXML
    private VBox downloadingContainer;
    private DownloadingAdapter downloadingAdapter;

    @FXML
    private VBox hasDownloadPane;
    private ObservableList<Node> children;

    private String downloadPath = "";
    @FXML
    private ImageView downloadPathImg;

    @FXML
    private Hyperlink projectlink;

    @FXML
    private Hyperlink bloglink;

    @FXML
    private Hyperlink qqlink;

    @FXML
    private Hyperlink qunLink;
    @FXML
    private Hyperlink qianbiLink;
    @FXML
    private JFXCheckBox checkboxClip;
    @FXML
    private JFXButton btnDeleteDownload;
    private JdbcUtil jdbcUtil = new JdbcUtil();

    @FXML
    private JFXTabPane rootPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //设置关于界面的点击链接
        MyUtils.setLinkAction(projectlink,()->MyUtils.setLinkAutoAction(projectlink));
        MyUtils.setLinkAction(bloglink,()->MyUtils.setLinkAutoAction(bloglink));
        MyUtils.setLinkAction(qqlink,()->MyUtils.setLinkAutoAction(qqlink));
        //TODO 新添加的书源在这添加hyperlink的跳转
        MyUtils.setWebLinkAutoAction(qianbiLink);

        MyUtils.setLinkAction(qunLink, () -> {
            try {
                Desktop.getDesktop().browse(new URI("https://shang.qq.com/wpa/qunwpa?idkey=28394194e1854ebcf977e8121dfe9acd29ca395833ab2dfa27934c1bca705235"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        //从数据库中读取下载目录
        if (jdbcUtil.getDownloadPath().equals("")) {
            //设置为默认下载目录
            downloadPath = MyUtils.getCurrentPath() + File.separator + "下载";
            jdbcUtil.savePath(downloadPath);
        } else {
            downloadPath = jdbcUtil.getDownloadPath();
        }

        tvDownload.setText(downloadPath);

        //剪贴板开关的选中设置
        checkboxClip.setSelected(jdbcUtil.getOpenClipBoard());
        checkboxClip.setOnAction(event -> {
            jdbcUtil.setOpenClipBoard(checkboxClip.isSelected());
            openClipBoard();//开启
        });

        //选择下载文件夹功能
        downloadPathImg.setOnMouseClicked(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("选择下载文件夹");
            File file = directoryChooser.showDialog(downloadPathImg.getScene().getWindow());
            downloadPath = file.getPath();
            tvDownload.setText(downloadPath);
            jdbcUtil.savePath(downloadPath);
        });
        children = hasDownloadPane.getChildren();

        downloadingAdapter = new DownloadingAdapter(downloadingContainer, this);
        btnPauseAll.setOnAction(event -> {
            if (btnPauseAll.getText().equals("全部暂停")) {
                btnPauseAll.setText("全部开始");
                pauseAllTask();
            } else {
                btnPauseAll.setText("全部暂停");
                startAllTask();
            }
        });

        //全部删除
        btnCancelAll.setOnAction(event -> {
            List<ItemController> itemControllers = downloadingAdapter.getItemControllers();
            for (ItemController itemController : itemControllers) {
                itemController.closeThis();
            }
      /*      //先停止（全部暂停）
            pauseAllTask();
            //TODO 删除硬盘中的文件
            //删除列表
            downloadingAdapter.deleteAllNode();*/
        });

        btnAddTask.setOnAction(event -> new DialogBuilder(btnAddTask).setTitle("输入网址")
                .setMessage("输入小说网址（笔趣阁，铅笔小说网")
                .setNegativeBtn("取消").setPositiveBtn("确定").setTextFieldText(result -> {
                    //TODO 多余的网站建立不同的缓存目录
                    if (result.contains("www.x23qb.com")) {
                        File file = new File(downloadPath + File.separator + "temp" + File.separator + "qianbi" + File.separator);
                        File file1 = new File(file,"img");//图片文件夹
                        //创建文件夹
                        if (!file.exists() && !file1.exists()) {
                            //成功创建文件夹之后，下载操作
                            if (file.mkdirs() && file1.mkdirs()) {
                                jdbcUtil.saveDownloadingData(new DownloadingData(result,file.getPath()));//存放数据库
                                downloadingAdapter.addNode(result, file.getPath());
                            }
                        } else {
                            //文件夹以及存在，下载操作
                            jdbcUtil.saveDownloadingData(new DownloadingData(result,file.getPath()));//存放数据库
                            downloadingAdapter.addNode(result, file.getPath());
                        }
                    } else {
                        new DialogBuilder(btnAddTask).setMessage("输入小说地址有误").setTitle("错误").setNegativeBtn("确定").create();
                    }
                }).create());

        readDownloadList();
        openClipBoard();
        btnDeleteDownload.setOnAction(event -> deleteAllHasDownloadItem());
        readDownloadingList();
    }

    private void readDownloadingList() {
        List<DownloadingData> downloadingDataList = jdbcUtil.getDownloadingDataList();
        for (DownloadingData downloadingData : downloadingDataList) {
            downloadingAdapter.addNode(downloadingData.getUrl(),downloadingData.getDownloadPath());
        }
    }


    /**
     * 开启剪贴板监控
     */
    private void openClipBoard() {
        if (checkboxClip.isSelected()) {
            //开启监控键盘
            System.out.println("功能未实现...");
        }
    }

    /**
     * 读取数据库中的下载列表
     */
    private void readDownloadList() {
        List<DownloadItem> downloadList = jdbcUtil.getDownloadList();
        if (downloadList != null) {
            for (DownloadItem downloadItem : downloadList) {
                String filePath = downloadItem.getFilePath();
                String imgPath = downloadItem.getImgPath();
                HasDownloadItem hasDownloadItem = new HasDownloadItem(filePath, imgPath);
                HasDownloadItemController hasDownloadItemController = new HasDownloadItemController(hasDownloadItem);
                hasDownloadItemController.setBtnDeleteAction(()->deleteHasDownloadItem(hasDownloadItemController));
                addHasDownloadItem(hasDownloadItemController);
            }
        }

    }


    public void addHasDownloadItem(HasDownloadItemController itemController) {
        Pane pane = itemController.getPane();
        itemController.setId(children.size()+1);
        children.add(pane);
    }

    /**
     * 删除所有已完成下载记录
     */
    public void deleteAllHasDownloadItem() {
        List<HasDownloadItemController> hasDownloadItemControllers = downloadingAdapter.getHasDownloadItemControllers();
        for (HasDownloadItemController hasDownloadItemController : hasDownloadItemControllers) {
            deleteHasDownloadItem(hasDownloadItemController);
        }
    }
    /**
     * 删除下载
     * @param itemController
     */
    public void deleteHasDownloadItem(HasDownloadItemController itemController) {
        Pane pane = itemController.getPane();
        jdbcUtil.deleteDownloadItem(itemController.getId());
        children.remove(pane);
    }

    private void startAllTask() {
        //全部开始功能实现
        List<ItemController> itemControllers = downloadingAdapter.getItemControllers();
        for (ItemController itemController : itemControllers) {
            itemController.hold();
        }
    }

    private void pauseAllTask() {
        //全部暂停功能实现
        List<ItemController> itemControllers = downloadingAdapter.getItemControllers();
        for (ItemController itemController : itemControllers) {
            itemController.pause();
        }
    }
}
