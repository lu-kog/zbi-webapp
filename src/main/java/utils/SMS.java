package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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
	
	string newAccountApproved() {
		String template = "Dear [Customer's Name],\n"
				+ "\n"
				+ "We are happy to inform you that your request for a new account with ZBI Bank has been successfully approved!\n"
				+ "\n"
				+ "An account has been created for you, and you can now enjoy the banking services offered by ZBI Bank.\n"
				+ "\n"
				+ "Your account details are as follows:\n"
				+ "- Account Number: [Account Number]\n"
				+ "- Account Type: [Account Type]\n"
				+ "- Initial Deposit: 101\n"
				+ "- Account Opening Date: [Account Opening Date]\n"
				+ "\n"
				+ "You can now access your account through our ZBI Bank app or website. We are committed to providing you with top-notch banking services and look forward to serving you.\n"
				+ "\n"
				+ "If you have any questions or need further assistance, please don't hesitate to contact our customer support team at [Customer Support Phone Number] or email us at [Customer Support Email Address].\n"
				+ "\n"
				+ "Thank you for choosing ZBI Bank. We appreciate your trust and loyalty.\n"
				+ "\n"
				+ "Best regards,\n"
				+ "[Your Name]\n"
				+ "[Your Position]\n"
				+ "ZBI Bank\n";
	}

}
