package com.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;

import com.customer.servlets.IsNewCustomer;

import utils.CommonLogger;
import utils.DB;
import utils.JSON;

/**
 * Servlet Filter implementation class LoginInputValidation
 */
public class LoginInputValidation extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;
	static Logger logger = new CommonLogger(LoginInputValidation.class).getLogger();

    /**
     * @see HttpFilter#HttpFilter()
     */
    public LoginInputValidation() {
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
		// place your code here
		HttpServletRequest req = (HttpServletRequest) request;

		
		try {
			
			String userName = request.getParameter("username");
			String passwd =  request.getParameter("passwd");

			logger.info(userName+" "+passwd);
			
			if (authenticateUser(userName, passwd)) {
				// pass the request along the filter chain
				chain.doFilter(request, response);
			}else {
				JSONObject errJson = JSON.CreateErrorJson(400, "Invalid Credentials!");
				response.getWriter().write(errJson.toString());
			}
			
		}catch (Exception e) {
			logger.error(e);
			JSONObject errJson = JSON.CreateErrorJson(400, "Input Error!");
			response.getWriter().write(errJson.toString());
		}

		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	
	
	private boolean authenticateUser(String userID, String passwd) {
        return ((userID != null) && (!userID.isEmpty()) && (passwd != null) && (!passwd.isEmpty()));
    }

	
	public static void main(String[] args) {
		System.out.println("testing");
	}
}
