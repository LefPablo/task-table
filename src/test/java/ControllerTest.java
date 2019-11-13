import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void addTask() {
        String source1 = "Director of corp";
        String source2 = "Bob Tempo";
        String source3 = "2019-11-10";
        String source4 = "2019-11-21";

        Boolean actual = true;
        Date start = Date.valueOf(source3);
        Date end = Date.valueOf(source4);
        Boolean expected = null;
        try {
            expected = Controller.addTask(source1, source2, start, end);
        } catch (SQLException e) {
            System.out.println(e);
        }
        System.out.println("Actual:" + actual);
        System.out.println("Expected:" + expected);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addTaskToDb() {
        String source1 = "Director of corp";
        String source2 = "Bob Tempo";
        String source3 = "10/11/2019";
        String source4 = "21/11/2019";

        Boolean actual = true;
        Boolean expected = null;
        try {
            expected = Controller.addTaskToDb(source1, source2, source3, source4);
        } catch (SQLException e) {
            System.out.println(e);
        }
        System.out.println("Actual:" + actual);
        System.out.println("Expected:" + expected);
        Assert.assertEquals(expected, actual);
    }

    @Test //used when data base in-memory mod
    public void countOfRecords() {
        String source1 = "Director of corp";
        String source2 = "Bob Tempo";
        String source3 = "10/11/2019";
        String source4 = "21/11/2019";

        int actual = 2;
        int expected = 0;
        try {
            Controller.addTaskToDb(source1, source2, source3, source4);
            Controller.addTaskToDb(source1, source2, source3, source4);
            expected = Controller.countOfRecords();
        } catch (SQLException e) {
            System.out.println(e);
        }
        System.out.println("Actual:" + actual);
        System.out.println("Expected:" + expected);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void stringDateToSqlDate() {
        String source = "10/11/2019";

        String actual = "2019-11-10";
        Date date = Controller.stringDateToSqlDate(source);
        String expected = date.toString();
        System.out.println("Actual:" + actual);
        System.out.println("Expected:" + expected);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getTasksByFilters() {
        String source1 = "Director of corp";
        String source2 = "Bob Tempo";
        String source3 = "10/11/2019";
        String source4 = "21/11/2019";

        String startDate = "2019-11-10";
        String endDate = "2019-11-21";

//        int actual = 2;
//        int expected = 0;
        Date start = Date.valueOf(startDate);
        Date end = Date.valueOf(endDate);

        ResultSet result = null;
        try {
            Controller.addTaskToDb(source1, source2, source3, source4);
            Controller.addTaskToDb(source1, source2, "13/11/2019", "21/11/2019");
            result = Controller.getTasksByFilters(null, start, end);

            while (result.next()) {
                System.out.println(result.getString(3));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

//        System.out.println("Actual:" + actual);
//        System.out.println("Expected:" + expected);
//        Assert.assertEquals(expected, actual);
    }
}