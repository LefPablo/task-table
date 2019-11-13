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

//    public static ResultSet getAllRecords() throws SQLException {
//        Connection conn = DataBase.getDb().connection;
//        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//        ResultSet result = st.executeQuery("SELECT * FROM TEST");
//
//        while(result.next()) {
//            System.out.println(result.getString("TIMESTAMP"));
//        }
//
//        return result;
//    }

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

//    public static ResultSet getTasksByFilters(String assignee, Date startDate, Date endDate) throws SQLException {
//        Connection conn = DataBase.getDb().connection;
//        PreparedStatement preparedSt = null;
//        ResultSet result;
//
//        String q = null;
//        if (assignee == null) {
//            if (date == null) {
//                q = "SELECT * FROM TEST";
//                preparedSt = conn.prepareStatement(q);
//            } else {
//                q = "SELECT * FROM TEST WHERE DAY(TIMESTAMP)=? AND MONTH(TIMESTAMP)=? AND YEAR(TIMESTAMP)=?";
//                preparedSt = conn.prepareStatement(q);
//                preparedSt.setString(1, String.valueOf(date.getDayOfMonth()));
//                preparedSt.setString(2, String.valueOf(date.getMonthValue()));
//                preparedSt.setString(3, String.valueOf(date.getYear()));
//            }
//        } else {
//            if (date == null) {
//                q = "SELECT * FROM TEST WHERE CARNUMBER = ? ";
//                preparedSt = conn.prepareStatement(q);
//                preparedSt.setString(1, carNumber);
//            } else {
//                q = "SELECT * FROM TEST WHERE CARNUMBER = ? AND DAY(TIMESTAMP)=? AND MONTH(TIMESTAMP)=? AND YEAR(TIMESTAMP)=?";
//                preparedSt = conn.prepareStatement(q);
//                preparedSt.setString(1, carNumber);
//                preparedSt.setString(2, String.valueOf(date.getDayOfMonth()));
//                preparedSt.setString(3, String.valueOf(date.getMonthValue()));
//                preparedSt.setString(4, String.valueOf(date.getYear()));
//            }
//        }
//        result = preparedSt.executeQuery();
//
//        return result;
//    }

}
