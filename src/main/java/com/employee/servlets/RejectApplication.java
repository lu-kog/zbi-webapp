package com.employee.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
import utils.Query;
import utils.sqlFile;

/**
 * Servlet implementation class RejectApplication
 */
@WebServlet("/RejectApplication")
public class RejectApplication extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RejectApplication() {
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
		
		Logger logger = new CommonLogger(RejectApplication.class).getLogger();
		
		try {

			String applicationId = request.getParameter("application_id");
			String empID = request.getParameter("empID");
			String reason = request.getParameter("reason");
			
			logger.info("Application going to reject: "+applicationId+ "by "+empID+" reason: "+reason);
			
			if (DB.checkValueisExist(applicationId, "Applications", "application_id")) {
				if (isPending(applicationId)) {
			        Connection conn = DB.getConnection();
			        
						PreparedStatement stmt = conn.prepareStatement(Query.rejectAnApplication);
						stmt.setString(1, empID);
						stmt.setString(2, reason);
						stmt.setString(3, applicationId);
						
						
						int affectedRow = stmt.executeUpdate();
						if (affectedRow == 1) {
							sqlFile.append(stmt.toString());
							logger.info(applicationId +" Rejected succesfully!");
							JSONObject respJson = JSON.CreateErrorJson(200, "Updated successfully!");
							response.getWriter().write(respJson.toString());
						}else {
							throw new Exception("Something went wrong!");
						}
						
				}else {
					throw new Exception("Application not pending!");
				}
			}else {
				throw new Exception("Application not found!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			JSONObject errObject = JSON.CreateErrorJson(400, e.getMessage());
			response.getWriter().write(errObject.toString());
		}
		
		
	}
	
	

	private boolean isPending(String applicationId) throws Exception {
		
        Connection conn = DB.getConnection();
        
        try {
			PreparedStatement stmt = conn.prepareStatement(Query.applicationStatusByRefID);
			stmt.setString(1, applicationId);
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				boolean isPending = results.getString("status").equals("pending");
				return isPending;
			}else {
				throw new Exception("Application not found!");
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	

}
