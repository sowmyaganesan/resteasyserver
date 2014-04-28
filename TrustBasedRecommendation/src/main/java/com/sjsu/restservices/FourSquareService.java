package com.sjsu.restservices;

import java.net.UnknownHostException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.mongodb.MongoClient;
import com.sjsu.foursquare.FourSquareClient;
import com.sjsu.mongodb.MongoDBClient;
import com.sjsu.pojo.FourSquareVenue;

import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.Category;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;



@Path("/foursquareservice")
public class FourSquareService {
	
	
	/*
	 * Input Format 
	 * 
{
    "findLatitude": "+37.189396",
    "findLongititude": "-121.705327"
}
	 * 
	 */

	@POST
	@Path("/getVenueByCoordinates")
	@Consumes("application/json")
	@Produces("application/json")
	public FourSquareVenue getVenueByCoordinates(FourSquareVenue fourSquareVenue )
	{
		
		/*FourSquareVenue fourSquareVenue  = new FourSquareVenue();
		fourSquareVenue.setFindLatitude("+37.189396");
		fourSquareVenue.setFindLongititude("-121.705327");*/
		
		String clientID = "FQ2N5PKZRPQVZFWLP3MC3H43O5N2YUCTPYFGS3W503OGOGQP";
		String clientSecret = "42ZECLAWYZOBY5W1EOSV2PFUJZCRCXFTOZN0ITSGEZAB4YDY";
		String callBackURL = "http://www.testapp.com";
		FourSquareVenue returnValueEntity = new FourSquareVenue();

		FourSquareClient fourSquareClient = new FourSquareClient(clientID, clientSecret, callBackURL);
		String coordinate = fourSquareVenue.getFindLatitude()+","+fourSquareVenue.getFindLongititude();
		
		MongoDBClient mongoClient = null ;
		try {
			 mongoClient = new MongoDBClient();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			Result<VenuesSearchResult> result = fourSquareClient.getVenueSearch(coordinate, null, null, null, null, null, null, null , null, null, null);
			if (result.getMeta().getCode() == 200) {
				// if query was ok we can finally we do something with the data
				for (CompactVenue venue : result.getResult().getVenues()) {
					if(venue.getCategories().length >0  & venue.getLocation().getAddress() != null  ) 
					{
					returnValueEntity.getVenueNameList().add(venue.getName());
					
					returnValueEntity.getVenueAddressList().add(venue.getLocation().getAddress());
					returnValueEntity.getVenueCityList().add(venue.getLocation().getCity());
					Category[] category = venue.getCategories();
					for(int i=0;i<category.length;i++)
					{
					String subCategory = 	category[i].getName() ; 
					String superCategory = mongoClient.findSuperCategory(subCategory);
						
					
					if(superCategory == null )
						superCategory = "nocategory";
						//returnValueEntity.getVenueCategoryList().add(category[i].getName());
						
						returnValueEntity.getVenueCategoryList().add(superCategory);
					}
				}///end of for loop
					
				}
				return returnValueEntity;
			}

			else {
				return returnValueEntity;
				
			}

		} catch (FoursquareApiException e) {

			return returnValueEntity;
		}
		
	}
	
	
	
	/*
	 * 
	 * Input Format 
	
"findNearVenueCity": "santa clara"

}
	 */
	@POST
	@Path("/getVenueByNearLocation")
	@Consumes("application/json")
	@Produces("application/json")
	public FourSquareVenue getVenueByNearLocation(FourSquareVenue fourSquareVenue)
	{
		String clientID = "FQ2N5PKZRPQVZFWLP3MC3H43O5N2YUCTPYFGS3W503OGOGQP";
		String clientSecret = "42ZECLAWYZOBY5W1EOSV2PFUJZCRCXFTOZN0ITSGEZAB4YDY";
		String callBackURL = "http://www.testapp.com";
		
		FourSquareVenue returnValueEntity = new FourSquareVenue();

		FourSquareClient fourSquareClient = new FourSquareClient(clientID, clientSecret, callBackURL);
		String nearLocation = fourSquareVenue.getFindNearVenueCity();
		
		MongoDBClient mongoClient = null ;
		try {
			 mongoClient = new MongoDBClient();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			Result<VenuesSearchResult> result = fourSquareClient.getVenueSearch(nearLocation, null, null, null, null, null, null, null);
			if (result.getMeta().getCode() == 200) {
				// if query was ok we can finally we do something with the data
				for (CompactVenue venue : result.getResult().getVenues()) {
					if(venue.getCategories().length >0  & venue.getLocation().getAddress() != null  ) 
					{

				
					returnValueEntity.getVenueNameList().add(venue.getName());
					returnValueEntity.getVenueAddressList().add(venue.getLocation().getAddress());
					returnValueEntity.getVenueCityList().add(venue.getLocation().getCity());
					Category[] category = venue.getCategories();
					for(int i=0;i<category.length;i++)
					{
					//	returnValueEntity.getVenueCategoryList().add(category[i].getName());
						
						String subCategory = 	category[i].getName() ; 
						String superCategory = mongoClient.findSuperCategory(subCategory);
							
							//returnValueEntity.getVenueCategoryList().add(category[i].getName());
						if(superCategory == null )
							superCategory = "nocategory";
							
							returnValueEntity.getVenueCategoryList().add(superCategory);
					}
				}///end of for loop
					
				}
				return returnValueEntity;
			}

			else {
				return returnValueEntity;
				
			}

		} catch (FoursquareApiException e) {

			return returnValueEntity;
		}
		
	}
	
	
	
}
