package com.accounts;

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
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.authentication.SessionAuthorization;

import utils.CommonLogger;
import utils.DB;
import utils.JSON;
import utils.Query;

/**
 * Servlet Filter implementation class AccountOwnerValidation
 */
@WebFilter("*/Account/*")
public class AccountOwnerValidation extends HttpFilter implements Filter {
       
    private static final long serialVersionUID = 1L;

	/**
     * @see HttpFilter#HttpFilter()
     */
    public AccountOwnerValidation() {
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
		Logger logger = new CommonLogger(SessionAuthorization.class).getLogger();
		String account_number = null;
		String userID = null;
		HttpServletRequest req = (HttpServletRequest) request;
		
		Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("account")) {
                	account_number = cookie.getValue();
                }
                else if (cookie.getName().equals("userID")) {
                	userID = cookie.getValue();
                }
          
            }
        }
        
        
        try {
			if (accountValidate(account_number, userID)) {
				chain.doFilter(request, response);
			}else {
				throw new Exception("Can't process with this session.. Please Re-login!");
			}
		} catch (Exception e) {
			logger.error(e);
			JSONObject errJson = JSON.CreateErrorJson(400, e.getMessage());
			response.getWriter().write(errJson.toString());
		}
	}

	private boolean accountValidate(String account_number, String userID) throws Exception {
		Connection conn = DB.getConnection();
        
        try {
        	PreparedStatement statement = conn.prepareStatement(Query.fetchAccountByCusID);
        	
        	statement.setString(1, userID);
        	statement.setString(2, account_number);

        	ResultSet rs = statement.executeQuery();
            if(rs.next()) {
            	return true;
			}else {
				return false;
			}
        }catch (Exception e) {
        	throw new Exception("Can't connect with this account! please re-login!");
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
