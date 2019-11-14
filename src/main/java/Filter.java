import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet(name = "Filter")
public class Filter extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        //get values from request
        String assignee = request.getParameter("assignee");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        //if data added success then status 200 else 500
        ResultSet result = Controller.tasksByFilters(assignee, startDate, endDate);
        response.setContentType("application/json; charset=utf-8");
        JSONArray jsonArray = new JSONArray();
        if (result != null) {
            try {
                //read line by line form result and write in json array
                while (result.next()) {
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put("id", result.getString("Id"));
                    jsonItem.put("summary", result.getString("SUMMARY"));
                    jsonItem.put("startDate", result.getDate("STARTDATE"));
                    jsonItem.put("endDate", result.getDate("ENDDATE"));
                    jsonItem.put("assignee", result.getString("ASSIGNEE"));
                    jsonArray.add(jsonItem);
                }
            } catch (SQLException e) {
                System.out.println(e);
                response.setStatus(500);
                System.out.println("fail");
                jsonResponse.put("Status", response.getStatus());
                jsonResponse.put("Message", "Error, SQL exception. More details on the server.");
                writer.println(jsonResponse);
                writer.close();
            }
        }
        //send ready json array as response
        response.setStatus(200);
        writer.println(jsonArray);
    }
}
