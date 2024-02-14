package com.authentication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import utils.CommonLogger;
import utils.DB;
import utils.JSON;
import utils.sqlFile;
import utils.Query;
/**
 * Servlet implementation class LoginUser
 */
public class LoginUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
    static Logger logger = new CommonLogger(LoginUser.class).getLogger();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String passwdString = request.getParameter("passwd");
		String role = (String) request.getAttribute("role");
		
		try {
			if (login(username, passwdString)) {
				List<Cookie> cookies = new ArrayList<Cookie>();
				cookies.add(new Cookie("userID", username));
				cookies.add(new Cookie("role", role));
				cookies.add(new Cookie("sessionID", DB.createNewSession(username)));
				cookies.forEach(x-> response.addCookie(x));
				
				JSONObject respObject = new JSONObject();
				
		        respObject.put("statuscode", 200);
		        logger.info("Response Sent! 200 :"+ request.getHeader("Date") );
		        response.getWriter().write(respObject.toString());
			}else {
				JSONObject errJson = JSON.CreateErrorJson(400, "Wrong Password!");
				response.getWriter().write(errJson.toString());
			}
		} catch (Exception e) {
			logger.error(e);
			JSONObject errJson = JSON.CreateErrorJson(400, e.getMessage());
			response.getWriter().write(errJson.toString());
		}

		
	}
	
	
	private boolean login(String username, String passwdString) throws Exception {
		Connection conn = DB.getConnection();

		PreparedStatement statement = null;
		try {
			statement = conn.prepareStatement(Query.getHashedPasswd);
		    statement.setString(1, username);
		    
		    ResultSet resultSet = statement.executeQuery();
		    if (resultSet.next()) {
		        String Hashpasswd = resultSet.getString("passwd");
		        return BCrypt.checkpw(passwdString, Hashpasswd);
		    }
		} catch (Exception e) {
			logger.error("SQL querry exception: "+e+" Querry: "+ statement);
			throw new Exception("Something went wrong! please contact admin");
		}
		
		return false;

	}

}
