package com.sjsu.mongodb;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.sjsu.model.User;
import com.sjsu.utilities.DatabaseConstants;

public class MongoDBClient {

	MongoClient mongoClient = null;

	public MongoDBClient() throws UnknownHostException {
		mongoClient = new MongoClient(DatabaseConstants.MONGO_HOST_NAME, DatabaseConstants.MONGO_PORT_NUMBER);
		System.out.println("Connected To Mongo DB Successfully at Host " + DatabaseConstants.MONGO_HOST_NAME + " Port Number " + DatabaseConstants.MONGO_PORT_NUMBER);
	}

	public void listAllDatabases() {

		List<String> databaseNamesList = mongoClient.getDatabaseNames();

		for (String db : databaseNamesList) {
			System.out.println(db);
		}
	}

	public String addUser(User user) throws IOException {

		validateUser(user.getEmail());
		DBCollection collection = getUserCollection();
		DBObject query = new BasicDBObject("email", new BasicDBObject("$exists", true));
		DBCursor existresult = collection.find(query);
		if (existresult.size() == 0){
			BasicDBObject document = new BasicDBObject();
			document.put("email", user.getEmail());
			document.put("city", user.getCity());
			document.put("name", user.getName());
			document.put("zip", user.getZip());
			document.put("password", user.getPasswrd());
			WriteResult result = collection.insert(document);
			String error = result.getError();
			if (error != null) {
				throw new IOException("Error adding the user with email id " + user.getEmail() + error);
			}
			return "{\"Success\": \"Added User\"}";
		}
		return "{\"Failed\": \"User already exist\"}";

	}

	public String updateUser(User user) throws IOException {

		String emailId = user.getEmail();
		validateUser(emailId);
		DBCollection collection = getUserCollection();
		DBObject query = new BasicDBObject("email", new BasicDBObject("$exists", true));
		DBCursor existresult = collection.find(query);
		if (existresult.size() > 0){
			DBObject history = new BasicDBObject();
			history.put("city", user.getCity());
			history.put("name", user.getName());
			history.put("zip", user.getZip());
	
			DBObject update = new BasicDBObject("$set", history);
	
			// search with email id and then udpate the object
			BasicDBObject searchQuery = new BasicDBObject().append("email", emailId);
			WriteResult result = collection.update(searchQuery, update);
			String error = result.getError();
			if (error != null) {
				throw new IOException("Error updating the user with email id " + emailId + error);
			}
			System.out.println("Number of Documents updated is " + result.getN());
			return "{\"Success\": \"Updated User information\"}";
		}
		return "{\"Failed\": \"User doesnt exist\"}";
	}

	public String deleteUser(String email) throws IOException {

		String emailId = email;
		validateUser(emailId);
		DBCollection collection = getUserCollection();
		DBObject query = new BasicDBObject("email", new BasicDBObject("$exists", true));
		DBCursor existresult = collection.find(query);
		if (existresult.size() > 0){
			BasicDBObject deleteDocument = new BasicDBObject();
			deleteDocument.put("email", emailId);
	
			WriteResult result = collection.remove(deleteDocument);
			String error = result.getError();
			if (error != null) {
				throw new IOException("Error deleting the user with emailid " + emailId + error);
			}
			System.out.println("Number of Documents deleted is " + result.getN());
			return "{\"Success\": \"Deleted User information\"}";
		}
		return "{\"Failed\": \"User doesnt exist\"}";
	}

	private DBCollection getUserCollection() {
		DB db = mongoClient.getDB(DatabaseConstants.DATABASE_NAME);
		DBCollection collection = db.getCollection(DatabaseConstants.USER_TABLE_NAME);
		return collection;
	}

	private void validateUser(String emailId) throws IOException {
		if (emailId == null || emailId.isEmpty()) {
			throw new IOException("Please enter the email id of the user");
		}

	}

}
