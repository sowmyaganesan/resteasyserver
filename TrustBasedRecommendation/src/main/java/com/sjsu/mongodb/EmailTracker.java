package com.sjsu.mongodb;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.sjsu.pojo.Inviteemails;
import com.sjsu.utilities.DatabaseConstants;

public class EmailTracker {

	MongoClient mongoClient = null;

	public EmailTracker() throws UnknownHostException {
		mongoClient = new MongoClient(DatabaseConstants.MONGO_HOST_NAME, DatabaseConstants.MONGO_PORT_NUMBER);
		System.out.println("Connected To Mongo DB Successfully at Host " + DatabaseConstants.MONGO_HOST_NAME + " Port Number " + DatabaseConstants.MONGO_PORT_NUMBER);
	}

	public void listAllDatabases() {
		List<String> databaseNamesList = mongoClient.getDatabaseNames();

		for (String db : databaseNamesList) { 
			System.out.println(db);
		}
	}

	public String updateemailedlist(Inviteemails inviteemails) throws IOException {
		DBCollection emailedCollection = getEmailedCollection();
			List<String> inviteemailslst = inviteemails.getEmailaddress();
			Iterator<String> iterator = inviteemailslst.iterator();
			while (iterator.hasNext()) {
				String bitr= iterator.next();
				BasicDBObject document = new BasicDBObject();
				document.put("senderemail", inviteemails.getSenderemail());
				document.put("emailaddress", bitr);

				WriteResult result = emailedCollection.insert(document);
				String error = result.getError();
				if (error != null) {
					throw new IOException("Error adding the user with email " + error);
				}
				
			}
			return "{\"Success\": \"Added User\"}";	
	}
		
		public DBCollection getEmailedCollection() {
			DB db = mongoClient.getDB(DatabaseConstants.DATABASE_NAME);
			DBCollection collection = db.getCollection(DatabaseConstants.EMAIL_TABLE_NAME);
			return collection;
	}
		public DBCollection getUserCollection() {
			DB db = mongoClient.getDB(DatabaseConstants.DATABASE_NAME);
			DBCollection collection = db.getCollection(DatabaseConstants.USER_TABLE_NAME);
			return collection;
		}
}
