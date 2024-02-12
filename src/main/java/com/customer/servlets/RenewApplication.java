package com.customer.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;

import com.employee.EmployeeDAO;

import utils.CommonLogger;
import utils.DB;
import utils.Generator;
import utils.JSON;

/**
 * Servlet implementation class RenewApplication
 */
public class RenewApplication extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RenewApplication() {
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
		// TODO Auto-generated method stub
		// Don't want to update in customer table!
		// just update application and create new refID!
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		Logger logger = new CommonLogger(RenewApplication.class).getLogger();

		logger.info("Renewal-Filter2 : started");
		
		String jsonString = (String) request.getAttribute("jsonData");

        
        JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
			String mobNum = jsonObject.getString("mobile_no");
	        int accountType = jsonObject.getInt("account_type");

			// generate new reference ID for application
	        String refID = Generator.ValidNumerical(8, "Bank_Account_Application", "application_id");

	        logger.info("new Ref ID generated! :"+refID);
	        
			// update his application in db
	        EmployeeDAO.getEmpDAO().renewApplication(mobNum, refID, accountType);
	        
	        JSONObject respObject = new JSONObject();
	        respObject.put("statuscode", 200);
	        respObject.put("data", new JSONObject().put("refID", refID));
	        logger.info("Response Sent! 200");
	        response.getWriter().write(respObject.toString());

		} catch (Exception e) {
			JSONObject errObj = JSON.CreateErrorJson(400, e.getMessage());
			response.getWriter().write(errObj.toString());
		}
        

        
	}

}
