package com.employee.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import utils.DB;
import utils.JSON;
import utils.Query;

/**
 * Servlet Filter implementation class ApproveApplication
 */
//@WebFilter("/ApproveApplication")
public class ValidateApplication extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public ValidateApplication() {
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
		
		try {
			
			// get application details and forward to respective servlets
			int applicationType = Integer.parseInt(request.getParameter("type"));
			String applicationID = request.getParameter("application_id");
			boolean isValid = isPending(applicationID); 

			if (isValid) {
				switch (applicationType) {
					case 1: {
						forwardToCreateBankAccount((HttpServletRequest) request, (HttpServletResponse) response);
					}
					case 2: {
						forwardToDebitCardRequest((HttpServletRequest) request, (HttpServletResponse) response);
					}
					case 3: {
						forwardToCreditCardRequest((HttpServletRequest) request, (HttpServletResponse) response);
					}
					case 4: {
						forwardToLoanRequest((HttpServletRequest) request, (HttpServletResponse) response);
					}
				}
			}
			
		} catch (Exception e) {
			JSONObject errJson = JSON.CreateErrorJson(400, e.getMessage());
			response.getWriter().write(errJson.toString());
		}
		
		
	}

	

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	
	private boolean isPending(String applicationId) throws Exception {
        Connection conn = DB.getConnection();
        
        try {
			PreparedStatement stmt = conn.prepareStatement(Query.applicationStatusByRefID);
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
