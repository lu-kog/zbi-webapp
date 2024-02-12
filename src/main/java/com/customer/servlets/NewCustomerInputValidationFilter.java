package com.customer.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;
import org.json.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import utils.CommonLogger;
import utils.DB;
import utils.JSON;


/**
 * Servlet Filter implementation class NewCustomerInputValidationFilter
 */
public class NewCustomerInputValidationFilter extends HttpFilter implements Filter {
       
    private static final long serialVersionUID = 1L;

	/**
     * @see HttpFilter#HttpFilter()
     */
    public NewCustomerInputValidationFilter() {
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

		Logger logger = new CommonLogger(NewCustomerInputValidationFilter.class).getLogger();
		
		String jsonString = "";
		BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
        	jsonString+=(line);
        }
        
        logger.info("JSON string: "+jsonString);
        JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
			int accType = jsonObject.getInt("account_type");


	        // Validate JSON input
			
			boolean mobileCheck = Pattern.matches("\\d{10}", jsonObject.getString("mobile_no"));
			boolean dobCheck = Pattern.matches("\\d{4}-\\d{2}-\\d{2}", jsonObject.getString("date_of_birth"));
	        boolean emailCheck = Pattern.matches(".+@.+\\.com", jsonObject.getString("email_address"));
	        boolean accTypeCheck = ((accType==1) || (accType==2));
			boolean fNameCheck = ((jsonObject.getString("first_name") != null) && (jsonObject.getString("first_name") != ""));
			boolean lNameCheck = ((jsonObject.getString("last_name") != null) && (jsonObject.getString("last_name") != ""));
			boolean genderCheck = jsonObject.getString("gender") != null;
			boolean addressCheck = ((jsonObject.getString("home_address") != null) && (jsonObject.getString("home_address") != ""));
			
			logger.info("All patter Tests: "+mobileCheck +" "+ dobCheck +" "+emailCheck +" "+ accTypeCheck +" "+ fNameCheck +" "+ lNameCheck +" "+ genderCheck +" "+ addressCheck);
			
	        	if (mobileCheck && dobCheck && emailCheck && accTypeCheck && fNameCheck && lNameCheck && genderCheck && addressCheck){
	        		logger.info("all Patterns Matched!");
	        		request.setAttribute("jsonData", jsonString);
	        		// Continue with the filter chain
	                chain.doFilter(request, response); 
	            }
	        	
	        	else {
	        		logger.error("Pattern Failed! JSON:"+jsonString);
	        		JSONObject jsonResponse = JSON.CreateErrorJson(400, "Invalid Inputs!");
	        		response.getWriter().write(jsonResponse.toString());
		            
	        	}
		} catch (JSONException e) {
    		logger.error("JSON Exception! JSON:"+jsonString);

    		JSONObject jsonResponse = JSON.CreateErrorJson(400, "Invalid Inputs!");
    		response.getWriter().write(jsonResponse.toString());

		}


	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	
	

}
