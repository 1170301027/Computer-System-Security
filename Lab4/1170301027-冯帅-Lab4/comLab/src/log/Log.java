package log;

import javax.swing.text.DefaultEditorKit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    static final String file = "log.txt";
    private static Logger getlogger(){
        Logger logger = Logger.getLogger("Logger");
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(file);
            fileHandler.setFormatter(simpleFormatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fileHandler);
        return logger;
    }

    public static void insert(String message){
        Logger logger = getlogger();
        Date date = new Date();
        logger.log(Level.INFO, date.getTime()+" ,"+message);
    }

    public static List<String> inquiry(){
        List<String> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String nextLine = null;
            while ((nextLine = reader.readLine())!=null){
                list.add(nextLine);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
