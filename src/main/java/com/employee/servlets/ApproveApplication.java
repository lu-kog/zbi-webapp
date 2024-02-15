package com.employee.servlets;

import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
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

/**
 * Servlet implementation class ApproveApplication
 */
@WebServlet("/ApproveApplication")
public class ApproveApplication extends HttpServlet {
	private static final long serialVersionUID = 1L;
      static Logger logger = new CommonLogger(ApproveApplication.class).getLogger(); 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApproveApplication() {
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
		

		try {
			
			// get application details and forward to respective servlets
			int applicationType = Integer.parseInt(request.getParameter("application_type"));
			String applicationID = request.getParameter("application_id");
			String empID = request.getParameter("empID");
			
			logger.info("Application "+applicationID+" is approving by: "+ empID);
			
			boolean isValid = isPending(applicationID); 
			
			
			if (isValid) {
				switch (applicationType) {
					case 1: {
						forwardToCreateBankAccount(request, response);
					}
					case 2: {
						forwardToDebitCardRequest(request, response);
					}
					case 3: {
						forwardToCreditCardRequest(request, response);
					}
					case 4: {
						forwardToLoanRequest(request, response);
					}
				}
			}
			
		} catch (Exception e) {
			JSONObject errJson = JSON.CreateErrorJson(400, e.getMessage());
			response.getWriter().write(errJson.toString());
		}
		
	}

	
	

	private boolean isPending(String applicationId) throws Exception {
        
        Connection conn = DB.getConnection();
        
        try {
			PreparedStatement stmt = conn.prepareStatement(Query.applicationStatusByRefID);
			stmt.setString(1, applicationId);
			
			logger.info("getting status: "+stmt);
			ResultSet results = stmt.executeQuery();

			
			if (results.next()) {
				boolean isActive = results.getString("status").equals("pending");
				if(isActive) {
					return true;
				}else {
					throw new Exception("Application is not in pending!");
				}
			}else {
				throw new Exception("No application found!");
			}
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	// Forward to create bank account
    private void forwardToCreateBankAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/CreateBankAccount");
        dispatcher.forward(request, response);
    }

    // Forward to debit card request
    private void forwardToDebitCardRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/debitCardRequest");
        dispatcher.forward(request, response);
    }

    // Forward to credit card request
    private void forwardToCreditCardRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/creditCardRequest");
        dispatcher.forward(request, response);
    }

    // Forward to loan request 
    private void forwardToLoanRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/loanRequest");
        dispatcher.forward(request, response);
    }
    
}
