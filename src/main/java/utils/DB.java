package utils;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.accounts.Account;


public class DB {

	private static Connection conn = null;
	static Logger logger = new CommonLogger(DB.class).getLogger();
	
	public static Connection getConnection() {
		
		

		if (conn == null) {
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				logger.error("SQL driver not found!");
			}
			try {
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ZBI", "root" , "");
				logger.info("DB connection created!");
			} catch (SQLException e) {
				logger.error("Error on sql: ");
			}
	      	
		}
		
		return conn;
    } 
	
	
	public static boolean checkValueisExist(String value, String table_name, String column) {
		Connection conn = DB.getConnection();
		PreparedStatement statement = null;
		try {
			statement = conn.prepareStatement("SELECT "+column+" FROM "+table_name+" WHERE "+column+" like ?");
            statement.setString(1, value);
            logger.info(statement.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            
        } catch (SQLException e) {
        	
			logger.error("SQL querry exception: "+e+" Querry: "+ statement);
		}
		
		return false;
	}
	
	
	public static String createNewSession(String userID) throws Exception {
		String sessionInsrtQry = "INSERT INTO Session (sessionID, userID, loggedTime) VALUES (?, ?, ?);";
		
		Connection conn = DB.getConnection();
        
        try (PreparedStatement statement = conn.prepareStatement(sessionInsrtQry)) {
        	
        	String sessionID = Generator.createUUID("Session", "sessionID");
        	Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        	statement.setString(1,sessionID);
        	statement.setString(2, userID);
        	statement.setTimestamp(3, currentTimestamp);

        	deleteSession(userID);
        	
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                logger.error("can't create new session: " + statement.toString());
            } else {
                logger.info("new session created successfully.");
                sqlFile.append(statement.toString());
                return sessionID;
            }
        }catch (SQLException e) {
            logger.error("SQL error on creating session"+e);
            throw new Exception("Can't create session! Please contact admin.");
        }

        return null;
    }
	
	
	
	public static void deleteSession(String userID) throws Exception {
		String sessionDeltQry = "DELETE FROM Session WHERE userID like ?;";
		
		Connection conn = DB.getConnection();
        
        try (PreparedStatement statement = conn.prepareStatement(sessionDeltQry)) {
        	
        	statement.setString(1, userID);

            if(statement.execute()) {
            	sqlFile.append(sessionDeltQry);
                logger.info("session deleted successfully.");
			}
        }catch (SQLException e) {
            logger.error("SQL error on deleting session"+e);
            throw new Exception("session error! Please contact admin.");
        }

    }


	
	public static void createNewAccount(String cusID, Account newAc) throws Exception {
        try {
        	Connection conn = DB.getConnection();
            String newAcntInsrt = "INSERT INTO Accounts (AccountID, cusID, IFSC, AccType, balance, dateOpened, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            	PreparedStatement statement = conn.prepareStatement(newAcntInsrt);
                // Set values for the prepared statement
                statement.setString(1, newAc.getAccountNumber());
                statement.setString(2, cusID);
                statement.setString(3, newAc.getIfsc());
                statement.setString(4, newAc.getAccount_type());
                statement.setDouble(5, newAc.getBalance());
                statement.setDate(6, java.sql.Date.valueOf(newAc.getOpenedDate()));
                statement.setString(7, newAc.isStatus() ? "Active" : "Inactive");

                
                int affRows = statement.executeUpdate();
                
                if (affRows!=1) {
                	logger.error("Can't create Account :"+statement);
					throw new Exception("Can't create Account!");
				}else {
					sqlFile.append(statement.toString());
				}
                
                
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	
	
	
}
