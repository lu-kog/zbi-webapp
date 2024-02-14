package com.employee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;

import com.customer.Customers;

import utils.CommonLogger;
import utils.DB;
import utils.JSON;
import utils.Query;
import utils.sqlFile;

public class EmployeeDAO {
	private static EmployeeDAO obj = new EmployeeDAO();
	static Logger logger = new CommonLogger(EmployeeDAO.class).getLogger();
	private EmployeeDAO() {
		// 
	}
	
	public static EmployeeDAO getEmpDAO() {
		return obj;
	}

	public void addApplication(Customers customer, String refID, int accountType) throws Exception {
		Connection conn = DB.getConnection();
            
            try {
            	
            	PreparedStatement statement = conn.prepareStatement(Query.insertNewApplication);
            	statement.setString(1, refID);                
            	
            	sqlFile.append(statement.toString());
                int rowsAffected = statement.executeUpdate();
                
                statement = conn.prepareStatement(Query.insertNewBankApplication);
                statement.setString(1, refID);
            	statement.setString(2, customer.getCusID());
                statement.setDate(3, Date.valueOf(LocalDate.now()));
                statement.setInt(4, accountType);

                
                rowsAffected += statement.executeUpdate();
                
                if (rowsAffected < 2) {
                    logger.error("Can't update new Application for cusID:" + customer.getCusID() + "refID: "+refID);
                    throw new Exception("Can't update new Application for your customer ID");
                } else {
                	sqlFile.append(statement.toString());
                    logger.info("New Application updated successfully. refID:"+refID);
                }
            }catch (SQLException e) {
                logger.error("SQL error on update customer "+customer.getCusID()+" - "+e);
                throw new Exception("Something went wrong on your application! Please contact admin.");
            }
	}
	
	
	public boolean approveApplication(String empID, String applicationId) throws Exception {
		
        Connection conn = DB.getConnection();
        
        try {
			PreparedStatement stmt = conn.prepareStatement(Query.approveApplication);
			stmt.setString(1, empID);
			stmt.setString(2, applicationId);
			
			int affectedRow = stmt.executeUpdate();
			if (affectedRow == 1) {
				sqlFile.append(stmt.toString());
				logger.info(applicationId +"Application:"+applicationId+" approved!");
				return true;
			}else {
				throw new Exception("Something went wrong!");
			}
        }catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void renewApplication(String mobNum, String refID, int accountType) throws Exception {

		
		//		String querry = "update Bank_Account_Application set application_id = ?, application_date = ?, status = 1, account_type = ? where cusID = (select Customers.cusID from Customers where Customers.phone like ?);";
//		Connection conn = DB.getConnection();
//        
//		logger.info("Mobile : "+mobNum+" Ref: "+refID+" Accnt:"+accountType+" Date:"+Date.valueOf(LocalDate.now()));
//		
//            try (PreparedStatement statement = conn.prepareStatement(querry)) {
//            	statement.setString(1, refID);
//            	statement.setDate(2, Date.valueOf(LocalDate.now()));
//                statement.setInt(3, accountType);
//                statement.setString(4, mobNum);
//
//                logger.info(statement);
//                
//                int rowsAffected = statement.executeUpdate();
//                if (rowsAffected == 0) {
//                    logger.error("Can't update new Application for refID: "+refID);
//                    throw new Exception("Can't Renew Application for your customer ID!");
//                } else {
//                    logger.info("Application Renewed successfully. refID:"+refID);
//                }
//            }catch (SQLException e) {
//                logger.error("SQL error on update Application - "+e);
//                throw new Exception("Something went wrong on your application! Please contact admin.");
//            }
	}

}
