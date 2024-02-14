package com.customer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mindrot.jbcrypt.BCrypt;

import utils.CommonLogger;
import utils.DB;
import utils.Query;
import utils.sqlFile;

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
            	PreparedStatement statement = conn.prepareStatement(Query.insertNewCustomerToUser);
            	statement.setString(1, customer.getCusID());
            	
            	logger.info("new User:"+statement);
            	int rowsAffected = statement.executeUpdate();
            	sqlFile.append(statement.toString());
            	
            	statement = conn.prepareStatement(Query.insertNewCustomer);
            	
            	statement.setString(1, customer.getCusID());
            	statement.setString(2, customer.getFirstName());
                statement.setString(3, customer.getLastName());
                statement.setDate(4, Date.valueOf(customer.getDOB()));
                statement.setString(5, customer.getEmail());
                statement.setString(6, customer.getPhone());
                statement.setString(7, customer.getAddress());
                
                logger.info("New Customer: "+statement);
                
                rowsAffected += statement.executeUpdate();
                if (rowsAffected < 2) {
                    logger.error("No customer found with cusID: " + customer.getCusID());
                } else {
                	sqlFile.append(statement.toString());
                    logger.info("Customer updated successfully."+customer.getFirstName());
                }
            }catch (SQLException e) {
                logger.error("SQL error on update customer"+customer.getCusID()+" - "+e);
                throw new Exception("Can't add your application. Please contact admin.");
            }

        }

	
	
	public boolean updatePasswd(String userId, String newPassword) throws Exception {
	    
		Connection conn = DB.getConnection();

	    String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

	    
        try{
        	PreparedStatement statement = conn.prepareStatement(Query.setPasswordToUser);
        	statement.setString(1, hashedPassword);
        	statement.setString(2, userId);
        	
        	logger.info("Changing passwd for User:"+userId+" passwd: "+hashedPassword);
        	int rowsAffected = statement.executeUpdate();
        	if (rowsAffected<1) {
				return false;
			}else {
				sqlFile.append(statement.toString());
				return true;
			}
        }catch (Exception e) {
			throw new Exception("Something went wrong! please contact admin.");
		}
		

	}
	
//	public static void main(String[] args) {
//		try {
//			CustomerDAO.getCustomerDAO().updatePasswd("22511651", "Chithapu");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}
