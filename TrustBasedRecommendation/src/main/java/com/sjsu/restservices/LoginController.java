package com.sjsu.restservices;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.sjsu.mongodb.Loginrepository;
import com.sjsu.pojo.User;

@Path("/login")
public class LoginController {

	@POST
	@Path("/signin")
	@Consumes("application/json")
	public Response signin(User user)
	{
		String Message  = null;
		try {
			Loginrepository loginrepository = new Loginrepository();
			Message = loginrepository.signin(user.getEmail(),user.getPasswrd());
		} catch (UnknownHostException e) {
			return Response.status(400).entity(Message).build();
		} catch (IOException e) {
			return Response.status(400).entity(Message).build();
		}
		
		return Response.status(200).entity(Message).build();

	}
}
