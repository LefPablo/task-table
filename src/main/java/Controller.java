import java.sql.*;
import java.time.*;
import java.time.format.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


public class Controller {
    public static boolean addTask(String summary, String assignee, Date startDate, Date endDate) throws SQLException {
        //connect to db and insert row
        //return true if added success, else false
        Connection conn = DataBase.getDb().connection;
        String q = "insert into TASKS(SUMMARY, ASSIGNEE, STARTDATE, ENDDATE) values(?, ?, ?, ?)";
        PreparedStatement preparedSt = conn.prepareStatement(q);
        preparedSt.setString(1, summary);
        preparedSt.setString(2, assignee);
        preparedSt.setDate(3, startDate);
        preparedSt.setDate(4, endDate);
        return preparedSt.executeUpdate() != 0;
    }

    public static boolean addTaskToDb(String summary, String assignee, String startDate, String endDate) {
        //parse string of date to Date format and call addTask function
        //if success return true else false
        boolean result = false;
        try {
            Date start = stringDateToSqlDate(startDate);
            Date end = stringDateToSqlDate(endDate);
            result = addTask(summary, assignee, start, end);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (DateTimeParseException e) {
            System.out.println(e);
        }
        return result;
    }

    public static int countOfRecords() throws SQLException {
        //return count of records in the table
        int count = 0;
        Connection conn = DataBase.getDb().connection;
        Statement st = conn.createStatement();

        ResultSet result;
        result = st.executeQuery("SELECT COUNT(*) FROM TASKS");
        if(result.next()) {
            count = result.getInt(1);
        }
        return count;
    }

    public static Date stringDateToSqlDate (String source) throws DateTimeParseException {
        //parse string date to SQL Date format
        String dataFormat = "dd/MM/yyyy";
        Date sqlDate = null;
        if (source != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dataFormat);
            sqlDate = Date.valueOf(
                    LocalDate.parse(source, formatter)
            );
        }
        return sqlDate;
    }

    public static ResultSet getListOfAssignees() throws SQLException {
        Connection conn = DataBase.getDb().connection;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery("SELECT DISTINCT ASSIGNEE FROM TASKS");

        return result;
    }

    public static ResultSet getTasksByFilters(String assignee, Date startDate, Date endDate) throws SQLException {
        Connection conn = DataBase.getDb().connection;
        PreparedStatement preparedSt = null;
        ResultSet result;

        //formation of query
        //initial set
        String q = "SELECT * FROM TASKS";

        //adds values to query, if is multi condition then also adds "AND"
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
        //make query to DB
        preparedSt = conn.prepareStatement(q);
        result = preparedSt.executeQuery();

        return result;
    }

    public static ResultSet tasksByFilters(String assignee, String startDate, String endDate) {
        //parse string of date to Date format and call getTasksByFilters function
        //return set of query result
        ResultSet result = null;
        try {
            Date start = stringDateToSqlDate(startDate);
            Date end = stringDateToSqlDate(endDate);
            result = getTasksByFilters(assignee, start, end);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (DateTimeParseException e) {
            System.out.println(e);
        }
        return result;
    }

    public static String getIndexPage(String assignee, String startDate, String endDate) {

        LinkedList<String> assigneeArray = new LinkedList<String>();
        LinkedList<Task> taskArray = new LinkedList<Task>();
        ResultSet assignees = null;
        ResultSet tasks = null;
        try {
            assignees = getListOfAssignees();
            while(assignees.next()) {
                assigneeArray.add(assignees.getString(1));
            }
            tasks = tasksByFilters(assignee, startDate, endDate);
            while(tasks.next()) {
                Task task = new Task(tasks.getInt(1), tasks.getString(2), tasks.getString(3), tasks.getString(4), tasks.getString(5));
                taskArray.add(task);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return vtlIndexPage(assigneeArray, taskArray);
    }

    public static String getIndexPage() {
        LinkedList<String> assigneeArray = new LinkedList<String>();
        LinkedList<Task> taskArray = new LinkedList<Task>();
        ResultSet assignees = null;
        try {
            assignees = getListOfAssignees();
            while(assignees.next()) {
                assigneeArray.add(assignees.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return vtlIndexPage(assigneeArray, taskArray);
    }

    private static String vtlIndexPage(LinkedList<String> assigneeArray, LinkedList<Task> taskArray) {
        Velocity.init();
        Template t = Velocity.getTemplate("./src/main/resources/vtl/index.vm");
        VelocityContext ctx = new VelocityContext();
        ctx.put("taskArray", taskArray);
        ctx.put("assigneeArray", assigneeArray);
        Writer writer = new StringWriter();
        t.merge(ctx, writer);
        System.out.println(writer);
        return writer.toString();
    }
}
