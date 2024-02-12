package com.customer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import utils.CommonLogger;
import utils.DB;
import com.customer.Customers;

public class CustomerDAO {
	static Logger logger = new CommonLogger(CustomerDAO.class).getLogger();
	private static CustomerDAO obj = new CustomerDAO();
	
	private CustomerDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public static CustomerDAO getCustomerDAO() {
		return obj;
	}
	
	public void updateCustomer(Customers customer) throws Exception {
		Connection conn = DB.getConnection();
            
            try{
            	PreparedStatement statement = conn.prepareStatement("insert into Users (userID, role) values (?, 1);");
            	statement.setString(1, customer.getCusID());
            	
            	int rowsAffected = statement.executeUpdate();
            		
            	statement = conn.prepareStatement("INSERT INTO Customers (cusID, firstName, lastName, DOB, email, phone, address) values (?, ?, ?, ?, ?, ?, ?)");
            	
            	statement.setString(1, customer.getCusID());
            	statement.setString(2, customer.getFirstName());
                statement.setString(3, customer.getLastName());
                statement.setDate(4, Date.valueOf(customer.getDOB()));
                statement.setString(5, customer.getEmail());
                statement.setString(6, customer.getPhone());
                statement.setString(7, customer.getAddress());
                

                rowsAffected += statement.executeUpdate();
                if (rowsAffected < 2) {
                    logger.error("No customer found with cusID: " + customer.getCusID());
                } else {
                    logger.info("Customer updated successfully."+customer.getFirstName());
                }
            }catch (SQLException e) {
                logger.error("SQL error on update customer"+customer.getCusID()+" - "+e);
                throw new Exception("Can't add your application. Please contact admin.");
            }

        }
	
	
	
}
