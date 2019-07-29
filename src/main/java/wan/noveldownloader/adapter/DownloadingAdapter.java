package wan.noveldownloader.adapter;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import wan.noveldownloader.controller.ItemController;

/**
 * @author StarsOne
 * @date Create in  2019/7/18 0018 9:10
 * @description
 */
public class DownloadingAdapter {
    private List<ItemController> itemControllers;
    private ObservableList<Node> nodes;


    public DownloadingAdapter(Pane pane) {
        itemControllers = new ArrayList<>();
        nodes = pane.getChildren();
    }

    public DownloadingAdapter(List<ItemController> itemControllers, ObservableList<Node> nodes) {
        this.itemControllers = itemControllers;
        this.nodes = nodes;

        if (nodes.size() != itemControllers.size()) {

            for (ItemController itemController : itemControllers) {
                nodes.add(itemController.getPane());
            }
        }
    }

    public void addNode(String url, String downloadPath) {

        ItemController itemController = new ItemController();
        itemController.setOnclick(this::deleteNode);
        nodes.add(itemController.getPane());
        itemControllers.add(itemController);
        itemController.startTask(url,downloadPath);

        //novel.setItem(itemController);
        //new Thread(novel.getTask()).start();//开启下载进程
        //
        //itemController.refreshData();//开启进程，每隔1s更新一次UI
        /*ItemController itemController = new ItemController();
        nodes.add(itemController.getPane());
        itemControllers.add(itemController);*/


    }

    public void deleteNode(Pane pane) {
        int index = nodes.indexOf(pane);
        nodes.remove(index);
        itemControllers.remove(index);
    }

    public void deleteAllNode() {
        nodes.remove(0, nodes.size());
    }

    public List<ItemController> getItemControllers() {
        return itemControllers;
    }

    public void setItemControllers(List<ItemController> itemControllers) {
        this.itemControllers = itemControllers;
    }

    public ObservableList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ObservableList<Node> nodes) {
        this.nodes = nodes;
    }
}
