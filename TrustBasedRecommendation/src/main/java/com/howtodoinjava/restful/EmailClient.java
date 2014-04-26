package com.howtodoinjava.restful;

import com.sjsu.mongodb.EmailService;
import com.sjsu.pojo.Attachment;
import com.sjsu.pojo.Email;
import com.sjsu.pojo.EmailConfiguration;

public class EmailClient
{
 public static void main(String[] args)
 {
  EmailConfiguration configuration = new EmailConfiguration();
  configuration.setProperty(EmailConfiguration.SMTP_HOST, "smtp.gmail.com");
  configuration.setProperty(EmailConfiguration.SMTP_AUTH, "true");
  configuration.setProperty(EmailConfiguration.SMTP_TLS_ENABLE, "true");
  configuration.setProperty(EmailConfiguration.SMTP_PORT, "587");
  configuration.setProperty(EmailConfiguration.SMTP_AUTH_USER, "instarecommendation@gmail.com");
  configuration.setProperty(EmailConfiguration.SMTP_AUTH_PWD, "Reco@2014");
  EmailService emailService = new EmailService(configuration);
  Email email = new Email();
  email.setFrom("instarecommendation@gmail.com");
  String[] to = {"sowmister@gmail.com", "sowmyaganesan_11@yahoo.com"};
  email.setTo(to);
  email.setSubject("Welcome to InstaReco!!");
  String bodytext = "Hi, <h3><strong> Explore new areas on the Web</strong></h3><table><tr><td> We are glad to have you on InstaReco</td></tr>" +
		  			"<tr>To register please click on the below link<td><a href='http://localhost:8080/signup'>Register</a> </td></tr>";
  email.setText(bodytext);
  email.setMimeType("text/html");
  /*Attachment attachment1 = new Attachment("ABCDEFGH".getBytes(), "test1.txt","text/plain");
  email.addAttachment(attachment1);
  Attachment attachment2 = new Attachment("XYZZZZZZ".getBytes(), "test2.txt","text/plain");
  email.addAttachment(attachment2);*/
  emailService.sendEmail(email);
 }
}
