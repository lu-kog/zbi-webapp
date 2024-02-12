package com.customer.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.*;

import com.customer.CustomerDAO;
import com.customer.Customers;
import com.employee.EmployeeDAO;

import utils.CommonLogger;
import utils.Generator;
import utils.JSON;

/**
 * Servlet implementation class ApplyForAccount
 */
public class ApplyForAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplyForAccount() {
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
		
		Logger logger = new CommonLogger(ApplyForAccount.class).getLogger();
			
		String jsonString = (String) request.getAttribute("jsonData");


        JSONObject json;
		try {
			json = new JSONObject(jsonString);
			logger.info("JSON parsed! JSON Data:" + json);
	        int accountType = json.getInt("account_type");
	        String firstName = json.getString("first_name");
	        String lastName = json.getString("last_name");
	        String homeAddress = json.getString("home_address");
	        String dateOfBirth = json.getString("date_of_birth");
	        String email = json.getString("email_address");
	        String mobileNo = json.getString("mobile_no");
	        String cusID = Generator.ValidNumerical(8, "Customers", "cusID");
	        
	        // create new Customer with given data!
	        Customers newCustomer = new Customers(cusID, firstName, lastName, dateOfBirth, email, mobileNo, homeAddress);
			
	        // generate new reference ID for application
	        String refID = Generator.ValidNumerical(8, "Bank_Account_Application", "application_id");
	        
	        logger.info("new Ref ID generated! :"+refID);
	        
	        // update new customer in db
	        CustomerDAO.getCustomerDAO().updateCustomer(newCustomer);
	        
	        // update his application in db
	        EmployeeDAO.getEmpDAO().addApplication(newCustomer, refID, accountType);
	        
	        JSONObject respObject = new JSONObject();
	        respObject.put("statuscode", 200);
	        respObject.put("data", new JSONObject().put("refID", refID));
	        logger.info("Response Sent! 200");
	        response.getWriter().write(respObject.toString());

		} catch (Exception e) {
			logger.error("exception on applying "+e);
			JSONObject errObject = JSON.CreateErrorJson(400, e.getMessage());
	        response.getWriter().write(errObject.toString());

		}

	}

}
