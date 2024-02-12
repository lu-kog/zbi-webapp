package com.authentication;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import utils.CommonLogger;
import utils.DB;
import utils.JSON;

/**
 * Servlet implementation class SessionAuthorization
 */
public class SessionAuthorization extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionAuthorization() {
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
		
		Logger logger = new CommonLogger(SessionAuthorization.class).getLogger();
		
		Cookie[] cookies = request.getCookies();
        String sessionID = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionID")) {
                     sessionID = cookie.getValue();
                     break;
                }
            }
        }
        
        try {
        	if (validateSession(sessionID)) {
            	JSONObject respObject = new JSONObject();
    	        respObject.put("statuscode", 200);
    	        logger.info("Response Sent! 200 :"+ sessionID );
    	        response.getWriter().write(respObject.toString());
            	
    		}else {
    			JSONObject errJson = JSON.CreateErrorJson(400, "Invalid Session!");
    			response.getWriter().write(errJson.toString());
    		}
		} catch (Exception e) {
			logger.error("JSON or sql exception session autherize: "+e);
			JSONObject errJson = JSON.CreateErrorJson(400, "Something went wrong!");
			response.getWriter().write(errJson.toString());
		}
        
        
        
	}

	private boolean validateSession(String sessionID) {
		
		return DB.checkValueisExist(sessionID, "Session", "sessionID");
	}

}
