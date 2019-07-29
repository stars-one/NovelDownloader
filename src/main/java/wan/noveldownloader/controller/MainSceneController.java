package wan.noveldownloader.controller;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import wan.noveldownloader.adapter.DownloadingAdapter;
import wan.noveldownloader.utils.BaseController;
import wan.noveldownloader.utils.DialogBuilder;

public class MainSceneController extends BaseController {


    @FXML
    private JFXButton btnAddTask;

    @FXML
    private JFXButton btnPauseAll;

    @FXML
    private JFXButton btnCancelAll;

    @FXML
    private VBox downloadingContainer;
    private DownloadingAdapter downloadingAdapter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        /*ObservableList<Node> children = downloadingContainer.getChildren();
        List<ItemController> lists = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ItemController itemController = new ItemController();
            itemController.setName("this is" + i);
            itemController.setOnclick(pane -> downloadingAdapter.deleteNode(pane));
            lists.add(itemController);
        }
        downloadingAdapter = new DownloadingAdapter(lists, children);*/
        //TODO 如果缓存目录有文件，则读取，继续下载

        downloadingAdapter = new DownloadingAdapter(downloadingContainer);
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
            ObservableList<Node> nodes = downloadingContainer.getChildren();
            //先停止（全部暂停）
            pauseAllTask();
            //TODO 删除硬盘中的文件

            //删除列表
            downloadingAdapter.deleteAllNode();
        });
        btnAddTask.setOnAction(event -> new DialogBuilder(btnAddTask).setTitle("输入网址")
                .setMessage("输入小说网址（笔趣阁，铅笔小说网")
                .setNegativeBtn("取消").setPositiveBtn("确定").setTextFieldText(result -> {
                    if (result.contains("www.x23qb.com")) {
                        downloadingAdapter.addNode(result, "Q:\\atest\\download\\temp\\");
                    } else {
                        new DialogBuilder(btnAddTask).setMessage("输入小说地址有误").setTitle("错误").setNegativeBtn("确定").create();
                    }
                }).create());

    }

    private void startAllTask() {
        //TODO 全部开始功能实现
        List<ItemController> itemControllers = downloadingAdapter.getItemControllers();
        for (ItemController itemController : itemControllers) {
            itemController.hold();
        }

    }

    private void pauseAllTask() {
        //TODO 全部暂停功能实现
        List<ItemController> itemControllers = downloadingAdapter.getItemControllers();
        for (ItemController itemController : itemControllers) {
            itemController.pause();
        }
    }
}
