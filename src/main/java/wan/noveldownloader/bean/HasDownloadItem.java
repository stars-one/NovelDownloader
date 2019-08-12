package wan.noveldownloader.bean;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author StarsOne
 * @date Create in  2019/8/2 0002 12:48
 * @description
 */
public class HasDownloadItem {
    private String bookName;
    private File file;
    private String size;//文件大小
    private String date;//修改时间
    private String path;//文件路径
    private String iconPath;

    public HasDownloadItem() {
    }

    public HasDownloadItem(String filepath,String iconPath) {
        this.file = new File(filepath);
        this.iconPath = iconPath;
        path = file.getPath();
        bookName = file.getName().substring(0,file.getName().indexOf("."));
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        size = decimalFormat.format(file.length()/(1024*1024*1.0))+"MB";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date = dateFormat.format(new Date(file.lastModified()));
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("HasDownloadItem{");
        sb.append("bookName='").append(bookName).append('\'');
        sb.append(", file=").append(file);
        sb.append(", size='").append(size).append('\'');
        sb.append(", date='").append(date).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append(", iconPath='").append(iconPath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
