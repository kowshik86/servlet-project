import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DBConnection extends HttpServlet {

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String s1 = request.getParameter("course");

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        out.println("<html><body>");
        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/college", "root", "root");
            stmt = con.createStatement();

            // Query students table
            rs = stmt.executeQuery("SELECT * FROM students");

            // Display results
            out.println("<center><h1>Students having course: " + s1 + "</h1>");
            out.println("<table border='1'><tr><th>Student Name</th></tr>");

            boolean found = false;
            while (rs.next()) {
                String course = rs.getString("course"); // use column name for clarity
                if (s1 != null && s1.equalsIgnoreCase(course)) {
                    out.println("<tr><td>" + rs.getString("student_name") + "</td></tr>");
                    found = true;
                }
            }

            if (!found) {
                out.println("<tr><td>No students found for course: " + s1 + "</td></tr>");
            }

            out.println("</table></center>");
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC Driver not found.", e);
        } catch (SQLException e) {
            throw new ServletException("Could not display records.", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ignored) {
            }
        }

        out.println("</body></html>");
        out.close();
    }
}
