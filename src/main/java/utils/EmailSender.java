package utils;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender extends Thread{

	String receiverMail;
	String subject;
	String bodyMessage;
	
	public EmailSender(String receiverMail, String subject, String bodyMessage){

		this.receiverMail = receiverMail;
		this.subject = subject;
		this.bodyMessage = bodyMessage;
		
		
        System.out.println("vaada..");
        
	}
	
	@Override
	public void run() {
		try{
			

		    Properties properties = new Properties();
		    properties.put("mail.smtp.starttls.enable", "true");
		    properties.put("mail.smtp.ssl.enable", "true");
		    properties.put("mail.smtp.auth", "true");
		    properties.put("mail.smtp.host", "smtp.gmail.com");
		    properties.put("mail.smtp.port", "465");

		    final String username="noreply.zbi@gmail.com";
		    final String password="gslcykmkipgazxau";
		    
		    Session session = Session.getInstance(properties, new Authenticator() {
		        @Override
		        protected PasswordAuthentication getPasswordAuthentication() {
		            return new PasswordAuthentication(username, password);
		        }
		    });

		    session.setDebug(true);
		    
		    Message message = new MimeMessage(session);
		    message.setFrom(new InternetAddress(username));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverMail));
	        message.setSubject(subject);
	        message.setText(bodyMessage);
		    
			System.out.println("trying");
	        Transport.send(message);
	        System.out.println("mail sent");
	    }
	    catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
	
	public static void main(String[] args) {
		try {
			EmailSender sender = new EmailSender("krishnagokul810@gmail.com", "Testing", "Hello!");
			sender.start();
			sender.join();
		} catch (Exception e) {
			System.out.println("error");
		}
	}
}

