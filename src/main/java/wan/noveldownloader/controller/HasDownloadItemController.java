package wan.noveldownloader.controller;

import com.jfoenix.controls.JFXButton;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import wan.noveldownloader.bean.HasDownloadItem;
import wan.noveldownloader.utils.MyUtils;

/**
 * @author StarsOne
 * @date Create in  2019/8/2 0002 13:17
 * @description
 */
public class HasDownloadItemController implements Initializable {

    private int id;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView iconImg;

    @FXML
    private Label name;

    @FXML
    private Label time;

    @FXML
    private Label size;

    @FXML
    private JFXButton btnFile;

    @FXML
    private JFXButton btnDelete;
    private HasDownloadItem hasDownloadItem;
    public OnDeleteListener onDeleteListener;

    public interface OnDeleteListener {
        void onDelete();
    }

    public HasDownloadItemController(HasDownloadItem hasDownloadItem) {
        FXMLLoader item = new FXMLLoader(MyUtils.getFxmlPath("hasDownItem"));
        item.setController(this);
        try {
            item.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.hasDownloadItem = hasDownloadItem;
        setData();
    }

    private void setData() {
        if (hasDownloadItem != null) {
            btnFile.setOnAction(event -> {
                try {
                    Desktop.getDesktop().browse(new File(hasDownloadItem.getPath()).getParentFile().toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            name.setText(hasDownloadItem.getBookName());
            size.setText(hasDownloadItem.getSize());
            time.setText(hasDownloadItem.getDate());
            try {
                iconImg.setImage(new Image(new FileInputStream(new File(hasDownloadItem.getIconPath()))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 设置删除
     *
     * @param onDeleteListener
     */
    public void setBtnDeleteAction(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
        btnDelete.setOnAction(event -> onDeleteListener.onDelete());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pane getPane() {
        return rootPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
