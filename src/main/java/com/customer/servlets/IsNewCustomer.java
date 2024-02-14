package com.customer.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import utils.CommonLogger;
import utils.DB;
import utils.JSON;
import utils.Query;


/**
 * Servlet Filter implementation class IsNewCustomer
 */
public class IsNewCustomer extends HttpFilter implements Filter {
	private static final long serialVersionUID = 1L;
	static Logger logger = new CommonLogger(IsNewCustomer.class).getLogger();
	
    /**
     * @see HttpFilter#HttpFilter()
     */
    public IsNewCustomer() {
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
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		
		logger.info("NewCustomerCheck-Filter2 : started");
		
		String jsonString = (String) request.getAttribute("jsonData");
        
        JSONObject jsonObject = new JSONObject();
		JSONObject jsonResponse = new JSONObject();

		try {
			jsonObject = new JSONObject(jsonString);
			
			String mobNum = jsonObject.getString("mobile_no");
			
			if (DB.checkValueisExist(mobNum, "Customers", "phone")) {
				logger.info("Mobile number already exists!");
				
				// if he has active account throw a mesage
				if (hasActiveAcnt(mobNum)) {
					logger.info("Existing Active Account : "+true);
					jsonResponse = JSON.CreateErrorJson(400, "You have an Active account on this mobile number!");
				}
				// if he has pending application throw a message
				else if (hasPendingApplication(mobNum)) {
					logger.info("Application is pending: "+true);
					jsonResponse = JSON.CreateErrorJson(400, "You have a Pending Application on this mobile number!");
				}
				// if his prev application is rejected before 24hrs, redirect to renew account and give new refID
				else if (hasRejectedApplication(mobNum)) {
					jsonResponse.put("statuscode", 200);
					
			        // Redirect to the renewal servlet
					RequestDispatcher rd = request.getRequestDispatcher("/RenewApplication");
			        rd.forward(request, response);
				}
				
				
				response.getWriter().write(jsonResponse.toString());
				
			}else {
				// pass the request along the filter chain
				logger.info("NewCustomerCheck-Filter2 : passed");
				chain.doFilter(request, response);
			}
			
		}catch (Exception e) {
			logger.error("Exception : "+ e);
			jsonResponse = JSON.CreateErrorJson(400, e.getMessage());
			response.getWriter().write(jsonResponse.toString());
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	
	private static boolean hasActiveAcnt(String mobileNumber) throws SQLException {
		// write a join querry to Customers and Accounts table to check his account is in active.
		
		Connection conn = DB.getConnection();
		
		try (PreparedStatement statement = conn.prepareStatement(Query.selectAccountStatusFromPhone)) {
            statement.setString(1, mobileNumber);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
				if (rs.getString(1).equals("Active")) {
					return true;
				}
			}
		} catch (SQLException e) {
            logger.error("SQl exception : hasActiveAcnt!"+e);
            throw new SQLException("Something went wrong! please contact admin.");
        }
		
		return false;
	}
	
	
	private static boolean hasPendingApplication(String mobileNumber) throws SQLException{

		Connection conn = DB.getConnection();
		
		try (PreparedStatement statement = conn.prepareStatement(Query.getApplicationStatusByPhone)) {
            statement.setString(1, mobileNumber);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
            	logger.info("hasPendingApplication: "+rs.getString(1));
				if (rs.getString(1).equals("pending")) {
					return true;
				}
			}
		} catch (SQLException e) {
            logger.error("SQl exception : hasPendingApplication!"+e);
            throw new SQLException("Something went wrong! please contact admin.");
        }
		
		return false;
	}
	
	
	private static boolean hasRejectedApplication(String mobileNumber) throws Exception{

		Connection conn = DB.getConnection();
		
		try (PreparedStatement statement = conn.prepareStatement(Query.getApplicationDateByPhone)) {
            statement.setString(1, mobileNumber);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
            	logger.info("hasRejectedApplication: passed");
            	
            	LocalDate applicationDate = rs.getDate(1).toLocalDate();
            	String appliStatus = rs.getString(2);
            	LocalDate today = LocalDate.now().minusDays(1);
            	
				if (appliStatus.equals("rejected")) {
					if (applicationDate.isBefore(today)) {						
						return true;
					}else {
						throw new Exception("Sorry!, Your application was rejected within a day. Please try after 24 hrs!");
					}
				}else {
					throw new Exception("Sorry!, There is a problem on your mobile number. Try with different number!");
				}
			}
		} catch (SQLException e) {
            logger.error("SQl exception : hasPendingApplication!"+e);
            throw new SQLException("Something went wrong! please contact admin.");
        }
		
		return false;
	}
	
}
