package wan.noveldownloader.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import wan.noveldownloader.bean.DownloadingItem;
import wan.noveldownloader.utils.DownloadTool;
import wan.noveldownloader.utils.MyUtils;

/**
 * @author StarsOne
 * @date Create in  2019/7/17 0017 12:24
 * @description
 */
public class ItemController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView iconImg;
    @FXML
    private ImageView imgPause;
    @FXML
    private Label name;
    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnPause;

    @FXML
    private JFXProgressBar progressbar;

    @FXML
    private Text flagText;

    @FXML
    private Text tvHasDownload;

    @FXML
    private Text tvPercentProcess;

    private DownloadingItem item;

    public ItemController() {
        FXMLLoader item = new FXMLLoader(MyUtils.getFxmlPath("item"));
        item.setController(this);
        try {
            item.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始下载每一章
     */
    public void startDownloadChacter() {
        new Thread(item.getTask()).start();
    }

    /**
     * 开启进程，获取小说封面图片，小说名字和目录信息
     * @param url
     * @param downloadPath
     */
    public void startTask(String url, String downloadPath) {

        new Thread(new Task<Void>() {
            @Override
            protected void succeeded() {
                super.succeeded();
                item.setFlagTextProperty("正在下载");
                fillData(item); //显示小说封面图片和小说名字
                bindControl();//设置相关的数据绑定
                startDownloadChacter();//开始任务
            }
            @Override
            protected Void call() {
                item = DownloadTool.getTaskMessage(url, downloadPath);
                return null;
            }
        }).start();
    }

    private void bindControl() {
        //绑定进度条,显示文字
        flagText.textProperty().bind(item.getFlagTextProperty());
        progressbar.progressProperty().bind(item.getTask().progressProperty());
        tvHasDownload.textProperty().bind(item.getTask().messageProperty());
        tvPercentProcess.textProperty().bind(item.getPercentProgressTextProperty());
    }

    private void fillData(DownloadingItem downloadingItem) {
        setName(downloadingItem.getName());
        setIconImg(downloadingItem.getImgPath());
    }

    public void setOnclick(btnOnclickListener listener) {
        btnCancel.setOnAction(event -> {
            listener.onclick(rootPane);
        });

    }

    private void setIconImg(String imgurl) {
        try {
            iconImg.setImage(new Image(new FileInputStream(new File(imgurl))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setName(String text) {
        name.setText(text);
    }


    public Pane getPane() {
        return rootPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnPause.setOnAction(event -> pauseOrHold());

    }

    public void pause() {
        imgPause.setImage(MyUtils.getImg("start.png"));
        item.setFlagTextProperty("已暂停");
        btnPause.setText("1");
        item.setFlag(true);
    }

    public void hold() {
        imgPause.setImage(MyUtils.getImg("pause.png"));
        item.setFlagTextProperty("正在下载");
        btnPause.setText("0");
        item.setFlag(false);
    }

    private void pauseOrHold() {
        if (btnPause.getText().equals("0")) {
            pause();
        } else {
            hold();
        }

    }

    public interface btnOnclickListener {
        void onclick(Pane pane);
    }
}
