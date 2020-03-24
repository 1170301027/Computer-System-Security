public class Login extends JFrame {

    String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    String DB_URL = "jdbc:mysql://localhost:3306/lab3?useSSL=false&serverTimezone=UTC";
    Connection conn = null;
    Statement stmt = null;
    Statement stmt1 = null;
    Statement stmt2 = null;

    JPanel user = new JPanel();
    JPanel passWord = new JPanel();

    JButton getButton = new JButton("登陆");

    JTextField userField = new JTextField(10);
    JTextField passField = new JTextField(10);

    String name;
    String passwd;

    Logger logger = Logger.getLogger("lab3");
    FileHandler fileHandler = new FileHandler("lab3.log");

    public Login() throws IOException {
        fileHandler.setLevel(Level.INFO);
        fileHandler.setFormatter(new Formatter() {

            @Override
            public String format(LogRecord record) {
                // TODO Auto-generated method stub
                return record.getMessage() + "\n";
            }
        });
        logger.setUseParentHandlers(false);
        logger.addHandler(fileHandler);
        setLayout(null);
        setJPanel(user, "用户名");
        user.add(userField);
        setJPanel(passWord, "密码   ");
        passWord.add(passField);
        user.setBounds(150,50,200,50);
        passWord.setBounds(150, 100, 200, 50);
        getButton.setBounds(150, 150, 200, 20);
        this.add(user);
        this.add(passWord);
        this.add(getButton);
        this.setTitle("Login");
        this.setSize(500, 300);
        this.setLocation(200, 120);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        getButton.addActionListener(event -> {
            name = userField.getText();
            passwd = passField.getText();
            try {
                login(name, passwd);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (name.length() <= 1) {
                bossGui();
            } else if (name.length() <= 2) {
                manageGui();
            } else {
                try {
                    employeeGui();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void setJPanel(JPanel panel, String name) {
        // TODO Auto-generated method stub
        Font font = new Font("宋体", Font.PLAIN, 22);
        JLabel label = new JLabel(name);
        label.setFont(font);
        panel.add(label);
    }

    public void login(String name, String passwd) throws ClassNotFoundException, SQLException {

        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL,name,passwd);
        stmt = conn.createStatement();
        stmt1 = conn.createStatement();
        stmt2 = conn.createStatement();
        logger.info("login " + name);
    }

    public void bossGui() {
        JFrame jFrame = new JFrame();
        jFrame.setLayout(null);
        JButton employeeButton = new JButton();
        employeeButton.setBounds(110,20,100,20);
        employeeButton.setText("管理雇员");
        JButton managerButton = new JButton();
        managerButton.setBounds(110,70,100,20);
        managerButton.setText("管理经理");
        JButton goodButton = new JButton();
        goodButton.setBounds(110,120,100,20);
        goodButton.setText("管理商品");
        jFrame.add(employeeButton);
        jFrame.add(managerButton);
        jFrame.add(goodButton);
        jFrame.setBounds(500,500,300, 200);
        jFrame.setVisible(true);

        employeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    manageEmployee();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    manageManager();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        goodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    manageGood();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void employeeGui() throws SQLException {
        JFrame frame = new JFrame();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"商品id","商品名称","价格","数量"});
        String sql = "SELECT * FROM good ";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String birthday = rs.getString("price");
            int age = rs.getInt("number");
            model.addRow(new Object[]{id,name, birthday, age});
        }
        JTable fk = new JTable(model);
        JScrollPane jsp = new JScrollPane(fk);
        jsp.setBounds(50, 10, 500, 250);
        JTextField idFiled = new JTextField(10);
        JTextField numberFiled = new JTextField(10);
        JButton modifyButton = new JButton();
        modifyButton.setBounds(20, 350,60,20);
        modifyButton.setText("修改");
        idFiled.setBounds(100, 350, 80, 20);
        numberFiled.setBounds(250, 350, 200, 20);
        frame.add(modifyButton);
        frame.add(idFiled);
        frame.add(numberFiled);
        frame.setBounds(100, 100, 500, 500);
        frame.add(jsp);
        frame.setVisible(true);
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String id = idFiled.getText();
                String number = numberFiled.getText();
                try {
                    updateGood(id, number);
                    logger.info(name + "update good " + id + " " + number);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void manageGui() {
        JFrame jFrame = new JFrame();
        jFrame.setLayout(null);
        JButton employeeButton = new JButton();
        employeeButton.setBounds(110,20,100,20);
        employeeButton.setText("管理雇员");
        JButton goodButton = new JButton();
        goodButton.setBounds(110,120,100,20);
        goodButton.setText("管理商品");
        jFrame.add(employeeButton);
        jFrame.add(goodButton);
        jFrame.setBounds(500,500,300, 200);
        jFrame.setVisible(true);

        employeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    managerManageEmployee();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        goodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    manageGood();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void manageEmployee() throws SQLException {
        JFrame frame = new JFrame();
        frame.setLayout(null);
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"员工id","员工姓名","生日","年龄"});
        String sql = "SELECT * FROM employee ";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String birthday = rs.getString("birthday");
            int age = rs.getInt("age");
            model.addRow(new Object[]{id,name, birthday, age});
        }
        JTable fk = new JTable(model);
        JScrollPane jsp = new JScrollPane(fk);
        jsp.setBounds(0, 0, 500, 300);
        JTextField idFiled = new JTextField(10);
        JTextField nameFiled = new JTextField(10);
        JTextField birthdayFiled = new JTextField(10);
        JTextField ageFiled = new JTextField(5);
        JTextField removeFiled = new JTextField(40);
        JButton addButton = new JButton();
        addButton.setBounds(20, 350,60,20);
        addButton.setText("添加");
        JButton removeButton = new JButton();
        removeButton.setText("删除");
        removeButton.setBounds(20,400,100, 20);
        idFiled.setBounds(100, 350, 80, 20);
        nameFiled.setBounds(200, 350, 80, 20);
        birthdayFiled.setBounds(300, 350, 80, 20);
        ageFiled.setBounds(400, 350, 80, 20);
        removeFiled.setBounds(150, 400, 200, 20);
        frame.add(addButton);
        frame.add(idFiled);
        frame.add(nameFiled);
        frame.add(birthdayFiled);
        frame.add(ageFiled);
        frame.add(removeButton);
        frame.add(removeFiled);
        frame.setBounds(100, 100, 500, 500);
        frame.add(jsp);
        frame.setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String id = idFiled.getText();
                String addname = nameFiled.getText();
                String birthday = birthdayFiled.getText();
                String age = ageFiled.getText();
                try {
                    addEmployee(id, addname, birthday, age);
                    logger.info(name + "add employee " + id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    deletePeople(removeFiled.getText(), "employee");
                    logger.info(name + "delete employee " + removeFiled.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        JButton upButton = new JButton();
        upButton.setBounds(400, 400, 100, 20);
        upButton.setText("升级");
        frame.add(upButton);
        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    upQuanxian(removeFiled.getText());
                    logger.info(name + " up level " + removeFiled.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void manageManager() throws SQLException {
        JFrame frame = new JFrame();
        frame.setLayout(null);
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"经理id","经理姓名","生日","年龄"});
        String sql = "SELECT * FROM manager ";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String birthday = rs.getString("birthday");
            int age = rs.getInt("age");
            model.addRow(new Object[]{id,name, birthday, age});
        }
        JTable fk = new JTable(model);
        JScrollPane jsp = new JScrollPane(fk);
        jsp.setBounds(00, 0, 500, 300);
        JTextField idFiled = new JTextField(10);
        JTextField nameFiled = new JTextField(10);
        JTextField birthdayFiled = new JTextField(10);
        JTextField ageFiled = new JTextField(5);
        JTextField removeFiled = new JTextField(40);
        JButton addButton = new JButton();
        addButton.setBounds(20, 350,60,20);
        addButton.setText("添加");
        JButton removeButton = new JButton();
        removeButton.setText("删除");
        removeButton.setBounds(20,400,100, 20);
        idFiled.setBounds(100, 350, 80, 20);
        nameFiled.setBounds(200, 350, 80, 20);
        birthdayFiled.setBounds(300, 350, 80, 20);
        ageFiled.setBounds(400, 350, 80, 20);
        removeFiled.setBounds(150, 400, 200, 20);
        frame.add(addButton);
        frame.add(idFiled);
        frame.add(nameFiled);
        frame.add(birthdayFiled);
        frame.add(ageFiled);
        frame.add(removeButton);
        frame.add(removeFiled);
        frame.setBounds(100, 100, 500, 500);
        frame.add(jsp);
        frame.setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String id = idFiled.getText();
                String addname = nameFiled.getText();
                String birthday = birthdayFiled.getText();
                String age = ageFiled.getText();
                try {
                    addManager(id, addname, birthday, age);
                    logger.info(name + " add manager " + id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    deletePeople(removeFiled.getText(), "manager");
                    logger.info(name + " delete manager " + removeFiled.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        JButton downButton = new JButton();
        downButton.setBounds(400, 400, 100, 20);
        downButton.setText("降权");
        frame.add(downButton);
        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    deleteQx(removeFiled.getText());
                    logger.info(name + " down level " + removeFiled.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void manageGood() throws SQLException {
        JFrame frame = new JFrame();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"商品id","商品名称","价格","数量"});
        String sql = "SELECT * FROM good ";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            int number = rs.getInt("number");
            model.addRow(new Object[]{id,name, price, number});
        }
        JTable fk = new JTable(model);
        JScrollPane jsp = new JScrollPane(fk);
        jsp.setBounds(50, 10, 500, 250);
        JTextField idFiled = new JTextField(10);
        JTextField nameFiled = new JTextField(10);
        JTextField priceFiled = new JTextField(10);
        JTextField numberFiled = new JTextField(5);
        JTextField removeFiled = new JTextField(40);
        JButton addButton = new JButton();
        addButton.setBounds(20, 350,60,20);
        addButton.setText("添加");
        JButton removeButton = new JButton();
        removeButton.setText("删除");
        removeButton.setBounds(20,400,100, 20);
        idFiled.setBounds(100, 350, 80, 20);
        nameFiled.setBounds(200, 350, 80, 20);
        priceFiled.setBounds(300, 350, 80, 20);
        numberFiled.setBounds(400, 350, 80, 20);
        removeFiled.setBounds(150, 400, 200, 20);
        frame.add(addButton);
        frame.add(idFiled);
        frame.add(nameFiled);
        frame.add(priceFiled);
        frame.add(numberFiled);
        frame.add(removeButton);
        frame.add(removeFiled);
        frame.setBounds(100, 100, 500, 500);
        frame.add(jsp);
        frame.setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String id = idFiled.getText();
                String addname = nameFiled.getText();
                String price = priceFiled.getText();
                String number = numberFiled.getText();
                try {
                    addGood(id, addname, price, number);
                    logger.info(name + " add good " + id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String[] id = removeFiled.getText().split(" ");
                for (int i = 0; i < id.length; i++) {
                    String sql = "DELETE FROM good WHERE id=" + id[i];
                    try {
                        stmt.executeUpdate(sql);
                        logger.info(name + " remove good " + id[i]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void managerManageEmployee() throws SQLException {
        JFrame frame = new JFrame();
        frame.setLayout(null);
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"员工id","员工姓名","生日","年龄"});
        String sql = "SELECT * FROM employee ";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String birthday = rs.getString("birthday");
            int age = rs.getInt("age");
            model.addRow(new Object[]{id,name, birthday, age});
        }
        JTable fk = new JTable(model);
        JScrollPane jsp = new JScrollPane(fk);
        jsp.setBounds(0, 0, 500, 300);
        JTextField idFiled = new JTextField(10);
        JTextField nameFiled = new JTextField(10);
        JTextField birthdayFiled = new JTextField(10);
        JTextField ageFiled = new JTextField(5);
        JButton addButton = new JButton();
        addButton.setBounds(20, 350,60,20);
        addButton.setText("修改");
        idFiled.setBounds(100, 350, 80, 20);
        nameFiled.setBounds(200, 350, 80, 20);
        birthdayFiled.setBounds(300, 350, 80, 20);
        ageFiled.setBounds(400, 350, 80, 20);
        frame.add(addButton);
        frame.add(idFiled);
        frame.add(nameFiled);
        frame.add(birthdayFiled);
        frame.add(ageFiled);
        frame.setBounds(100, 100, 500, 500);
        frame.add(jsp);
        frame.setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String id = idFiled.getText();
                String addname = nameFiled.getText();
                String birthday = birthdayFiled.getText();
                String age = ageFiled.getText();
                try {
                    updateEmployee(id, addname, birthday, age);
                    logger.info(name + " update employee " + id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addManager(String id, String name, String birthday, String age) throws SQLException {
        String sql = "create user '" + id + "'@'%' identified by 'hello'";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO `employee` VALUES (" + id + ", '"+ name + "','" + birthday + "'," + age +  ")";
        stmt.executeUpdate(sql);
        sql = "grant ALL on lab3.employee  to '" + id +"'";
        stmt.executeUpdate(sql);
        sql = "grant ALL on lab3.good  to '" + id + "'";
        stmt.executeUpdate(sql);
    }

    public void deletePeople(String ids, String table) throws SQLException {
        String[] id = ids.split(" ");
        for (int i = 0; i < id.length; i++) {
            String sql = "drop user '" + id[i] + "'";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM " + table + " WHERE id=" + id[i];
            stmt.executeUpdate(sql);
        }
    }

    public void addEmployee(String id, String name, String birthday, String age) throws SQLException {
        String sql = "create user '" + id + "'@'%' identified by 'hello'";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO `employee` VALUES (" + id + ", '"+ name + "','" + birthday + "'," + age +  ")";
        stmt.executeUpdate(sql);
        sql = "grant SELECT on lab3.good  to '" + id +"'";
        stmt.executeUpdate(sql);
        sql = "grant update(number) on lab3.good  to '" + id + "'";
        stmt.executeUpdate(sql);
    }

    public void addGood(String id, String name, String price, String number) throws SQLException {
        String sql = "INSERT INTO `good` VALUES (" + id + ", '"+ name + "'," + price + "," + number +  ")";
        stmt.executeUpdate(sql);
    }

    public void updateEmployee(String id, String name, String birthday, String age) throws SQLException {
        String sql = "DELETE FROM employee WHERE id=" + id;
        stmt.executeUpdate(sql);
        sql = "INSERT INTO `employee` VALUES (" + id + ", '"+ name + "','" + birthday + "'," + age +  ")";
        stmt.executeUpdate(sql);
    }
    public void updateGood(String id, String number) throws SQLException {
        String sql ="UPDATE good SET number=" + number + " WHERE id=" + id;
        stmt.executeUpdate(sql);
    }
    public void upQuanxian(String ids) throws SQLException {
        String[] id = ids.split(" ");
        for (int i = 0; i < id.length; i++) {
            int idNumber = Integer.parseInt(id[i]) % 100;
            String sql = "drop user '" + id[i] + "'";
            stmt.executeUpdate(sql);
            sql = "create user '" + idNumber + "'@'%' identified by 'hello'";
            stmt.executeUpdate(sql);
            sql = "grant ALL on lab3.employee  to '" + idNumber +"'";
            System.out.println(sql);
            stmt.executeUpdate(sql);
            sql = "grant ALL on lab3.good  to '" + idNumber + "'";
            stmt.executeUpdate(sql);
            sql = "SELECT * FROM employee where id=" + id[i];
            ResultSet rs = stmt1.executeQuery(sql);
            while (rs.next()) {
                int hereid = rs.getInt("id");
                String name = rs.getString("name");
                String birthday = rs.getString("birthday");
                int age = rs.getInt("age");
                sql = "INSERT INTO `manager` VALUES (" + (hereid % 100) + ", '"+ name + "','" + birthday + "'," + age +  ")";
                stmt.executeUpdate(sql);
            }
            sql = "DELETE FROM employee WHERE id=" + id[i];
            stmt.executeUpdate(sql);
        }
    }
    public void deleteQx(String ids) throws SQLException {
        String[] id = ids.split(" ");
        for (int i = 0; i < id.length; i++) {
            int idNumber = Integer.parseInt(id[i]) + 200;
            String sql = "drop user '" + id[i] + "'";
            stmt.executeUpdate(sql);
            sql = "create user '" + idNumber + "'@'%' identified by 'hello'";
            stmt.executeUpdate(sql);
            sql = "grant SELECT on lab3.good  to '" + idNumber +"'";
            stmt.executeUpdate(sql);
            sql = "grant update(number) on lab3.good  to '" + idNumber + "'";
            stmt.executeUpdate(sql);
            sql = "SELECT * FROM manager where id=" + id[i];
            ResultSet rs = stmt2.executeQuery(sql);
            while (rs.next()) {
                int hereid = rs.getInt("id");
                String name = rs.getString("name");
                String birthday = rs.getString("birthday");
                int age = rs.getInt("age");
                sql = "INSERT INTO `employee` VALUES (" + (hereid + 200) + ", '"+ name + "','" + birthday + "'," + age +  ")";
                stmt.executeUpdate(sql);
            }
            sql = "DELETE FROM manager WHERE id=" + id[i];
            stmt.executeUpdate(sql);
        }
    }
}
