package com.sjsu.restservices;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sjsu.mongodb.MongoDBClient;
import com.sjsu.pojo.Bookmark;
import com.sjsu.pojo.User;

@Path("/trustbasedtecommendationservice")
public class TrustBasedRecommendationService {

	@GET
	@Path("/gettrustrecommendation")
	public List<Bookmark> getRecommendation(User user  ) {
		List<Bookmark> bookmarksList =  new ArrayList<Bookmark>()  ;
		try {
			MongoDBClient mongoClient = new MongoDBClient();
			 bookmarksList = mongoClient.getRecommendationsforUser(user);
			if(bookmarksList.size() == 0  )
			{
				 bookmarksList = mongoClient.getPopularRecommendationinCategory();
			}
			
			for (int i = 0 ; i <bookmarksList.size() ; i++ )
			{
				Bookmark bookmark = (Bookmark)bookmarksList.get(i);
				System.out.println(bookmark.toString()) ; 
			}
		
		} catch (Exception e) {
		
		}
		return bookmarksList;

	}
	
	
	
	
	
	
	

	/*@GET
	@Path("/getnewuserrecommendation")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getNewUserRecommendation(User user , List<String>  categoryList) {
		String failedMessage = "failed to get popular  recommendation";
		String successMessage = "got popular recommentation  ";
		try {
			MongoDBClient mongoClient = new MongoDBClient();
			
			List<Bookmark> bookmarksList = mongoClient.getPopularRecommendationinCategory(  categoryList);
			
			
		
		
		} catch (UnknownHostException e) {
			return Response.status(400).entity(failedMessage).build();
		} catch (IOException e) {
			return Response.status(400).entity(failedMessage).build();
		}

		// 200 denotes it is success
		return Response.status(200).entity(successMessage).build();

	}*/

}
