package com.sjsu.restservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.sjsu.mongodb.MongoDBClient;
import com.sjsu.pojo.Bookmark;
import com.sjsu.pojo.User;

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
	
	@DELETE
	@Path("/deleteuser")
	@Consumes("application/json")
	public Response deleteUser(User user)
	{
		String message = null;
		try {
			MongoDBClient mongoClient = new MongoDBClient();
			message = mongoClient.deleteUser(user.getEmail());
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
	
	
	@GET
	@Path("/getCategories")
	@Produces("application/json")
	public List<String> getCategories()
	{
		List<String>  categoriesList = new ArrayList<String>();
		try {
			MongoDBClient mongoClient = new MongoDBClient();
			
 categoriesList = 	mongoClient.getAllCategories();
			
		
		} catch (Exception e) {
		
		}
		return categoriesList;
	}
	
	
}
