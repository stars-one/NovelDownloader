package wan.noveldownloader.controller;

import com.jfoenix.controls.JFXProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

/**
 * @author StarsOne
 * @date Create in  2019/7/17 0017 13:00
 * @description
 */
public class Test implements Initializable {

    @FXML
    private ProgressBar bar;
    @FXML
    private JFXProgressBar progressbar;

    @FXML
    private Button btnStart;
    @FXML
    private Button btnPause;

    @FXML
    private Text text;
    @FXML
    private Text name;
    private boolean isPause = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //btnPause.setOnAction(event -> isPause = !isPause);
        //Task<Void> task = new Task<Void>() {
        //    @Override
        //    protected void succeeded() {
        //        super.succeeded();
        //        text.setText("下载成功！");
        //    }
        //
        //    protected void getData(DownloadingItem1 taskMessage) {
        //        name.setText(taskMessage.getName());
        //    }
        //
        //    @Override
        //    protected Void call() throws Exception {
        //
        //
        //};
        //
        //progressbar.progressProperty().bind(task.progressProperty());
        //
        //btnStart.setOnAction(event -> {
        //    new Thread(task).start();
        //});
    }
}
