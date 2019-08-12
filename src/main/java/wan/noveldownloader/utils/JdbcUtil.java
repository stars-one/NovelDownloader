package wan.noveldownloader.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import wan.noveldownloader.bean.DownloadItem;
import wan.noveldownloader.bean.DownloadPath;

/**
 * @author StarsOne
 * @date Create in  2019/8/7 0007 22:15
 * @description
 */
public class JdbcUtil {
    static {
        try {
            //注册oracle驱动程序
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("注册驱动程序失败");
            e.printStackTrace();
        }
    }

    private Connection connection;

    public JdbcUtil(String currentPath) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + currentPath + File.separator + "config.db");
            //在数据库中创建下载列表
            QueryRunner queryRunner = new QueryRunner(true);
            int count = queryRunner.query(connection, "select count(1) from sqlite_master where tbl_name = 'downloaditem' and type='table'", new ScalarHandler<>());
            //下载列表不存在则创表
            if (count <= 0) {
                queryRunner.update(connection, "create table downloaditem(id int,filepath varchar2(255),imgPath varchar2(255))");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DownloadItem> getDownloadList() {
        QueryRunner queryRunner = new QueryRunner(true);

        try {
            return queryRunner.query(connection, "select * from downloaditem", new BeanListHandler<>(DownloadItem.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据库之中是否有path
     *
     * @return
     */
    public boolean isPathTableExist() {
        QueryRunner queryRunner = new QueryRunner(true);

        try {
            int count = queryRunner.query(connection, "select count(1) from sqlite_master where tbl_name = 'path' and type='table'", new ScalarHandler<>());
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteDownloadItem(int id) {
        QueryRunner queryRunner = new QueryRunner(true);
        try {
            queryRunner.update(connection, "delete from downloaditem where id=?", id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePath(String downloadPath) {
        QueryRunner queryRunner = new QueryRunner(true);
        try {
            if (isPathTableExist()) {
                //存在，更新下载目录
                queryRunner.update(connection, "update path set path=? where id=1", downloadPath);
            } else {
                //不存在，创建并保存默认的下载目录
                queryRunner.update(connection, "create table path(id int primary key,path varchar2(255))");
                queryRunner.update(connection, "insert into path values(1,?)", downloadPath);
                queryRunner.update(connection, "insert into path values(2,'0')");//剪切板监控默认关
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得数据库中保存的下载目录
     *
     * @return
     */
    public String getDownloadPath() {
        QueryRunner queryRunner = new QueryRunner(true);
        try {
            DownloadPath query = queryRunner.query(connection, "select * from path where id=1", new BeanHandler<>(DownloadPath.class));
            return query.getPath();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *
     */
    public void setOpenClipBoard(boolean flag) {
        QueryRunner queryRunner = new QueryRunner(true);
        try {
            if (flag) {
                queryRunner.update(connection, "update path set path ='1' where id=2");
            } else {
                queryRunner.update(connection, "update path set path ='0' where id=2");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getOpenClipBoard() {
        QueryRunner queryRunner = new QueryRunner(true);
        try {
            DownloadPath re = queryRunner.query(connection, "select * from path where id=2", new BeanHandler<>(DownloadPath.class));
            return !re.getPath().equals("0");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addDownloadItem(DownloadItem downloadItem) {
        QueryRunner queryRunner = new QueryRunner(true);
        try {
            //添加新数据，id为最新的那条数据的id+1
            DownloadItem item = queryRunner.query(connection, "select * from downloaditem order by id desc limit 1", new BeanHandler<>(DownloadItem.class));
            int id =0;
            if (item != null) {
                id = item.getId();
            }
            queryRunner.update(connection, "insert into downloaditem values(?,?,?)", id + 1, downloadItem.getFilePath(), downloadItem.getImgPath());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
