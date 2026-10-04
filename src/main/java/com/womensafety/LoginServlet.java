package com.womensafety;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String sql = "SELECT user_id, name FROM users WHERE email = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                HttpSession session = request.getSession();
                session.setAttribute("userId", rs.getInt("user_id"));
                session.setAttribute("userName", rs.getString("name"));

                response.sendRedirect("dashboard.html");

            } else {

                response.setContentType("text/html;charset=UTF-8");

                response.getWriter().println(
                    "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "<title>Login Failed - Women Safety System</title>" +

                    "<style>" +
                    "*{box-sizing:border-box;margin:0;padding:0;}" +

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

                    ".error-container{" +
                    "width:100%;" +
                    "max-width:450px;" +
                    "background:white;" +
                    "padding:45px 40px;" +
                    "border-radius:24px;" +
                    "box-shadow:0 12px 35px rgba(0,0,0,0.10);" +
                    "text-align:center;" +
                    "}" +

                    ".icon{" +
                    "font-size:60px;" +
                    "margin-bottom:18px;" +
                    "}" +

                    "h1{" +
                    "color:#7b315f;" +
                    "font-size:28px;" +
                    "margin-bottom:12px;" +
                    "}" +

                    ".message{" +
                    "color:#777;" +
                    "font-size:15px;" +
                    "line-height:1.6;" +
                    "margin-bottom:28px;" +
                    "}" +

                    ".button{" +
                    "display:inline-block;" +
                    "background:#7b315f;" +
                    "color:white;" +
                    "text-decoration:none;" +
                    "padding:13px 28px;" +
                    "border-radius:10px;" +
                    "font-weight:bold;" +
                    "transition:0.2s;" +
                    "}" +

                    ".button:hover{" +
                    "background:#65264d;" +
                    "}" +

                    ".brand{" +
                    "margin-top:25px;" +
                    "font-size:13px;" +
                    "color:#999;" +
                    "}" +

                    "</style>" +
                    "</head>" +

                    "<body>" +

                    "<div class='error-container'>" +

                    "<div class='icon'>⚠️</div>" +

                    "<h1>Login Failed</h1>" +

                    "<p class='message'>" +
                    "The email or password you entered is incorrect.<br>" +
                    "Please check your details and try again." +
                    "</p>" +

                    "<a href='index.html' class='button'>← Back to Login</a>" +

                    "<p class='brand'>🛡️ Women Safety System</p>" +

                    "</div>" +

                    "</body>" +
                    "</html>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                "<h2>Something went wrong.</h2>" +
                "<p>Please try again later.</p>" +
                "<a href='index.html'>Back to Login</a>"
            );
        }
    }
}



