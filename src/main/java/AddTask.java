import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet(name = "AddTask")
public class AddTask extends HttpServlet {
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

        String result = Controller.getAddPage();

        response.setStatus(200);
        writer.println(result);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        //get request body and parse body to json
//        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//        JSONObject json = null;
//        try {
//            json = (JSONObject) new JSONParser().parse(body);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        //get values from json
//        String summary = json.get("summary").toString();
//        String assignee = json.get("assignee").toString();
//        String startDate = json.get("startDate").toString();
//        String endDate = json.get("endDate").toString();

        //get values from request
        String summary = request.getParameter("summary");
        String assignee = request.getParameter("assignee");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String result = "";

        //if data added success then status 200 else 500
        if (Controller.addTaskToDb(summary, assignee, startDate, endDate)) {
            response.setStatus(200);
            System.out.println("success");
            jsonResponse.put("Status", response.getStatus());
            jsonResponse.put("Message", "Data successfully added to DB");
            result = Controller.getIndexPage();
            writer.println(result);
        } else {
            response.setStatus(500);
            System.out.println("fail");
            jsonResponse.put("Status", response.getStatus());
            jsonResponse.put("Message", "Error, invalid data or SQL request is failed. More details on the server.");
            writer.println(jsonResponse);
        }
    }
}
