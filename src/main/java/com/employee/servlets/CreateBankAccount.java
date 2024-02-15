package com.employee.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import utils.CommonLogger;
import utils.DB;
import utils.Generator;
import utils.JSON;
import utils.Query;
import utils.SMS;

import com.accounts.Account;
import com.customer.CustomerDAO;
import com.employee.EmployeeDAO;

import javax.servlet.annotation.WebInitParam;

/**
 * Servlet implementation class CreateBankAccount
 */
@WebServlet(value = "/CreateBankAccount", initParams = { 
		@WebInitParam(name = "IFSC", value = "ZSB11"),
		@WebInitParam(name = "initBal", value = "101"),
		@WebInitParam(name = "AcStatus", value = "Active")})
public class CreateBankAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateBankAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Logger logger = new CommonLogger(CreateBankAccount.class).getLogger();
		
		// get cusID, email, phone and A/c type from Customers  from query
		
		
		String empID = request.getParameter("empID");
		String applicationID = request.getParameter("application_id");
		String cusID, email, phone, accType;
		
		Connection conn = DB.getConnection();
        
        try {
			
        	PreparedStatement stmt = conn.prepareStatement(Query.fetchCustomerDetailsByRefID);
			stmt.setString(1, applicationID);
        	
			ResultSet results = stmt.executeQuery();
			if (results.next()) {
				cusID = results.getString("cusID");
				email = results.getString("email");
				phone = results.getString("phone");
				accType = results.getString("account_type");
				String customerName = (results.getString("firstName")+results.getString("lastName"));
				String account_number = Generator.ValidNumerical(15, "Accounts", "AccountID");
				
				logger.info("Account details generated:"+applicationID+" cusID: "+cusID+ " Ac:"+account_number+ " email:"+email+" phone:"+phone+" accType:"+accType);
				
				Account newAccount = new Account(account_number, getInitParameter("IFSC"), accType, 101, LocalDate.now(), true);
				
				DB.createNewAccount(cusID, newAccount);
				
				if (DB.checkValueisExist(account_number, "Accounts", "AccountID")) {
					logger.info("New Account created! - "+account_number);
					
					// create strong password and send to his mail!
					String newPasswd = Generator.generateStrongPassword(8);
					CustomerDAO.getCustomerDAO().updatePasswd(cusID, newPasswd);
					// send sms or mail
					SMS.send(phone, SMS.newAccountApproved(customerName, account_number, accType, 101.0, newAccount.getOpenedDate(), cusID, newPasswd));
					
					EmployeeDAO.getEmpDAO().approveApplication(empID, applicationID);
					System.out.println("Everything works good!");
					
					JSONObject resp = JSON.CreateErrorJson(200, "Account created successfully!");
					response.getWriter().write(resp.toString());
				}
				
			}else {
				throw new Exception("Applicaiton details not found!");
			}
		}catch (Exception e) {
			logger.error(e.getMessage()+" applicationID:" + applicationID);
			JSONObject errJson = JSON.CreateErrorJson(400, e.getMessage());
			response.getWriter().write(errJson.toString());
		}
		
	}

}
