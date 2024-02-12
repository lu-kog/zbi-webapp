package com.authentication;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import utils.JSON;

/**
 * Servlet Filter implementation class SessionValidation
 */
public class SessionValidation extends HttpFilter implements Filter {
       
    private static final long serialVersionUID = 1L;


	/**
     * @see HttpFilter#HttpFilter()
     */
    public SessionValidation() {
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
		
			HttpServletRequest req = (HttpServletRequest) request;
		
	        Cookie[] cookies = req.getCookies();
	        String userID = null, role = null, sessionID = null;

	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                switch (cookie.getName()) {
	                    case "userID":
	                        userID = cookie.getValue();
	                        break;
	                    case "role":
	                        role = cookie.getValue();
	                        break;
	                    case "sessionID":
	                        sessionID = cookie.getValue();
	                        break;
	                }
	            }
	        }
	        
	        if (verifyCookies(userID, role, sessionID)) {
	        	// pass the request along the filter chain
	    		chain.doFilter(request, response);
			}else {
				JSONObject errObject = JSON.CreateErrorJson(400, "Invalid session!");
				response.getWriter().write(errObject.toString());
			}

	        
	    }


		
	

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	
	private boolean verifyCookies(String userID, String role, String sessionID) {
		return ((userID!=null) && (!userID.isEmpty()) && (role!=null) && (!role.isEmpty()) && (sessionID!=null) && (!sessionID.isEmpty()));
	}

}
