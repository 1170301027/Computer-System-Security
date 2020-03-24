package conn;

import user.Client;
import user.Administrator;

import javax.management.Query;
import java.nio.channels.NonWritableChannelException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectToSql {
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/comlab?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT&allowPublicKeyRetrieval=true&useSSL=false";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "123456";

    //相关Sql
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private String sql = null;

    private void connectSql() throws ClassNotFoundException, SQLException {
        // 注册 JDBC 驱动
        Class.forName(JDBC_DRIVER);
        // 打开链接
        System.out.println("连接数据库...");
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        // 执行查询
        stmt = conn.createStatement();
    }
    private void recovery(){
        conn = null;
        stmt = null;
        rs = null;
        sql = null;
    }

    public boolean insertClient(Client client){
        try {
            connectSql();
            String insert = "('"+client.getUserName()+"','"+client.getPassword()+"','"+client.getBalance()+"')";
            sql = "insert into client (userName, password, balance) values "+insert;
            int result = stmt.executeUpdate(sql);
            if (result != 0){
                System.out.println("insert client successfully");
                //调用的地方再写入log
                //Log.insert("insert client: "+client.getUserName());
            } else {
                return false;
            }
            conn.close();
            stmt.close();
            recovery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean insertManager(Administrator administrator){
        try {
            connectSql();
            String insert = "('"+ administrator.getUserName()+"','"+ administrator.getPassword()+"')";
            sql = "insert into administrator (userName, password) values "+insert;
            int result = stmt.executeUpdate(sql);
            if (result != 0){
                System.out.println("insert admin successfully");
                //调用的地方再写入log
                //Log.insert("insert client: "+client.getUserName());
            } else {
                return false;
            }
            conn.close();
            stmt.close();
            recovery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean delete(String userName){
        try {
            connectSql();
            sql = "delete from client where userName='"+userName+"'";
            int result = stmt.executeUpdate(sql);
            if (result != 0){
                System.out.println("delete successfully");
                //调用的地方再写入log
                //Log.insert("insert client: "+client.getUserName());
            } else {
                return false;
            }
            conn.close();
            stmt.close();
            recovery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     *
     * @param administrator - 修改后的管理员信息
     * @return
     */
    public boolean changeAdiministratorPassword(Administrator administrator){
        try {
            connectSql();
            sql = "delete from administrator where userName='"+administrator.getUserName()+"'";
            int result = stmt.executeUpdate(sql);
            if (result != 0){
                System.out.println("delete successfully");
                //调用的地方再写入log
                //Log.insert("insert client: "+client.getUserName());
            } else {
                return false;
            }
            conn.close();
            stmt.close();
            recovery();
            insertManager(administrator);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 在指定数据库查询用户是否存在
     * @param userName
     * @param identity
     * @return
     */
    public boolean query(String userName, String identity){
        try {
            connectSql();
            sql = "select * from "+identity+" where userName='"+userName+"'";
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                return true;
            }
            rs.close();
            conn.close();
            stmt.close();
            recovery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Client constructClient(String userName){
        Client client = null;
        try {
            connectSql();
            sql = "select * from client where userName='"+userName+"'";
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                String password = rs.getString("password");
                String balance = rs.getString("balance");
                client = new Client(userName,password,balance);
            }
            rs.close();
            conn.close();
            stmt.close();
            recovery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return client;
    }

    public Administrator consructAdministrator(String userName){
        Administrator administrator = null;
        try {
            connectSql();
            sql = "select * from administrator where userName='"+userName+"'";
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                String password = rs.getString("password");
                administrator = new Administrator(userName,password);
            }
            rs.close();
            conn.close();
            stmt.close();
            recovery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return administrator;
    }

    public List<String> getAllClients() {
        List<String> list = new ArrayList<>();
        try {
            connectSql();
            sql = "select * from client";
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                String userName = rs.getString("userName");
                list.add(userName);
            }
            rs.close();
            conn.close();
            stmt.close();
            recovery();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
