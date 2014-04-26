package com.sjsu.restservices;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.sjsu.model.Bookmark;
import com.sjsu.model.Bookmarkcollection;
import com.sjsu.model.User;
import com.sjsu.model.UserBookmark;
import com.sjsu.model.Useremail;
import com.sjsu.mongodb.Bookmarkrepository;
import com.sjsu.mongodb.MongoDBClient;

@Path("/bookmark")
public class Bookmarkcontroller {
	
	@POST
	@Path("/updatebookmark")
	@Consumes("application/json")
	public Response updatebookmark(Bookmarkcollection bookmarkcollection)
	{
		String Message = null;
		try {
			Bookmarkrepository bookmarkrepository = new Bookmarkrepository();
			Message = bookmarkrepository.updatebookmark(bookmarkcollection);
		} catch (UnknownHostException e) {
			return Response.status(400).entity(Message).build();
		} 
		System.out.println(Message);
		return Response.status(200).entity(Message).build();

	}
	
	@POST
	@Path("/deletebookmark")
	@Consumes("application/json")
	public Response deletebookmark(UserBookmark userbookmark)
	{
		String Message = null;
		try {
			Bookmarkrepository bookmarkrepository = new Bookmarkrepository();
			Message = bookmarkrepository.deletebookmark(userbookmark.getBookmark(),userbookmark.getEmail());
		} catch (UnknownHostException e) {
			return Response.status(400).entity(Message).build();
		} catch (IOException e) {
			System.out.println(e);
			return Response.status(400).entity(Message).build();
		}
		
		//200 denotes it is success
		return Response.status(200).entity(Message).build();
		

	}//end of deleteUser()
	
	
	@GET
	@Path("/getauserbookmark")
	public String getauserbookmark(@QueryParam("email") String email)
	{
		String result = null;
		try {
			Bookmarkrepository bookmarkrepository = new Bookmarkrepository();
			result = bookmarkrepository.getauserbookmark(email);
		} catch (UnknownHostException e) {
			return result;
		} catch (IOException e) {
			System.out.println(e);
			return result;
		}
		
		//200 denotes it is success
		return result;
		

	}
}
