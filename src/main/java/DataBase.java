import java.sql.*;

public final class DataBase {
    private static DataBase db;
    public Connection connection;
    public Statement statement;

    private DataBase () {
        //get Driver class
        String DB_Driver = "org.h2.Driver";
        try {
            Class.forName(DB_Driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //connect to db and creat table
        try {   //getConnection("jdbc:h2:./Task", "sa", " "); //local db
            connection = DriverManager.getConnection("jdbc:h2:mem:test"); // in-memory db
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS TASKS(ID INT PRIMARY KEY AUTO_INCREMENT, SUMMARY VARCHAR(200) NOT NULL, STARTDATE DATE NOT NULL, ENDDATE DATE NOT NULL, ASSIGNEE VARCHAR(100) NOT NULL)");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    //singleton
    public static synchronized DataBase getDb() {
        if (db == null) {
            synchronized (DataBase.class) {
                if (db == null)
                    db = new DataBase();
            }
        }
        return db;
    }
}
