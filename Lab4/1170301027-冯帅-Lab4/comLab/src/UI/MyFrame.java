package UI;

import conn.ConnectToSql;
import log.Log;
import tp.UserTP;
import user.Administrator;
import user.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.Target;
import java.util.*;

public class MyFrame {
    private JFrame frame = null;
    private String request = null;
    private int count = 0;
    private Set<Administrator> administratorSet = new HashSet<>();
    private Client activeClient = null;
    private List<String> requestParamList = new ArrayList<>();
    public MyFrame() {
        init();
    }

    private void init(){
        frame = new JFrame("Bank");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    /**
     * 用户登录
     * */
    public void login() {
        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        //创建 标签 & 输入框 & 按钮
        JLabel userLabel = new JLabel("username:");
        JLabel passwordLabel = new JLabel("password:");
        JLabel identityLabel = new JLabel("identity:");
        JTextField userNameText = new JTextField(20);
        JPasswordField userPasswordText = new JPasswordField(20);
        userPasswordText.setEchoChar('*');
        //JTextField identityText = new JTextField(20);
        JButton loginButton = new JButton("login");
        JButton registerButton = new JButton("register");

        // 设置标签的大小和位置
        int x,y,width,height;
        x = 150;
        y = 100;
        userLabel.setBounds(x, y+20, 80, 25);
        passwordLabel.setBounds(x, y+50, 80, 25);
        identityLabel.setBounds(x, y+80, 80, 25);

        userNameText.setBounds(x+40, y+20, 165, 25);
        userPasswordText.setBounds(x+40, y+50, 165, 25);
        //identityText.setBounds(x+40, y+80, 165, 25);

        loginButton.setBounds(220, y+110, 80, 25);
        registerButton.setBounds(220, y+140, 80, 25);
        String[] identity = {"client","administrator"};
        JComboBox comBox = new JComboBox();//下拉列表
        comBox.addItem(identity[0]);
        comBox.addItem(identity[1]);
        comBox.setBounds(x+40, y+80, 165, 25);
        panel.add(comBox);

        // 设置面板内容
        panel.add(userLabel);
        panel.add(userNameText);
        panel.add(passwordLabel);
        panel.add(userPasswordText);
        panel.add(identityLabel);
        //panel.add(identityText);
        panel.add(loginButton);
        panel.add(registerButton);

        // 将面板加入到窗口中
        frame.add(panel);

        // 按钮的监听事件
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 检测身份
                ConnectToSql connectToSql = new ConnectToSql();
                panel.setVisible(false);
                boolean flag = false;
                if (comBox.getSelectedItem().toString().equals(identity[0])) {
                    // 查找该用户
                    if (connectToSql.query(userNameText.getText(),identity[0])) {

                        Client client = connectToSql.constructClient(userNameText.getText());
                        String password = new String(userPasswordText.getPassword());
                        if (!password.equals(client.getPassword())){
                        } else {
                            Log.insert("Client "+client.getUserName()+" login successfully.");
                            flag = true;
                            loginClient(client);
                        }
                    }
                } else if (comBox.getSelectedItem().toString().equals(identity[1])) {
                    // 查找该用户
                    if (connectToSql.query(userNameText.getText(),identity[1])) {
                        Administrator administrator = connectToSql.consructAdministrator(userNameText.getText());
                        String password = new String(userPasswordText.getPassword());
                        if (!password.equals(administrator.getPassword())){
                        } else {
                            Log.insert("Administrator "+administrator.getUserName()+" login successfully.");
                            flag = true;
                            loginAdmin(administrator);
                        }
                    }
                }
                if (!flag){
                    System.out.println(false);
                    loginError();

                }
            }
        });

        // 注册按钮的监听事件
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                register();
            }
        });

        // 设置窗口可见
        frame.setVisible(true);
    }

    private void loginError() {
        //登录失败
        JPanel panel = new JPanel();
        JButton returnButton = new JButton("return");
        JLabel label = new JLabel("Fail to login");
        panel.add(label);
        panel.add(returnButton);
        frame.add(panel);
        returnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                login();
            }

        });
    }

    private void loginClient(Client client){
        //余额查询，转账，取款，存款
        activeClient = client;
        JPanel panel = new JPanel();

        JButton moneyButton = new JButton("Balance Inquiry");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");
        JButton returnButton = new JButton("Return");

        panel.add(moneyButton);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(transferButton);
        panel.add(returnButton);

        frame.add(panel);
        moneyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.setVisible(false);
                viewMoney(client);
            }
        });
        depositButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);

                deposit(client);
            }
        });
        withdrawButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                withdraw(client);
            }
        });
        transferButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                transfer(client);
            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.setVisible(false);
                login();
            }
        });
    }

    private void loginAdmin(Administrator administrator){
        JPanel panel = new JPanel();
        administratorSet.add(administrator);
        if (request != null){
            handle(administrator);
        } else {
            JButton logButton = new JButton("view log");
            JButton userButton = new JButton("view users");
            JButton returnButton = new JButton("return");
            JLabel label = new JLabel("Hello! "+administrator.getUserName());
            JTextArea textArea = new JTextArea();
            panel.add(label);
            panel.add(logButton);
            panel.add(userButton);
            panel.add(returnButton);
            panel.add(textArea);

            frame.add(panel);
            logButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    List<String> list = Log.inquiry();
                    textArea.removeAll();
                    for (String string : list){
                        textArea.append(string+"\n");
                    }
                }
            });
            userButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    ConnectToSql connectToSql = new ConnectToSql();
                    textArea.removeAll();
                    List<String> list = connectToSql.getAllClients();
                    for (String string : list){
                        textArea.append("userName : "+string+"\n");
                    }
                }
            });
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    panel.setVisible(false);
                    login();
                }
            });
        }

    }

    //admin agree or not
    private void handle(Administrator administrator) {

        JPanel panel = new JPanel();
        JButton agreeButton = new JButton("agree");
        JButton rejectButton= new JButton("reject");
        JLabel welcome = new JLabel("Hello! "+administrator.getUserName());
        JLabel label = new JLabel(request);
        panel.add(welcome);
        panel.add(label);
        panel.add(agreeButton);
        panel.add(rejectButton);
        frame.add(panel);
        agreeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                count++;
//                System.out.println(count);
//                System.out.println(administratorSet.size());
                if(count == administratorSet.size()){
                    handleRequest();
                    count = 0;
                }
                loginClient(activeClient);
            }
        });
        rejectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                request = null;
                requestParamList.clear();
                opFail();
            }
        });
    }

    /**
     * 四种用户操作
     */
    private void transfer(Client client) {
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        //创建 标签 & 输入框 & 按钮
        JLabel oppo = new JLabel("opposite user:");
        JLabel money = new JLabel("money:");
        JTextField userNameText = new JTextField(20);
        JTextField moneyText = new JTextField(20);
        JButton confirm = new JButton("confirm");
        JButton returnButton = new JButton("return");

        // 设置标签的大小和位置
        int x,y,width,height;
        x = 150;
        y = 100;
        oppo.setBounds(x, y+50, 80, 25);
        money.setBounds(x, y+80, 80, 25);

        userNameText.setBounds(x+40, y+50, 165, 25);
        moneyText.setBounds(x+40, y+80, 165, 25);

        confirm.setBounds(220, y+110, 80, 25);
        returnButton.setBounds(220, y+140, 80, 25);
        String[] identity = {"client","administrator"};

        // 设置面板内容
        panel.add(userNameText);
        panel.add(oppo);
        panel.add(moneyText);
        panel.add(money);
        //panel.add(identityText);
        panel.add(confirm);
        panel.add(returnButton);

        // 将面板加入到窗口中
        frame.add(panel);

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ConnectToSql connectToSql = new ConnectToSql();
                panel.setVisible(false);
                int source = Integer.parseInt(client.getBalance());
                int target = Integer.parseInt(moneyText.getText());
                if (target <= 0 || target > source || !connectToSql.query(userNameText.getText(),"client")){
                    moneyError();
                }
                request = "Client "+client.getUserName()+" want to transfer "+target+" to "+userNameText.getText()+".";
                requestParamList.add(client.getUserName());
                requestParamList.add(userNameText.getText());
                requestParamList.add(target+"");

                if(administratorSet.size() == 0){
                    login();
                }
                for (Administrator administrator: administratorSet){
                    handle(administrator);
                }

            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.setVisible(false);
                loginClient(client);
            }
        });

    }

    private void withdraw(Client client) {
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        //创建 标签 & 输入框 & 按钮
        JLabel money = new JLabel("money:");
        JTextField moneytext = new JTextField(20);
        JButton confirm = new JButton("confirm");
        JButton returnButton = new JButton("return");

        // 设置标签的大小和位置
        int x,y,width,height;
        x = 150;
        y = 100;
        money.setBounds(x, y+80, 80, 25);

        moneytext.setBounds(x+40, y+80, 165, 25);

        confirm.setBounds(220, y+110, 80, 25);
        returnButton.setBounds(220, y+140, 80, 25);
        String[] identity = {"client","administrator"};

        // 设置面板内容
        panel.add(moneytext);
        panel.add(money);
        //panel.add(identityText);
        panel.add(confirm);
        panel.add(returnButton);

        // 将面板加入到窗口中
        frame.add(panel);

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.setVisible(false);
                int source = Integer.parseInt(client.getBalance());
                int target = Integer.parseInt(moneytext.getText());
                if (target <= 0 || target > source){
                    moneyError();
                }
                request = "Client "+client.getUserName()+" want to withdraw "+target+".";
                requestParamList.add(client.getUserName());
                requestParamList.add(target+"");
                if(administratorSet.size() == 0){
                    login();
                }
                for (Administrator administrator: administratorSet){
                    loginAdmin(administrator);
                }
            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.setVisible(false);
                loginClient(client);
            }
        });
    }

    private void handleRequest() {
        UserTP userTP = new UserTP();
        ConnectToSql connectToSql = new ConnectToSql();
        Client client = connectToSql.constructClient(requestParamList.get(0));
        if (requestParamList.size() == 3){
            Client target = connectToSql.constructClient(requestParamList.get(1));
            userTP.transfer(client,target, Integer.parseInt(requestParamList.get(2)));
        } else if (request.contains("deposit")) {
            userTP.deposit(client,Integer.parseInt(requestParamList.get(1)));
        } else {
            userTP.withdraw(client,Integer.parseInt(requestParamList.get(1)));
        }
        String[] split = request.split("want to ");
        Log.insert(split[0] + split[1]);
        request = null;
        requestParamList.clear();
    }

    private void deposit(Client client) {
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        //创建 标签 & 输入框 & 按钮
        JLabel money = new JLabel("money:");
        JTextField moneytext = new JTextField(20);
        JButton confirm = new JButton("confirm");
        JButton returnButton = new JButton("return");

        // 设置标签的大小和位置
        int x,y,width,height;
        x = 150;
        y = 100;
        money.setBounds(x, y+80, 80, 25);

        moneytext.setBounds(x+40, y+80, 165, 25);

        confirm.setBounds(220, y+110, 80, 25);
        returnButton.setBounds(220, y+140, 80, 25);
        String[] identity = {"client","administrator"};

        // 设置面板内容
        panel.add(moneytext);
        panel.add(money);
        //panel.add(identityText);
        panel.add(confirm);
        panel.add(returnButton);

        // 将面板加入到窗口中
        frame.add(panel);

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.setVisible(false);
                int target = Integer.parseInt(moneytext.getText());
                if (target <= 0 ){
                    moneyError();
                }
                request = "Client "+client.getUserName()+" want to deposit "+target+".";
                requestParamList.add(client.getUserName());
                requestParamList.add(target+"");
                if (administratorSet.size()==0){//无在线管理员则登录
                    login();
                }
                for (Administrator administrator: administratorSet){
                    handle(administrator);
                }
            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.setVisible(false);
                loginClient(client);
            }
        });
    }

    private void viewMoney(Client client) {
        JPanel panel = new JPanel();
        JButton returnButton = new JButton("return");
        client = new ConnectToSql().constructClient(client.getUserName());
        activeClient = client;
        JLabel label = new JLabel("balance: "+ client.getBalance());
        Log.insert("Client "+client.getUserName()+" view the balance.");
        panel.add(label);
        panel.add(returnButton);
        frame.add(panel);
        returnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                loginClient(activeClient);
            }

        });
        return ;
    }

    /**
     * 操作异常
     */
    private void opFail() {
        JPanel panel = new JPanel();
        JButton returnButton = new JButton("return");
        JLabel label = new JLabel("Operation failed");
        panel.add(label);
        panel.add(returnButton);
        frame.add(panel);
        returnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                loginClient(activeClient);
            }

        });
    }

    private void moneyError() {
        JPanel panel = new JPanel();
        JButton returnButton = new JButton("return");
        JLabel label = new JLabel("Invalid input");
        panel.add(label);
        panel.add(returnButton);
        frame.add(panel);
        returnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                loginClient(activeClient);
            }

        });
    }



    /**
     * 用户注册
     */
    public void register(){
        //注册
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        //创建 标签 & 输入框 & 按钮
        JLabel userLabel = new JLabel("username:");
        JLabel passwordLabel = new JLabel("password:");
        JLabel identityLabel = new JLabel("identity:");
        JTextField userNameText = new JTextField(20);
        JPasswordField userPasswordText = new JPasswordField(20);
        userPasswordText.setEchoChar('*');
        //JTextField identityText = new JTextField(20);
        JButton returnButton = new JButton("return");
        JButton registerButton = new JButton("register");

        // 设置标签的大小和位置
        int x,y,width,height;
        x = 150;
        y = 100;
        userLabel.setBounds(x, y+20, 80, 25);
        passwordLabel.setBounds(x, y+50, 80, 25);
        identityLabel.setBounds(x, y+80, 80, 25);

        userNameText.setBounds(x+40, y+20, 165, 25);
        userPasswordText.setBounds(x+40, y+50, 165, 25);
        //identityText.setBounds(x+40, y+80, 165, 25);

        returnButton.setBounds(220, y+140, 80, 25);
        registerButton.setBounds(220, y+110, 80, 25);
        String[] identity = {"client","administrator"};
        JComboBox comBox = new JComboBox();//下拉列表
        comBox.addItem(identity[0]);
        comBox.addItem(identity[1]);
        comBox.setBounds(x+40, y+80, 165, 25);
        panel.add(comBox);

        // 设置面板内容
        panel.add(userLabel);
        panel.add(userNameText);
        panel.add(passwordLabel);
        panel.add(userPasswordText);
        panel.add(identityLabel);
        //panel.add(identityText);
        panel.add(returnButton);
        panel.add(registerButton);

        // 将面板加入到窗口中
        frame.add(panel);

        // 按钮的监听事件
        returnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                login();
            }

        });

        // 注册按钮的监听事件
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 检测身份
                ConnectToSql connectToSql = new ConnectToSql();
                panel.setVisible(false);
                boolean flag = false;
                if (comBox.getSelectedItem().toString().equals(identity[0])) {
                    // 查找该用户
                    if (!connectToSql.query(userNameText.getText(),identity[0])) {
                        Client client = new Client(userNameText.getText(),new String(userPasswordText.getPassword()),"0");
                        if(!connectToSql.insertClient(client)){
                        } else {
                            Log.insert("Client " + client.getUserName() + " register successfully.");
                            flag = true;
                            registerSuccess();
                        }
                    }
                } else if (comBox.getSelectedItem().toString().equals(identity[1])) {
                    // 查找该用户
                    if (!connectToSql.query(userNameText.getText(),identity[1])) {
                        Administrator administrator = new Administrator(userNameText.getText(),new String(userPasswordText.getPassword()));
                        if(!connectToSql.insertManager(administrator)){
                        } else {
                            Log.insert("Administrator " + administrator.getUserName() + " register successfully.");
                            flag = true;
                            registerSuccess();
                        }
                    }
                }
                if (!flag){
                    registerError(userNameText.getText());
                }
            }
        });

    }

    private void registerError(String userName) {
        //注册过
        JPanel panel = new JPanel();
        JButton returnButton = new JButton("return");
        JLabel label = new JLabel(userName+" has been registered.");
        panel.add(label);
        panel.add(returnButton);
        frame.add(panel);
        returnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                login();
            }

        });
    }

    private void registerSuccess() {
        //注册成功
        JPanel panel = new JPanel();
        JButton returnButton = new JButton("return");
        JLabel label = new JLabel("Succeed to register.");
        panel.add(label);
        panel.add(returnButton);
        frame.add(panel);
        returnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(false);
                login();
            }

        });
    }

}
