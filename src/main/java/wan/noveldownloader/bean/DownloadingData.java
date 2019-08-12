package wan.noveldownloader.bean;

/**
 * @author StarsOne
 * @date Create in  2019/8/12 0012 18:30
 * @description
 */
public class DownloadingData {
    private String url,downloadPath;

    public DownloadingData() {
    }

    public DownloadingData(String url, String downloadPath) {
        this.url = url;
        this.downloadPath = downloadPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }
}
