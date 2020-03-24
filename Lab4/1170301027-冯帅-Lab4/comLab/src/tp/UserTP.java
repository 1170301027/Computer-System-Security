package tp;

import conn.ConnectToSql;
import log.Log;
import user.Administrator;
import user.Client;

public class UserTP {
    /**
     * 转账
     * @param sourceAccount
     * @param targetAccount
     * @return
     */
    public boolean transfer(Client sourceAccount, Client targetAccount,int money){
        //授权
        deposit(targetAccount,money);
        withdraw(sourceAccount,money);
        return true;
    }


    /**
     * 存款
     * @param sourceAccount
     * @param money
     * @return
     */
    public boolean deposit(Client sourceAccount ,int money){
        //授权
        ConnectToSql connectToSql = new ConnectToSql();
        int source = Integer.parseInt(sourceAccount.getBalance());
        int target = source + money;
        sourceAccount.setBalance(target+"");
        if(connectToSql.delete(sourceAccount.getUserName())) {
            if (!connectToSql.insertClient(sourceAccount))
                return false;
        } else
            return false;
        return true;
    }

    /**
     * 取款
     * @param sourceAccount
     * @param money
     * @return
     */
    public boolean withdraw(Client sourceAccount ,int money){
        //授权
        ConnectToSql connectToSql = new ConnectToSql();
        int source = Integer.parseInt(sourceAccount.getBalance());
        int target = source - money;
        sourceAccount.setBalance(target+"");
        if(connectToSql.delete(sourceAccount.getUserName())) {
            if (!connectToSql.insertClient(sourceAccount))
                return false;
        } else
            return false;
        return true;
    }

    /**
     * 查询余额
     * @param account
     * @return
     */
    public int queryBalance(Client account){
        ConnectToSql connectToSql = new ConnectToSql();
        account = connectToSql.constructClient(account.getUserName());
        return Integer.parseInt(account.getBalance());
    }

    /**
     * 改密
     * @param account
     * @return
     */
    public boolean changeClientPassword(Client account,String password){
        account.setPassword(password);
        ConnectToSql connectToSql = new ConnectToSql();
        if(connectToSql.delete(account.getUserName())) {
            if (!connectToSql.insertClient(account))
                return false;
        } else
            return false;
        return true;
    }
    /**
     * 改密
     * @param administrator
     * @return
     */
    public boolean changeAdminPassword(Administrator administrator,String password){
        administrator.setPassword(password);
        administrator.setPassword(password);
        ConnectToSql connectToSql = new ConnectToSql();
        if(connectToSql.delete(administrator.getUserName())) {
            if (!connectToSql.insertManager(administrator))
                return false;
        } else
            return false;
        return true;
    }

}
