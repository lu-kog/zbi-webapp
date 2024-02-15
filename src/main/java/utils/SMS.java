package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;

import org.apache.log4j.Logger;

public class SMS {
	static Logger logger = new CommonLogger(SMS.class).getLogger();
	
    
	public static void send(String mobileNum, String message) throws Exception {
		try {
			
	        String scriptPath = "/home/gokul-zstk330/Scripts/sendSMS.sh";
	        
	        ProcessBuilder processBuilder = new ProcessBuilder(scriptPath, mobileNum, message);
	        Process process = processBuilder.start();

	    } catch (IOException e) {
	    	logger.error("Can't send message: "+message+" To:"+mobileNum);
	        throw new Exception("Can't send SMS!");
	    }
	}
	
	public static void main(String[] args) throws Exception {
		send("8610983293", "Hello, This is a system generated message! thankyou..");
	}
	
	
	// templates
	
	public static String newAccountApproved(String customer_name, String account_number, String accountType, Double initDeposit, LocalDate OpeningDate, String cusID, String password) {
		String template = "Dear "+customer_name+",\n"
				+ "\n"
				+ "We are happy to inform you that your request for a new account with ZBI Bank has been successfully approved!\n"
				+ "\n"
				+ "An account has been created for you, and you can now enjoy the banking services offered by ZBI Bank.\n"
				+ "\n"
				+ "Your account details are as follows:\n"
				+ "- Account Number: "+account_number+"\n"
				+ "- Account Type: "+accountType+"\n"
				+ "- Initial Deposit: "+initDeposit+"\n"
				+ "- Account Opening Date: "+OpeningDate+"\n"
				
				+ "\n"
				+ "You can now access your account through our ZBI  website. We are committed to providing you with good banking services and look forward to serving you.\n"
				+ "\n"
				+ "- User ID:"+cusID
				+ "- Password:"+password
				+ "\n"
				+ "If you have any questions or need further assistance, please don't hesitate to contact our customer support team at zbi.helpinghand@gmail.com \n"
				+ "\n"
				+ "Thank you for choosing ZBI Bank. We appreciate your trust and loyalty.\n"
				+ "\n"
				+ "Best regards,\n"
				+ "ZBI\n";
		
		return template;
	}

}
