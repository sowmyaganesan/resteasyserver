package com.sjsu.mongodb;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sjsu.pojo.Inviteemails;

public class AmazonEmailService {

    static final String FROM = "instarecommendation@gmail.com";   // Replace with your "From" address. This address must be verified.
    static final String TO = "sowmister@gmail.com";  // Replace with a "To" address. If you have not yet requested
                                                       // production access, this address must be verified.
    
    static final String BODY = "Hi, <h3><strong> Explore new areas on the Web</strong></h3><table><tr><td> We are glad to have you on InstaReco</td></tr>" +
		  			"<tr>To register please click on the below link<td><a href='http://54.187.117.106:8080/signup'>Register</a> </td></tr>";
    static final String SUBJECT = "Welcome to InstaReco!";
    
    // Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
    static final String SMTP_USERNAME = "AKIAIMFMCZUDDEDWSS6A";  // Replace with your SMTP username.
    static final String SMTP_PASSWORD = "ApzEwVq3WL4TgilxwGWl7Tv/0HACwlVcoSzx+RxM1ERY";  // Replace with your SMTP password.
    
    // Amazon SES SMTP host name. This example uses the us-east-1 region.
    static final String HOST = "email-smtp.us-west-2.amazonaws.com";    
    
    // Port we will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
    // STARTTLS to encrypt the connection.
    static final int PORT = 587;

	public String sendEmail(Inviteemails inviteemails) throws Exception{
	       // Create a Properties object to contain connection configuration information.
	    	Properties props = System.getProperties();
	    	props.put("mail.transport.protocol", "smtp");
	    	props.put("mail.smtp.port", "587"); 
	    	
	    	// Set properties indicating that we want to use STARTTLS to encrypt the connection.
	    	// The SMTP session will begin on an unencrypted connection, and then the client
	        // will issue a STARTTLS command to upgrade to an encrypted connection.
	    	props.put("mail.smtp.auth", "true");
	    	props.put("mail.smtp.starttls.enable", "true");
	    	props.put("mail.smtp.starttls.required", "true");

	    	System.out.println(props.getProperty("mail.smtp.port"));
	        // Create a Session object to represent a mail session with the specified properties. 
	    	Session session = Session.getDefaultInstance(props);

	        // Create a message with the specified information. 
	        MimeMessage msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(FROM));
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
	        msg.setSubject(SUBJECT);
	        msg.setContent(BODY,"text/html; charset=utf-8");
	            
	        // Create a transport.        
	        Transport transport = session.getTransport();
	                    
	        // Send the message.
	        try
	        {
	            System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
	            
	            // Connect to Amazon SES using the SMTP username and password you specified above.
	            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
	        	
	            // Send the email.
	            transport.sendMessage(msg, msg.getAllRecipients());
	            System.out.println("Email sent!");
	        }
	        catch (Exception ex) {
	            System.out.println("The email was not sent.");
	            System.out.println("Error message: " + ex.getMessage());
	        }
	        finally
	        {
	            // Close and terminate the connection.
	            transport.close();        	
	        }
        return "{\"Success\": \"Email sent\"}";
	}

}
