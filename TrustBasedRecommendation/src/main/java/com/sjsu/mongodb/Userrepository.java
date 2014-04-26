package com.sjsu.mongodb;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.BSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.sjsu.model.Bookmark;
import com.sjsu.model.TrustScoreCollection;
import com.sjsu.utilities.DatabaseConstants;

public class Userrepository {

	MongoClient mongoClient = null;

	public Userrepository() throws UnknownHostException {
		mongoClient = new MongoClient(DatabaseConstants.MONGO_HOST_NAME, DatabaseConstants.MONGO_PORT_NUMBER);
		System.out.println("Connected To Mongo DB Successfully at Host " + DatabaseConstants.MONGO_HOST_NAME + " Port Number " + DatabaseConstants.MONGO_PORT_NUMBER);
	}

	public void listAllDatabases() {
		List<String> databaseNamesList = mongoClient.getDatabaseNames();

		for (String db : databaseNamesList) {
			System.out.println(db);
		}
	}

	public String addfriend(TrustScoreCollection trustScoreCollection){

		try {
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("email", trustScoreCollection.getUser());
			DBCollection usercollection = getUserCollection();
			DBCollection trustcollection = getTrustCollection();
			DBCursor cursor = usercollection.find(whereQuery);
			if(cursor.size() > 0){
				String bookmarkupdated = null;
				while(cursor.hasNext()) {
					BSONObject json = cursor.next();
					List<String> existingfriends = (List<String>) json.get("friends");
					
					if (existingfriends != null && !existingfriends.equals("")) {
						if (!existingfriends.contains(trustScoreCollection.getFriend())){
			 				//TypeToken<String[]> frdtoken = new TypeToken<String[]>(){};
			 				//String[] frdarr = new Gson().fromJson(existingfriends, frdtoken.getType());
							existingfriends.add(trustScoreCollection.getFriend());
			 				
			 				BasicDBObject searchQuery = new BasicDBObject().append("email", trustScoreCollection.getUser());
						    BasicDBObject newDocument = new BasicDBObject();
							newDocument.append("$set", new BasicDBObject().append("friends", existingfriends));
							usercollection.update(searchQuery, newDocument);
							
							BasicDBObject document = new BasicDBObject();
							document.put("user", trustScoreCollection.getUser());
							document.put("friend", trustScoreCollection.getFriend());
							document.put("category", trustScoreCollection.getCategory());
							document.put("trustscore", trustScoreCollection.getTrustscore());
							document.put("explicit", trustScoreCollection.isExplicit());
							WriteResult result = trustcollection.insert(document);
							String error = result.getError();
							if (error != null) {
								throw new IOException("Error adding the friend with email id " + trustScoreCollection.getUser() + error);
							}
							return "{\"Success\": \"Added User\"}";							
						}else{
							return "{\"Failed\": \"user exist in friends list\"}";
						}
					}else{
						List<String> frdarr = new ArrayList<String>();
						frdarr.add(trustScoreCollection.getFriend());
						BasicDBObject searchQuery = new BasicDBObject().append("email", trustScoreCollection.getUser());
					    BasicDBObject newDocument = new BasicDBObject();
						newDocument.append("$set", new BasicDBObject().append("friends", frdarr));
						usercollection.update(searchQuery, newDocument);
						
						BasicDBObject document = new BasicDBObject();
						document.put("user", trustScoreCollection.getUser());
						document.put("friend", trustScoreCollection.getFriend());
						document.put("category", trustScoreCollection.getCategory());
						document.put("trustscore", trustScoreCollection.getTrustscore());
						document.put("explicit", trustScoreCollection.isExplicit());
						WriteResult result = trustcollection.insert(document);
						String error = result.getError();
						if (error != null) {
							throw new IOException("Error adding the friend with email id " + trustScoreCollection.getUser() + error);
						}
						return "{\"Success\": \"Added User\"}";
					}
			}
				return "{\"Failed\": \"User doesnt exist\"}";
			}
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "{\"Failed\": \"Could not update friends list\"}";
	}

	
	public String removefriend(TrustScoreCollection trustScoreCollection) throws IOException {

			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("email", trustScoreCollection.getUser());
			DBCollection usercollection = getUserCollection();
			DBCollection trustcollection = getTrustCollection();
			DBCursor cursor = usercollection.find(whereQuery);
			if(cursor.size() > 0){
				while(cursor.hasNext()) {
					BSONObject json = cursor.next();
					List<String> existingfriends = (List<String>) json.get("friends");
					if (!existingfriends.equals("") && existingfriends != null){
						if (existingfriends.contains(trustScoreCollection.getFriend())){
							Iterator<String> iterator = existingfriends.iterator();
							while (iterator.hasNext()) {
								String bitr= iterator.next();
								if (bitr.equals(trustScoreCollection.getFriend())){
									iterator.remove();
								}
							}
							BasicDBObject searchQuery = new BasicDBObject().append("email", trustScoreCollection.getUser());
						    BasicDBObject newDocument = new BasicDBObject();
							newDocument.append("$set", new BasicDBObject().append("friends", existingfriends));
							usercollection.update(searchQuery, newDocument);
							
							BasicDBObject deleteDocument = new BasicDBObject();
							deleteDocument.put("friend", trustScoreCollection.getFriend());
					
							WriteResult result = trustcollection.remove(deleteDocument);
							String error = result.getError();
							if (error != null) {
								throw new IOException("Error deleting the user with emailid " + trustScoreCollection.getFriend() + error);
							}
							System.out.println("Number of Documents deleted is " + result.getN());
							return "{\"Success\": \"Deleted User information\"}";
						}
					}
				}
			}
			return "{\"Failed\": \"Internal error either user doesnt exist or error in deletion\"}";
		
	}
	
	private DBCollection getUserCollection() {
		DB db = mongoClient.getDB(DatabaseConstants.DATABASE_NAME);
		DBCollection collection = db.getCollection(DatabaseConstants.USER_TABLE_NAME);
		return collection;
	}
	private DBCollection getTrustCollection() {
		DB db = mongoClient.getDB(DatabaseConstants.DATABASE_NAME);
		DBCollection collection = db.getCollection(DatabaseConstants.TRUST_TABLE_NAME);
		return collection;
	}
}
