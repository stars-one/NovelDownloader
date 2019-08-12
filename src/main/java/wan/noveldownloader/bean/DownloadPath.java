package wan.noveldownloader.bean;

/**
 * @author StarsOne
 * @date Create in  2019/8/11 0011 11:59
 * @description
 */
public class DownloadPath {
    private Integer id;
    private String path;

    public DownloadPath() {
    }

    public DownloadPath(Integer id, String path) {
        this.id = id;
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
