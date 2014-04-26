package com.sjsu.mongodb;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.sjsu.utilities.DatabaseConstants;

public class Loginrepository {

	MongoClient mongoClient = null;

	public Loginrepository() throws UnknownHostException {
		mongoClient = new MongoClient(DatabaseConstants.MONGO_HOST_NAME, DatabaseConstants.MONGO_PORT_NUMBER);
		System.out.println("Connected To Mongo DB Successfully at Host " + DatabaseConstants.MONGO_HOST_NAME + " Port Number " + DatabaseConstants.MONGO_PORT_NUMBER);
	}

	public void listAllDatabases() {

		List<String> databaseNamesList = mongoClient.getDatabaseNames();

		for (String db : databaseNamesList) {
			System.out.println(db);
		}
	}

	public String signin(String email,String passwrd) throws IOException {

			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("email", email);
			DBCollection collection = getBookmarkCollection();
			DBCursor cursor = collection.find(whereQuery);
			if (cursor.size() > 0){
				while(cursor.hasNext()) {
					BSONObject json = cursor.next();
					String pass = (String) json.get("password");
					
					if (passwrd.equals(pass)){
						System.out.println("success");
					}else{
						System.out.println("Invalid passowrd");
						return "{\"Failed\": \"Invalid Password\"}";
					}
				}
				return "{\"Success\": \"successfully logged\"}";
			}
		return "{\"Failed\": \"Invalid Login credentials\"}";
	}

	private DBCollection getBookmarkCollection() {
		DB db = mongoClient.getDB(DatabaseConstants.DATABASE_NAME);
		DBCollection collection = db.getCollection(DatabaseConstants.USER_TABLE_NAME);
		return collection;
	}

	private boolean validateUser(String emailId) throws IOException {
		if (emailId == null || emailId.isEmpty()) {
			throw new IOException("Please enter the email id of the user");
		}
		return true;
	}

}
