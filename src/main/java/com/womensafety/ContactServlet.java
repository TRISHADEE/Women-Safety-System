package com.womensafety;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;

@WebServlet("/contacts")
public class ContactServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("index.html");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String name = request.getParameter("contact_name");
        String phone = request.getParameter("phone");
        String relationship = request.getParameter("relationship");

        String sql = "INSERT INTO emergency_contacts " +
                     "(user_id, contact_name, phone, relationship) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, relationship);

            ps.executeUpdate();

            response.sendRedirect("contacts");

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<h2>Unable to add emergency contact.</h2>" +
                "<a href='contacts'>Go Back</a>"
            );
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("index.html");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        response.setContentType("text/html;charset=UTF-8");

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");

        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>Emergency Contacts</title>");

        html.append("<style>");

        html.append("*{box-sizing:border-box;margin:0;padding:0;}");

        html.append("body{");
        html.append("font-family:Arial,sans-serif;");
        html.append("background:linear-gradient(135deg,#f8f0f7,#f1e5f0);");
        html.append("min-height:100vh;");
        html.append("color:#292333;");
        html.append("padding:30px;");
        html.append("}");

        html.append(".container{");
        html.append("width:90%;");
        html.append("max-width:1000px;");
        html.append("margin:20px auto;");
        html.append("}");

        html.append(".header{");
        html.append("background:white;");
        html.append("padding:28px;");
        html.append("border-radius:20px;");
        html.append("text-align:center;");
        html.append("box-shadow:0 8px 25px rgba(0,0,0,0.08);");
        html.append("margin-bottom:25px;");
        html.append("}");

        html.append(".header-icon{font-size:45px;margin-bottom:8px;}");

        html.append(".header h1{");
        html.append("color:#6d3b8f;");
        html.append("margin-bottom:8px;");
        html.append("}");

        html.append(".header p{color:#777;}");

        html.append(".form-card{");
        html.append("background:white;");
        html.append("padding:30px;");
        html.append("border-radius:20px;");
        html.append("box-shadow:0 8px 25px rgba(0,0,0,0.07);");
        html.append("margin-bottom:25px;");
        html.append("}");

        html.append(".form-card h2{");
        html.append("color:#7b315f;");
        html.append("margin-bottom:20px;");
        html.append("}");

        html.append("label{");
        html.append("display:block;");
        html.append("font-weight:bold;");
        html.append("margin-bottom:7px;");
        html.append("font-size:14px;");
        html.append("}");

        html.append("input{");
        html.append("width:100%;");
        html.append("padding:13px;");
        html.append("border:1px solid #ddd;");
        html.append("border-radius:10px;");
        html.append("margin-bottom:18px;");
        html.append("font-size:15px;");
        html.append("}");

        html.append("input:focus{");
        html.append("outline:none;");
        html.append("border-color:#9b70ae;");
        html.append("}");

        html.append("button{");
        html.append("background:#7b315f;");
        html.append("color:white;");
        html.append("border:none;");
        html.append("padding:13px 25px;");
        html.append("border-radius:10px;");
        html.append("font-weight:bold;");
        html.append("font-size:15px;");
        html.append("cursor:pointer;");
        html.append("}");

        html.append("button:hover{background:#65264d;}");

        html.append(".contacts-card{");
        html.append("background:white;");
        html.append("padding:30px;");
        html.append("border-radius:20px;");
        html.append("box-shadow:0 8px 25px rgba(0,0,0,0.07);");
        html.append("overflow-x:auto;");
        html.append("}");

        html.append(".contacts-card h2{");
        html.append("color:#6d3b8f;");
        html.append("margin-bottom:20px;");
        html.append("}");

        html.append("table{");
        html.append("width:100%;");
        html.append("border-collapse:collapse;");
        html.append("}");

        html.append("th{");
        html.append("background:#f3eaf5;");
        html.append("color:#6d3b8f;");
        html.append("padding:14px;");
        html.append("text-align:left;");
        html.append("}");

        html.append("td{");
        html.append("padding:14px;");
        html.append("border-bottom:1px solid #eee;");
        html.append("}");

        html.append(".empty{");
        html.append("color:#777;");
        html.append("padding:15px 0;");
        html.append("}");

        html.append(".back{");
        html.append("display:block;");
        html.append("text-align:center;");
        html.append("margin-top:25px;");
        html.append("color:#7b315f;");
        html.append("text-decoration:none;");
        html.append("font-weight:bold;");
        html.append("}");

        html.append("</style>");
        html.append("</head>");

        html.append("<body>");

        html.append("<div class='container'>");

        html.append("<div class='header'>");
        html.append("<div class='header-icon'>👥</div>");
        html.append("<h1>Emergency Contacts</h1>");
        html.append("<p>Add people you trust to contact during an emergency.</p>");
        html.append("</div>");


        html.append("<div class='form-card'>");

        html.append("<h2>➕ Add Emergency Contact</h2>");

        html.append("<form method='post' action='contacts'>");

        html.append("<label>Contact Name</label>");
        html.append("<input name='contact_name' placeholder='Enter contact name' required>");

        html.append("<label>Phone Number</label>");
        html.append("<input name='phone' placeholder='Enter phone number' required>");

        html.append("<label>Relationship</label>");
        html.append("<input name='relationship' placeholder='Example: Mother, Father, Friend'>");

        html.append("<button type='submit'>Add Contact</button>");

        html.append("</form>");

        html.append("</div>");


        html.append("<div class='contacts-card'>");

        html.append("<h2>📋 Saved Contacts</h2>");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT contact_name, phone, relationship " +
                     "FROM emergency_contacts WHERE user_id = ?")) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            html.append("<table>");
            html.append("<tr>");
            html.append("<th>Name</th>");
            html.append("<th>Phone</th>");
            html.append("<th>Relationship</th>");
            html.append("</tr>");

            boolean hasContacts = false;

            while (rs.next()) {

                hasContacts = true;

                html.append("<tr>");
                html.append("<td>").append(rs.getString("contact_name")).append("</td>");
                html.append("<td>").append(rs.getString("phone")).append("</td>");
                html.append("<td>").append(rs.getString("relationship")).append("</td>");
                html.append("</tr>");
            }

            html.append("</table>");

            if (!hasContacts) {
                html.append("<p class='empty'>No emergency contacts added yet.</p>");
            }

        } catch (Exception e) {

            html.append("<p class='empty'>Error loading contacts.</p>");
            e.printStackTrace();
        }

        html.append("</div>");

        html.append("<a class='back' href='dashboard.html'>← Back to Dashboard</a>");

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        response.getWriter().println(html.toString());
    }
}






