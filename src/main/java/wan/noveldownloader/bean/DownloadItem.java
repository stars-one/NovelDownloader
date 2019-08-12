package wan.noveldownloader.bean;

/**
 * 数据库存放的实体类
 * @author StarsOne
 * @date Create in  2019/8/11 0011 15:47
 * @description
 */
public class DownloadItem {
    private String filePath,imgPath;
    private int id;
    public DownloadItem() {
    }



    public DownloadItem(String filePath, String imgPath) {
        this.filePath = filePath;
        this.imgPath = imgPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
