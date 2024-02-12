package com.employee.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.DB;
import utils.JSON;

/**
 * Servlet implementation class GetPendingApplications
 */
public class GetPendingApplications extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPendingApplications() {
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
		
		JSONArray jsonArray = new JSONArray();
		try {
			Connection conn = DB.getConnection();
			String pendingStatusQuery = "SELECT a.application_id, b.cusID, c.`firstName`, c.`lastName`, a.Application_type, b.application_date FROM `Applications` a INNER JOIN `Bank_Account_Application` b ON a.application_id = b.application_id join `Customers` c on b.`cusID`= c.`cusID` WHERE a.status = 'pending';";
			
			PreparedStatement stmt = conn.prepareStatement(pendingStatusQuery);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
                JSONObject newEntry = new JSONObject();
                newEntry.put("application_id", rs.getString("application_id"));
                newEntry.put("cusID", rs.getString("cusID"));
                newEntry.put("customerName", (rs.getString("firstName")+rs.getString("lastName")));
                newEntry.put("type", rs.getInt("Application_type"));
                newEntry.put("date", rs.getDate("application_date"));
                jsonArray.put(newEntry);
            }
			
			JSONObject respObject = new JSONObject();
			respObject.put("statuscode", 200);
			respObject.put("data", jsonArray);
			
			response.getWriter().write(respObject.toString());
		
		}catch (Exception e) {
			JSONObject errJson = JSON.CreateErrorJson(400, "404 Error!");
			response.getWriter().write(errJson.toString());
		}
	}

}
