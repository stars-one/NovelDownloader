package wan.noveldownloader.utils;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import wan.noveldownloader.bean.DownloadingItem;

/**
 * @author StarsOne
 * @date Create in  2019-7-19 0019 09:02:27
 * @description
 */
public class DownloadTool {

    public interface OnCompleteListener{
        void onStart();
        void onComplete();
    }

    public static String downloadImage(String imgUrl, String path) {
        int index = imgUrl.lastIndexOf("/");
        String fileName = imgUrl.substring(index);
        try {
            URL url = new URL(imgUrl);
            URLConnection connection = url.openConnection();//打开链接
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            File file = new File(path, fileName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            int c;
            byte[] temp = new byte[1024 * 2];//提供个缓冲区
            while ((c = bufferedInputStream.read(temp)) != -1) {
                bufferedOutputStream.write(temp, 0, c);//读多少，写多少
            }
            bufferedOutputStream.close();
            inputStream.close();
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取小说的目录信息
     * @param url
     * @param downloadPath
     * @return
     */
    public static DownloadingItem getTaskMessage(String url, String downloadPath) {
        try {
            Document document = Jsoup.connect(url).get();
            Element div = document.getElementById("bookimg");
            Element img = div.selectFirst("img");
            String imgUrl = img.attr("src");
            String name = img.attr("alt");

            Map<Integer, String> maps = new HashMap<>();
            Elements elements = document.select("#chapterList li");
            for (int i = 0; i < elements.size(); i++) {
                String url1 = "https://www.x23qb.com" +elements.get(i).selectFirst("a").attr("href");
                maps.put(i, url1);
            }

            String imgPath = downloadImage(imgUrl, downloadPath);
            return new DownloadingItem(name, imgPath, downloadPath, maps);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载每章节
     * @param url
     * @param path
     * @param num
     * @return
     */
    public static String downloadChapter(String url, String path, int num) {

        try {
            int end = url.lastIndexOf("/");
            int start = url.indexOf("k") + 2;
            String fileName = url.substring(start, end) + "_" + num;
            File file = new File(path, fileName+".txt");

            Document document = Jsoup.connect(url).get();
            Element mainTextDiv = document.selectFirst("#mlfy_main_text");
            String title = mainTextDiv.selectFirst("h1").text();//章节标题

            Element element = document.selectFirst("#TextContent");
            String content = element.text().replaceAll(" ", "\n").replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("&nbsp;", "");
            content = content.replaceAll("本章未完，点击下一页继续阅读", "").replaceAll("＞＞", "");//章节内容

            //文件写入操作
            FileUtils.writeStringToFile(file,title+"\n"+content+"\n","GBK");
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
