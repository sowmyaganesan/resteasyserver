package com.sjsu.restservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.sjsu.mongodb.AmazonEmailService;
import com.sjsu.mongodb.Bookmarkrepository;
import com.sjsu.mongodb.EmailService;
import com.sjsu.mongodb.EmailTracker;
import com.sjsu.mongodb.Userrepository;
import com.sjsu.pojo.Email;
import com.sjsu.pojo.EmailConfiguration;
import com.sjsu.pojo.Inviteemails;
import com.sjsu.pojo.TrustScoreCollection;

@Path("/friends")
public class FriendController {
	
	@POST
	@Path("/invitefriends")
	@Consumes("application/json")
	public Response invitefriends(Inviteemails inviteemails) throws Exception
	{
		String Message = null;
		String invfrd = new Gson().toJson(inviteemails);

		AmazonEmailService amazonEmailService = new AmazonEmailService();
		Message = amazonEmailService.sendEmail(inviteemails);
		  if (Message.contains("Success")){
			  try {
				  if (inviteemails.getEmailaddress().contains(inviteemails.getSenderemail())){
						 List<String> emailaddresses = new ArrayList<String>();
						 emailaddresses = inviteemails.getEmailaddress();
					  Iterator<String> iterator = emailaddresses.iterator();
						while (iterator.hasNext()) {
							Object bitr= iterator.next();
							if (bitr.equals(inviteemails.getSenderemail())){
								iterator.remove();
							}
						}
						inviteemails.setEmailaddress(emailaddresses);
						EmailTracker emailtracker = new EmailTracker();
						Message = emailtracker.updateemailedlist(inviteemails);
				  }
				  else{
					   EmailTracker emailtracker = new EmailTracker();
						Message = emailtracker.updateemailedlist(inviteemails);
				  }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		
		System.out.println(Message);
		return Response.status(200).entity(Message).build();

	}
	
	@POST
	@Path("/addTrustscoretofriend")
	@Consumes("application/json")
	public Response addTrustscoretofriend(TrustScoreCollection trustScoreCollection)
	{
		String Message = null;
		String invfrd = new Gson().toJson(trustScoreCollection);
		System.out.println(invfrd);
		try {
			Userrepository userrepository = new Userrepository();
			Message = userrepository.addTrustscoretofriend(trustScoreCollection);
		} catch (UnknownHostException e) {
			return Response.status(400).entity(Message).build();
		} 
		System.out.println(Message);
		return Response.status(200).entity(Message).build();
	}

	@POST
	@Path("/updateTrustscoretofriend")
	@Consumes("application/json")
	public Response updateTrustscoretofriend(TrustScoreCollection trustScoreCollection)
	{
		String Message = null;
		String invfrd = new Gson().toJson(trustScoreCollection);
		System.out.println(invfrd);
		try { 
			Userrepository userrepository = new Userrepository();
			Message = userrepository.updateTrustscoretofriend(trustScoreCollection);
		} catch (UnknownHostException e) {
			return Response.status(400).entity(Message).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println(Message);
		return Response.status(200).entity(Message).build();
	}

	
	@DELETE
	@Path("/removefriendfromtrustnw")
	public Response removefriendfromtrustnw(@QueryParam("user") String user, @QueryParam("friend") String friend, @QueryParam("category") String category)
	{
		String Message = null;
		
		try {
			Userrepository userrepository = new Userrepository();
			Message = userrepository.removefriendfromtrustnw(user,friend,category);
		} catch (UnknownHostException e) {
			return Response.status(400).entity(Message).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println(Message);
		return Response.status(200).entity(Message).build();
	}

}
