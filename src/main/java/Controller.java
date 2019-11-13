import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;


public class Controller {
    public static boolean addTask(String summary, String assignee, Date startDate, Date endDate) throws SQLException {
        //connect to db and insert row
        Connection conn = DataBase.getDb().connection;
        String q = "insert into TASKS(SUMMARY, ASSIGNEE, STARTDATE, ENDDATE) values(?, ?, ?, ?)";
        PreparedStatement preparedSt = conn.prepareStatement(q);
        preparedSt.setString(1, summary);
        preparedSt.setString(2, assignee);
        preparedSt.setDate(3, startDate);
        preparedSt.setDate(4, endDate);
        return preparedSt.executeUpdate() != 0;
    }

    public static boolean addTaskToDb(String summary, String assignee, String startDate, String endDate) throws SQLException {
        Date start = stringDateToSqlDate(startDate);
        Date end = stringDateToSqlDate(endDate);
        addTask(summary, assignee, start, end);
        return true;
    }

    public static int countOfRecords() throws SQLException {
        int count = 0;
        Connection conn = DataBase.getDb().connection;
        Statement st = conn.createStatement();

        ResultSet result;
        result = st.executeQuery("SELECT COUNT(*) FROM TASKS");
        if(result.next()) {
            count = result.getInt(1);
        }

        System.out.println(count);
        return count;
    }

    public static Date stringDateToSqlDate (String source) {
        String dataFormat = "dd/MM/yyyy";
        Date sqlDate = null;
        if (source != null) {
            //parse date from request to localDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dataFormat);
            //regular expression comparison
            sqlDate = Date.valueOf(
                    LocalDate.parse(source, formatter)
            );
        }
        return sqlDate;
    }

    public static ResultSet getTasksByFilters(String assignee, Date startDate, Date endDate) throws SQLException {
        Connection conn = DataBase.getDb().connection;
        PreparedStatement preparedSt = null;
        ResultSet result;

        String q = "SELECT * FROM TASKS";
        boolean isMultiCondition = false;
        if (assignee != null || startDate != null || endDate != null) {
            q += " WHERE";
            if (assignee != null) {
                if (isMultiCondition) {
                    q += " AND";
                } else {
                    isMultiCondition = true;
                }
                q += (" ASSIGNEE='" + assignee + "'");
            }
            if (startDate != null) {
                if (isMultiCondition) {
                    q += " AND";
                } else {
                    isMultiCondition = true;
                }
                q += (" STARTDATE>='" + startDate + "'");
            }
            if (endDate != null) {
                if (isMultiCondition) {
                    q += " AND";
                } else {
                    isMultiCondition = true;
                }
                q += (" ENDDATE<='" + endDate + "'");
            }
        }
        System.out.println(q);
        preparedSt = conn.prepareStatement(q);
        result = preparedSt.executeQuery();

        return result;
    }

}
