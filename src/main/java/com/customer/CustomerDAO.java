package com.customer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mindrot.jbcrypt.BCrypt;

import utils.CommonLogger;
import utils.DB;
import utils.Generator;
import utils.Query;
import utils.sqlFile;

import com.accounts.Transactions;
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
	
	public static Customers getCustomerObj(String cusID) throws Exception {
		Connection conn = DB.getConnection();

		try{
        	PreparedStatement statement = conn.prepareStatement(Query.getCustomerByCusID);
        	statement.setString(1, cusID);
        	
        	logger.info("Creating customer object:"+cusID);
        	
        	ResultSet rs = statement.executeQuery();
        	
        	if (rs.next()) {
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				LocalDate DOB = rs.getDate("DOB").toLocalDate();
				String email = rs.getString("email");
				String phone = rs.getString("phone");
				String address = rs.getString("address");
				
				Customers customer = new Customers(cusID, firstName, lastName, DOB.toString(), email, phone, address);
				
				return customer;
			}else {
				logger.error("Can't create customer Object:"+cusID);
				throw new Exception("Something went wrong!");
			}
        }catch (Exception e) {
			throw new Exception("Something went wrong! please contact admin.");
		}
	}
	
	public static void addTransactionEntry(String AccountID, LocalDate trnxDate, String Description, double Cr_amount, double Dr_amount, double Net_Balance) throws Exception {
		String Transaction_id = Generator.ValidNumerical(10, "passbook", "Transaction_id");
		
		
		Connection conn = DB.getConnection();

		try{
        	PreparedStatement statement = conn.prepareStatement(Query.newPassbookEntry);
        	statement.setString(1, AccountID);
        	statement.setString(2, Transaction_id);
        	statement.setDate(3, Date.valueOf(trnxDate));
        	statement.setString(4, Description);
        	statement.setDouble(5, Cr_amount);
        	statement.setDouble(6, Dr_amount);
        	statement.setDouble(7, Net_Balance);
        	
        	logger.info("New transaction entry adding!");
        	int rowsAffected = statement.executeUpdate();
        	
        	if (rowsAffected==1) {
				sqlFile.append(statement.toString());
				logger.info("Entry updated!");
			}else {
				logger.error("Can't add transaction entry:"+AccountID+"Amount :"+Cr_amount+" - "+Dr_amount);
			}
        	
        }catch (Exception e) {
        	logger.error(e.getMessage());
			throw new Exception("Something went wrong! please contact admin.");
		}
		
	}

	public static List<Transactions> fetchTransactions(String accountNumber) throws Exception {
		List<Transactions> allTransactions = new ArrayList<Transactions>();
		
		Connection conn = DB.getConnection();

		try{
        	PreparedStatement statement = conn.prepareStatement(Query.fetchTransactionsByAccID);
        	statement.setString(1, accountNumber);
        	
        	
        	ResultSet rs = statement.executeQuery();
        	while (rs.next()) {
				Transactions trns = new Transactions(rs.getString("Transaction_id"), rs.getDate("Transaction_date").toLocalDate(), rs.getString("Description"), rs.getDouble("Cr_amount"), rs.getDouble("Dr_amount"), rs.getDouble("Net_Balance"));
				
				allTransactions.add(trns);
			}
        	
        	logger.info("Transactions fetched for:"+accountNumber);
        	
        	return allTransactions;
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
