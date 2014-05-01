package com.sjsu.mongodb;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.sjsu.pojo.TrustScoreCollection;
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

	/*public String addTrustscoretofriend(TrustScoreCollection trustScoreCollection){

		try {
			MongoDBClient mongoDBClient = new MongoDBClient();
			List<String> categorylist = mongoDBClient.getAllCategories();
			HashMap<String,Double>  categorymap = new HashMap<String, Double>();
			Iterator<String> itr = categorylist.iterator();
			while (itr.hasNext()) {
				categorymap.put(itr.next(), 0.00);
			}
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("email", trustScoreCollection.getUser());
			DBCollection usercollection = getUserCollection();
			DBCollection trustcollection = getTrustCollection();
			DBCursor cursor = usercollection.find(whereQuery);
			if(cursor.size() > 0){
				while(cursor.hasNext()) {
					BSONObject json = cursor.next();
					List<String> existingfriends = (List<String>) json.get("friends");
					
					if (existingfriends != null && !existingfriends.equals("")) {
						if (existingfriends.contains(trustScoreCollection.getFriend())){
							
							BasicDBObject whereexistbook = new BasicDBObject();
							whereexistbook.put("user", trustScoreCollection.getUser());
							whereexistbook.put("friend", trustScoreCollection.getFriend());
							if (categorymap.containsKey(trustScoreCollection.getCategory())){
								categorymap.put(trustScoreCollection.getCategory(),trustScoreCollection.getTrustscore());
							}
							whereexistbook.put("category", trustScoreCollection.getCategory());
							
							DBCursor bookcursor = trustcollection.find(whereexistbook);
							
							if(bookcursor.size() > 0){
								return "{\"Failed\": \"User has already been given trust score try update\"}";	
							}else{
								BasicDBObject document = new BasicDBObject();
								document.put("user", trustScoreCollection.getUser());
								document.put("friend", trustScoreCollection.getFriend());
								document.put("category", trustScoreCollection.getCategory());
								document.put("trustscore", trustScoreCollection.getTrustscore());
								document.put("explicit", trustScoreCollection.getExplicit());
								WriteResult result = trustcollection.insert(document);
								String error = result.getError();
								if (error != null) {
									throw new IOException("Error adding the friend with email id " + trustScoreCollection.getUser() + error);
								}
								return "{\"Success\": \"Added Trust score\"}";
							}
														
						}else{
							return "{\"Failed\": \"user doesnt exist in friends list\"}";
						}
					}else{
						return "{\"Failed\": \"user not a friend please send him a friend request\"}";
					}
				}
			}else{
				return "{\"Failed\": \"User doesnt exist\"}";
			}
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "{\"Failed\": \"Could not update trust score\"}";
	}*/

	public String removefriendfromtrustnw(String user, String friend, String category) throws IOException {

		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("user", user);
		whereQuery.put("friend", friend);
		whereQuery.put("category", category);
		DBCollection trustcollection = getTrustCollection();
		DBCursor cursor = trustcollection.find(whereQuery);
		if(cursor.size() > 0){
			while(cursor.hasNext()) {
						WriteResult result = trustcollection.remove(whereQuery);
						String error = result.getError();
						if (error != null) {
							throw new IOException("Error deleting the user with emailid " + friend + error);
						}
						System.out.println("Number of Documents deleted is " + result.getN());
						return "{\"Success\": \"Deleted User information\"}";
			}
			
		}
		return "{\"Failed\": \"Internal error either user doesnt exist or error in deletion\"}"; 
}

	public String updateTrustscoretofriend(List<TrustScoreCollection> trustScoreCollection) throws IOException {

		if (trustScoreCollection.size() > 0){
			Iterator<TrustScoreCollection> itr = trustScoreCollection.iterator();
			DBCollection trustcollection = getTrustCollection();
			while (itr.hasNext()) {
				TrustScoreCollection tempcollection = itr.next();
				BasicDBObject whereQuery = new BasicDBObject();
				whereQuery.put("user", tempcollection.getUser());
				whereQuery.put("friend", tempcollection.getFriend());
				whereQuery.put("category", tempcollection.getCategory());
				DBCursor cursor = trustcollection.find(whereQuery);
				if(cursor.size() > 0){
					while(cursor.hasNext()) {
						BSONObject bjson = cursor.next();
						if (tempcollection.getTrustscore() > 0 && tempcollection.getTrustscore() != ((Double) bjson.get("trustscore"))){
							BasicDBObject newDocument = new BasicDBObject();
							newDocument.append("$set", new BasicDBObject().append("trustscore", tempcollection.getTrustscore()));
							WriteResult result =trustcollection.update(whereQuery, newDocument);
							String error = result.getError();
							if (error != null) {
								throw new IOException("Error deleting the user with emailid " + tempcollection.getFriend() + error);
							}
							System.out.println("Number of Documents updated is " + result.getN());
						}else {
							WriteResult result = trustcollection.remove(whereQuery);
							String error = result.getError();
							if (error != null) {
								throw new IOException("Error deleting the user with emailid " + tempcollection.getFriend() + error);
							}
							System.out.println("Number of Documents deleted is " + result.getN());
						}
					}
				}else{
					if (tempcollection.getTrustscore() > 0){
						BasicDBObject document = new BasicDBObject();
						document.put("user", tempcollection.getUser());
						document.put("friend", tempcollection.getFriend());
						document.put("category", tempcollection.getCategory());
						document.put("trustscore", tempcollection.getTrustscore());
						document.put("explicit", tempcollection.getExplicit());
						WriteResult result = trustcollection.insert(document);
						String error = result.getError();
						if (error != null) {
							throw new IOException("Error adding the friend with email id " + tempcollection.getFriend() + error);
						}
					}
				}
			}
			return "{\"Success\": \"Updated trust score\"}";
		}
		return "{\"Failed\": \"Internal error either list is empty or user doesnt exist\"}";
}

	private DBCollection getUserCollection() {
		DB db = mongoClient.getDB(DatabaseConstants.DATABASE_NAME);
		DBCollection collection = db.getCollection(DatabaseConstants.USER_TABLE_NAME);
		return collection;
	}
	private DBCollection getTrustCollection() {
		DB db = mongoClient.getDB(DatabaseConstants.DATABASE_NAME);
		DBCollection collection = db.getCollection(DatabaseConstants.TRUSTSCORE_TABLE_NAME);
		return collection;
	}
}
