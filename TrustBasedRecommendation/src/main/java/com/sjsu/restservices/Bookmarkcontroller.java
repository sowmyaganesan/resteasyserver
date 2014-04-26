package com.sjsu.restservices;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.sjsu.mongodb.Bookmarkrepository;
import com.sjsu.pojo.Bookmarkcollection;

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
	
	@DELETE
	@Path("/deletebookmark")
	public Response deletebookmark(@QueryParam("bookmarkid") String bookmarkid, @QueryParam("email") String email)
	{
		String Message = null;
		try {
			Bookmarkrepository bookmarkrepository = new Bookmarkrepository();
			Message = bookmarkrepository.deletebookmark(bookmarkid,email);
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
