package com.customer.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import utils.CommonLogger;
import utils.DB;
import utils.JSON;

/**
 * Servlet implementation class ApplicationStatus
 */
@WebServlet("/ApplicationStatus")
public class ApplicationStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationStatus() {
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
		
		Logger logger = new CommonLogger(ApplicationStatus.class).getLogger();
		
		String refId = request.getParameter("refId");
        if (refId == null || refId.isEmpty()) {
        	logger.error("Ref id is empty:"+refId);
            JSONObject errObject = JSON.CreateErrorJson(400, "Invalid RefID!");
            response.getWriter().write(errObject.toString());
            return;
        }

        try {

            if (!isValid(refId)) {
            	JSONObject errJson = JSON.CreateErrorJson(400, "Application not found!");
                response.getWriter().write(errJson.toString());
                return;
            }

            JSONObject json = DB.fetchApplication(refId); 
            response.getWriter().write(json.toString());
            
        } catch (Exception e) {
            logger.error("Error fetching application details", e);
            JSONObject errObject = JSON.CreateErrorJson(400, e.getMessage());
            response.getWriter().write(errObject.toString());
        }

	}

	private boolean isValid(String refId) {
		return DB.checkValueisExist(refId, "Applications", "application_id");
	}

}
