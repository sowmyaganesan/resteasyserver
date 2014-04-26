package com.sjsu.restservices;

import java.io.IOException;

import java.net.UnknownHostException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.sjsu.model.User;
import com.sjsu.mongodb.MongoDBClient;

@Path("/restservice")
public class RestService {

	@POST
	@Path("/adduser")
	@Consumes("application/json")
	public Response addUser(User user)
	{
		String message = null;
		
		try {
			MongoDBClient mongoClient = new MongoDBClient();
			message = mongoClient.addUser(user);
		} catch (UnknownHostException e) {
			return Response.status(400).entity(message).build();
		} catch (IOException e) {
			return Response.status(400).entity(message).build();
		}
		
		//200 denotes it is success
		return Response.status(200).entity(message).build();
		//String output = user.toString();
		 //return Response.status(200).entity(output).build();

	}//end of addUser()
	
	
	@POST
	@Path("/edituser")
	@Consumes("application/json")
	public Response editUser(User user)
	{
		String message = null;
		try {
			MongoDBClient mongoClient = new MongoDBClient();
			message = mongoClient.updateUser(user);
		} catch (UnknownHostException e) {
			return Response.status(400).entity(message).build();
		} catch (IOException e) {
			return Response.status(400).entity(message).build();
		}
		
		//200 denotes it is success
		return Response.status(200).entity(message).build();

	}//end of editUser()
	
	
	@POST
	@Path("/deleteuser")
	@Consumes("application/json")
	public Response deleteUser(User user)
	{
		String message = null;
		try {
			MongoDBClient mongoClient = new MongoDBClient();
			message = mongoClient.deleteUser(user);
		} catch (UnknownHostException e) {
			return Response.status(400).entity(message).build();
		} catch (IOException e) {
			return Response.status(400).entity(message).build();
		}
		
		//200 denotes it is success
		return Response.status(200).entity(message).build();
		

	}//end of deleteUser()
	
	
	
	
	@GET
	@Path("/test")
	public Response test()
	{
		String result = "<h1>RESTful Demo Application</h1> !!";
		return Response.status(200).entity(result).build();
	}
	
	
	
	
	
	//sample function for production json
	@GET
	@Path("/print")
	@Produces("application/json")
	public User produceJSON()
	{
		User user = new User();
		user.setName("jsonName");
		user.setZip(95051);
		
		return user;
	}
	
	
	
	
	
}
