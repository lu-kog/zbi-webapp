package utils;

import java.util.DoubleSummaryStatistics;

public class Query {

	public static final String getHashedPasswd = "select passwd from `Users` where `userID` like ? ;";
	
	public static final String findRoleOfUser = "SELECT role FROM Users WHERE userID like ?;";

	public static final String insertNewCustomerToUser = "insert into Users (userID, role) values (?, 'Customer');";

	public static final String insertNewCustomer = "INSERT INTO Customers (cusID, firstName, lastName, DOB, email, phone, address) values (?, ?, ?, ?, ?, ?, ?);";
	
	public static final String setPasswordToUser = "UPDATE Users SET passwd = ? WHERE userID like ?";

	public static final String selectAccountStatusFromPhone = "select status from `Accounts` join `Customers` on `Accounts`.`cusID` = `Customers`.`cusID` where `Customers`.`cusID` like (select `Customers`.`cusID` from `Customers` where phone like ?);";

	public static final String fetchApplicationByPhone =  "select a.application_id, a.application_type, a.status, a.comments, a.application_date from Applications a join Bank_Account_Application b on a.application_id = b.application_id join Customers on b.cusID = Customers.cusID where phone like ?;";

	public static final String insertNewApplication = "insert into Applications (application_id, application_date, application_type) values (?, ?, ?);";
	
	public static final String insertNewBankApplication = "insert into Bank_Account_Application (application_id, cusID, account_type) values (?, ?, ?);";

	public static final String approveApplication = "update `Applications` set status = 'approved', `EmpID` = ? where application_id like ? ;";
	
	public static final String fetchCustomerDetailsByRefID = "select b.cusID, `firstName`, `lastName`, email, phone, account_type from Customers join Bank_Account_Application as b on Customers.cusID = b.cusID  where b.application_id like ?;";

	public static final String fetchPendingApplications = "SELECT a.application_id, b.cusID, c.`firstName`, c.`lastName`, a.Application_type, a.application_date FROM `Applications` a INNER JOIN `Bank_Account_Application` b ON a.application_id = b.application_id join `Customers` c on b.`cusID`= c.`cusID` WHERE a.status = 'pending';";

	public static final String rejectAnApplication = "update `Applications` set status = 'rejected', `EmpID` = ?, comments = ? where application_id like ? ;";
	
	public static final String applicationStatusByRefID =  "select status from Applications where application_id like ? ;";

	public static final String createNewSession = "INSERT INTO Session (sessionID, userID, loggedTime) VALUES (?, ?, ?);";

	public static final String deleteSession = "DELETE FROM Session WHERE userID like ?;";

	public static final String validateSession = "select sessionID from Session where userID like ?;";
	
	public static final String createNewAccount = "INSERT INTO Accounts (AccountID, cusID, IFSC, AccType, balance, dateOpened, status) VALUES (?, ?, ?, ?, ?, ?, ?);";
	
	public static final String fetchApplicationDetails = "select application_id, application_date, application_type, status, comments from `Applications` where application_id like ?;";

	public static final String newPassbookEntry = "insert into passbook (AccountID, Transaction_id, Transaction_date, Description, Cr_amount, Dr_amount, Net_Balance) values (?, ?, ?, ?, ?, ?, ?);";

	public static final String fetchTransactionsByAccID = "select Transaction_id, Transaction_date, Description, Cr_amount, Dr_amount, Net_Balance from passbook where AccountID like ? LIMIT 100;";

	public static final String getCustomerByCusID = "select * from Customers where cusID like ?;";

	public static final String fetchAccountByCusID = "select * from `Accounts` where `cusID` like ? and `AccountID` like ?;";
	
			
}
