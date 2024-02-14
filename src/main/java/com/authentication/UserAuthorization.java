package com.authentication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import utils.CommonLogger;
import utils.DB;
import utils.JSON;
import utils.Query;

/**
 * Servlet Filter implementation class UserAuthorization
 */
public class UserAuthorization extends HttpFilter implements Filter {
       private static final long serialVersionUID = 1L;
	static Logger logger = new CommonLogger(UserAuthorization.class).getLogger();
    /**
     * @see HttpFilter#HttpFilter()
     */
    public UserAuthorization() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		try {
			String username = request.getParameter("username");
			if (isValidUser(username)) {
				String role = findRole(username);
				request.setAttribute("role", role);
				// pass the request along the filter chain
				chain.doFilter(request, response);
			}else {
				logger.info(username+" not found!");
				JSONObject errJson = JSON.CreateErrorJson(400, "User doesn't exist!");
				response.getWriter().write(errJson.toString());
			}
			
			
		} catch (Exception e) {
			JSONObject errJson = JSON.CreateErrorJson(400, "Can't process given Credentials!");
			response.getWriter().write(errJson.toString());
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	
	boolean isValidUser(String username) {
		return DB.checkValueisExist(username, "Users", "userID");
	}
	
	String findRole(String username) throws Exception {
		
		Connection conn = DB.getConnection();

		PreparedStatement statement = null;
		try {
			statement = conn.prepareStatement(Query.findRoleOfUser);
		    statement.setString(1, username);
		    
		    ResultSet resultSet = statement.executeQuery();
		    if (resultSet.next()) {
		        return resultSet.getString("role");
		    }
		} catch (SQLException e) {
			logger.error("SQL querry exception: "+e+" Querry: "+ statement);
			throw new Exception("Something went wrong!");
		}
		
		return "";

		
	}

}
