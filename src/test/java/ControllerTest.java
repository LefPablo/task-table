import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
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

    @Test //used when data base in-memory mod
    public void countOfRecords() {
        String source1 = "Director of corp";
        String source2 = "Bob Tempo";
        String source3 = "2019-11-10";
        String source4 = "2019-11-21";

        int actual = 2;
        Date start = Date.valueOf(source3);
        Date end = Date.valueOf(source4);
        int expected = 0;
        try {
            Controller.addTask("Director of corp", "Bob Tempo", start, end);
            Controller.addTask("Director of corp", "Bob Tempo", start, end);
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
}