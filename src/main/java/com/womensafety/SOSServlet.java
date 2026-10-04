package com.womensafety;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/sos")
public class SOSServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("index.html");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

        String sql = "INSERT INTO emergency_alerts " +
                     "(user_id, latitude, longitude, status) " +
                     "VALUES (?, ?, ?, 'ACTIVE')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            if (latitude == null || latitude.isEmpty()) {
                ps.setNull(2, java.sql.Types.DECIMAL);
            } else {
                ps.setBigDecimal(2, new java.math.BigDecimal(latitude));
            }

            if (longitude == null || longitude.isEmpty()) {
                ps.setNull(3, java.sql.Types.DECIMAL);
            } else {
                ps.setBigDecimal(3, new java.math.BigDecimal(longitude));
            }

            ps.executeUpdate();

            response.setContentType("text/html;charset=UTF-8");

            String mapLink = "";

            if (latitude != null && !latitude.isEmpty()
                    && longitude != null && !longitude.isEmpty()) {

                mapLink =
                    "<a href='https://www.google.com/maps?q="
                    + latitude + "," + longitude
                    + "' target='_blank' class='map-button'>"
                    + "📍 View My Location"
                    + "</a>";
            }

            response.getWriter().println(

                "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +

                "<meta charset='UTF-8'>" +

                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>" +

                "<title>SOS Alert Sent - Women Safety System</title>" +

                "<style>" +

                "*{" +
                "box-sizing:border-box;" +
                "margin:0;" +
                "padding:0;" +
                "}" +

                "body{" +
                "font-family:Arial,sans-serif;" +
                "background:linear-gradient(135deg,#f8f0f7,#f1e5f0);" +
                "min-height:100vh;" +
                "display:flex;" +
                "justify-content:center;" +
                "align-items:center;" +
                "padding:30px;" +
                "color:#292333;" +
                "}" +

                ".container{" +
                "width:100%;" +
                "max-width:520px;" +
                "background:white;" +
                "padding:45px 40px;" +
                "border-radius:24px;" +
                "box-shadow:0 12px 35px rgba(0,0,0,0.10);" +
                "text-align:center;" +
                "}" +

                ".icon{" +
                "font-size:65px;" +
                "margin-bottom:15px;" +
                "}" +

                "h1{" +
                "color:#7b315f;" +
                "font-size:30px;" +
                "margin-bottom:12px;" +
                "}" +

                ".success{" +
                "color:#238636;" +
                "font-weight:bold;" +
                "font-size:16px;" +
                "margin-bottom:25px;" +
                "}" +

                ".info{" +
                "background:#f8f0f7;" +
                "border-radius:14px;" +
                "padding:18px;" +
                "margin-bottom:25px;" +
                "text-align:left;" +
                "}" +

                ".info p{" +
                "margin:8px 0;" +
                "color:#555;" +
                "font-size:14px;" +
                "}" +

                ".buttons{" +
                "display:flex;" +
                "flex-direction:column;" +
                "gap:12px;" +
                "}" +

                ".button{" +
                "display:block;" +
                "padding:13px 20px;" +
                "border-radius:10px;" +
                "text-decoration:none;" +
                "font-weight:bold;" +
                "}" +

                ".map-button{" +
                "background:#7b315f;" +
                "color:white;" +
                "}" +

                ".call-button{" +
                "background:#238636;" +
                "color:white;" +
                "}" +

                ".dashboard-button{" +
                "background:#eee;" +
                "color:#555;" +
                "}" +

                ".brand{" +
                "margin-top:25px;" +
                "font-size:13px;" +
                "color:#999;" +
                "}" +

                "</style>" +
                "</head>" +

                "<body>" +

                "<div class='container'>" +

                "<div class='icon'>🚨</div>" +

                "<h1>SOS Alert Sent</h1>" +

                "<p class='success'>✅ Emergency alert recorded successfully</p>" +

                "<div class='info'>" +

                "<p>📍 <strong>Location:</strong> Recorded successfully</p>" +

                "<p>🕐 <strong>Status:</strong> ACTIVE</p>" +

                "<p>🛡️ <strong>System:</strong> Women Safety System</p>" +

                "</div>" +

                "<div class='buttons'>" +

                mapLink +

                "<a href='tel:112' class='button call-button'>" +
                "📞 Call Emergency Number 112" +
                "</a>" +

                "<a href='dashboard.html' class='button dashboard-button'>" +
                "🏠 Back to Dashboard" +
                "</a>" +

                "</div>" +

                "<p class='brand'>Your safety matters. Stay safe. 🛡️</p>" +

                "</div>" +

                "</body>" +
                "</html>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                "<h2>Something went wrong.</h2>" +
                "<p>Unable to record the SOS alert.</p>" +
                "<a href='emergency.html'>Back to Emergency Center</a>"
            );
        }
    }
}

