package com.sjsu.Recommendation;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sjsu.foursquare.FourSquareClient;
import com.sjsu.mongodb.MongoDBClient;
import com.sjsu.pojo.User;
import com.sjsu.pojo.UserRecommendation;

import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.Category;

public class RecommendationProcessDriver {

	public static void main(String[] args) throws UnknownHostException {
		
		startupOperations();
		
		
		ExplicitRecommendationProcess explictrecommendationProcess = new ExplicitRecommendationProcess();
		
		explictrecommendationProcess.userExplictionRecommendationProcess(); 
		
		
		
		//implicit reco 
		
		
		ImplicitRecommendationProcess implicitRecoProcess = new ImplicitRecommendationProcess();

		List<User> usersList = implicitRecoProcess
				.getAllUserandFrndsofSystemfromDB();

		implicitRecoProcess.useRecommendationAttrMap = implicitRecoProcess
				.processDataPopulateMap(usersList);

		

		implicitRecoProcess.useRecommendationAttrMap = implicitRecoProcess
				.findCosineSimiliartyofFrnds(usersList);

		for (Map.Entry entry : implicitRecoProcess.useRecommendationAttrMap.entrySet()) {
		    System.out.print("key,val: ");
		    System.out.println(entry.getKey() + "," + entry.getValue());
		}
		
		
		
		List<UserRecommendation>  userRecommendationList=  implicitRecoProcess.getBookmarkRecommendationforUser(usersList);
		
		
		//Find out the explict recommendations already entered in the system . 
		
		List<String >  usersExplicitRecoList = implicitRecoProcess.FindExplictRecommendationUsers();
		
		HashMap <String , String > usersExplicitRecoListMap =  new HashMap<String ,String>();
		
		for(int i = 0  ;i < usersExplicitRecoList.size() ; i++ )
		{
			usersExplicitRecoListMap.put(usersExplicitRecoList.get(i), usersExplicitRecoList.get(i)); 
		}
		
		for(int i = 0 ;i <userRecommendationList.size() ; i++)
		{
			if(usersExplicitRecoListMap.containsKey(userRecommendationList.get(i).getEmail()))
			{
				userRecommendationList.remove(i);
				i--;
			}
		}
		
		
		
		
		implicitRecoProcess.populateUserRecommedationinDB(userRecommendationList);
		 

		

	}

	private static void startupOperations() {
		try {
			MongoDBClient mongoClient  = new MongoDBClient();
			
			List<String>  categoiesList = mongoClient.getAllCategories() ;
			if(categoiesList.size() <= 0)
			{
				getCategoriesfromFoursquare();
			}
			
			mongoClient.RecommendationStartupOperation();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private static void getCategoriesfromFoursquare() throws Exception {
		String clientID = "FQ2N5PKZRPQVZFWLP3MC3H43O5N2YUCTPYFGS3W503OGOGQP";
		String clientSecret = "42ZECLAWYZOBY5W1EOSV2PFUJZCRCXFTOZN0ITSGEZAB4YDY";
		String callBackURL = "http://www.testapp.com";

		FourSquareClient fourSquareClient = new FourSquareClient(clientID, clientSecret, callBackURL);
		
		MongoDBClient mongoClient = new MongoDBClient();

		try {
			Result<Category[]> resultArray = fourSquareClient.getCategories();
			if (resultArray.getMeta().getCode() == 200) {
				// if query was ok we can finally we do something with the data
				Category[]   categoryArray = resultArray.getResult();
				for(int i = 0 ; i <categoryArray.length ; i++)
				{
					String superCategory = categoryArray[i].getName();
					System.out.println(categoryArray[i].getName());
					Category[]   categorySubArray  =  categoryArray[i].getCategories();
					
					
					
					
					for(int k = 0 ; k <categorySubArray.length ; k++)
					{
						String subCategory = categorySubArray[k].getName();
						
						mongoClient.addCategories(superCategory , subCategory);
						System.out.println("	"  + categorySubArray[k].getName());
					}

					
					//System.out.println(categoryArray[i].getPluralName() );
				}
			}


			else {
				System.out.println("Error occured: ");
				System.out.println("  code: " + resultArray.getMeta().getCode());
				System.out.println("  type: " + resultArray.getMeta().getErrorType());
				System.out.println("  detail: " + resultArray.getMeta().getErrorDetail());
			}

		} catch (FoursquareApiException e) {

			e.printStackTrace();
		}
		
	}

}
