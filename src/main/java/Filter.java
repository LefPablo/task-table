import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet(name = "Filter")
public class Filter extends HttpServlet {
    static private ServletConfig config;
    public void init (ServletConfig config) throws ServletException
    {
        this.config = config;
    }

    static public String getContextPath() {
        return config.getServletContext().getContextPath();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        //get values from request
        String assignee = request.getParameter("assignee");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        //if all parameters are empty than send page without table else table with filter
        String result = null;
        if (assignee == null && startDate == null && endDate == null) {
            result = Controller.getIndexPage();
        } else {
            ResultSet set = Controller.tasksByFilters(assignee, startDate, endDate);
            result = Controller.getIndexPage(set);
        }

        //send ready page
        response.setStatus(200);
        writer.println(result);
    }
}
