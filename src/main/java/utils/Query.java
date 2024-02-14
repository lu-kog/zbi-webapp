package utils;

public class Query {

	public static final String getHashedPasswd = "select passwd from `Users` where `userID` like ? ;";
	
	public static final String findRoleOfUser = "SELECT role FROM Users WHERE userID like ?;";

	public static final String insertNewCustomerToUser = "insert into Users (userID, role) values (?, 'Customer');";

	public static final String insertNewCustomer = "INSERT INTO Customers (cusID, firstName, lastName, DOB, email, phone, address) values (?, ?, ?, ?, ?, ?, ?);";
	
	public static final String setPasswordToUser = "UPDATE Users SET passwd = ? WHERE userID like ?";

	public static final String selectAccountStatusFromPhone = "select status from `Accounts` join `Customers` on `Accounts`.`cusID` = `Customers`.`cusID` where `Customers`.`cusID` like (select `Customers`.`cusID` from `Customers` where phone like ?);";

	public static final String getApplicationStatusByPhone = "SELECT A.status FROM Bank_Account_Application AS BAA join `Applications` as A on BAA.application_id = A.application_id JOIN Customers AS C ON BAA.cusID = C.cusID WHERE C.phone LIKE ? ORDER BY BAA.application_date DESC limit 1;";

	public static final String getApplicationDateByPhone =  "SELECT BAA.application_date, A.status FROM Bank_Account_Application AS BAA join `Applications` as A on BAA.application_id = A.application_id JOIN Customers AS C ON BAA.cusID = C.cusID WHERE C.phone LIKE ? ORDER BY BAA.application_date DESC limit 1;";

	public static final String insertNewApplication = "insert into Applications (application_id, Application_type) values (?, 1);";
	
	public static final String insertNewBankApplication = "insert into Bank_Account_Application (application_id, cusID, application_date, account_type) values (?, ?, ?, ?);";

	public static final String approveApplication = "update `Applications` set status = 'approved', `EmpID` = ? where application_id like ? ;";

	public static final String selectStatusOfApplicationByID = "select status from Applications where application_id like ? ;";
	
	public static final String fetchCustomerDetailsByRefID = "select b.cusID, email, phone, account_type from Customers join Bank_Account_Application as b on Customers.cusID = b.cusID  where b.application_id like ?;";

	public static final String fetchPendingApplications = "SELECT a.application_id, b.cusID, c.`firstName`, c.`lastName`, a.Application_type, b.application_date FROM `Applications` a INNER JOIN `Bank_Account_Application` b ON a.application_id = b.application_id join `Customers` c on b.`cusID`= c.`cusID` WHERE a.status = 'pending';";

	public static final String rejectAnApplication = "update `Applications` set status = 'rejected', `EmpID` = ?, comments = ? where application_id like ? ;";
	
	public static final String applicationStatusByRefID =  "select status from Applications where application_id like ? ;";

	public static final String createNewSession = "INSERT INTO Session (sessionID, userID, loggedTime) VALUES (?, ?, ?);";

	public static final String deleteSession = "DELETE FROM Session WHERE userID like ?;";

	public static final String createNewAccount = "INSERT INTO Accounts (AccountID, cusID, IFSC, AccType, balance, dateOpened, status) VALUES (?, ?, ?, ?, ?, ?, ?);";
	
	public static final String fetchApplicationDetails = "select application_id, application_date, application_type, status, comments from `Applications` where application_id like ?;";
}
