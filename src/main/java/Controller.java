import java.io.*;
import java.sql.*;
import java.time.*;
import java.time.format.*;

import java.util.LinkedList;
import java.util.Properties;

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

        //to avoid Exception NullPointerException
        if (assignee == null) {
            assignee = "";
        }

        //formation of query
        //initial set
        String q = "SELECT * FROM TASKS";
        //adds values to query, if is multi condition then also adds "AND"
        boolean isMultiCondition = false;
        if (!assignee.isEmpty() || startDate != null || endDate != null) {
            q += " WHERE";
            if (!assignee.isEmpty()) {
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
        Date start = null;
        Date end = null;
        //if cannot to parse string, then ignore this parameter in query (equal null)
        try {
            start = stringDateToSqlDate(startDate);
        } catch (DateTimeParseException e) {
            System.out.println(e);
        }
        try {
            end = stringDateToSqlDate(endDate);
        } catch (DateTimeParseException e) {
            System.out.println(e);
        }
        try {
            result = getTasksByFilters(assignee, start, end);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

    public static String getIndexPage(ResultSet tasks) {

        LinkedList<String> assigneeArray = new LinkedList<String>();
        LinkedList<Task> taskArray = new LinkedList<Task>();
        ResultSet assignees = null;
        try {
            assignees = getListOfAssignees();
            if (assignees != null) {
                while(assignees.next()) {
                    assigneeArray.add(assignees.getString(1));
                }
            }
            if (tasks != null) {
                while(tasks.next()) {
                    Task task = new Task(tasks.getInt(1), tasks.getString(2), tasks.getString(5), tasks.getString(3), tasks.getString(4));
                    taskArray.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return vtlIndexPage(assigneeArray, taskArray);
    }

    public static String getIndexPage() {
        return getIndexPage(null);
    }

    private static String vtlIndexPage(LinkedList<String> assigneeArray, LinkedList<Task> taskArray) {
        Properties props = new Properties();
        props.put("resource.loader", "class");
        props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        Velocity.init(props);
        String url = "/vtl/index.vm";
        Template t = Velocity.getTemplate(url);

        VelocityContext ctx = new VelocityContext();
        ctx.put("taskArray", taskArray);
        ctx.put("assigneeArray", assigneeArray);
        ctx.put("contextPath", Filter.getContextPath());
        Writer writer = new StringWriter();
        t.merge(ctx, writer);
        return writer.toString();
    }

    public static String getAddPage() {
        Properties props = new Properties();
        props.put("resource.loader", "class");
        props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        Velocity.init(props);
        String url = "/vtl/addTask.vm";
        Template t = Velocity.getTemplate(url);

        VelocityContext ctx = new VelocityContext();
        ctx.put("contextPath", Filter.getContextPath());
        Writer writer = new StringWriter();
        t.merge(ctx, writer);
        return writer.toString();
    }
}
