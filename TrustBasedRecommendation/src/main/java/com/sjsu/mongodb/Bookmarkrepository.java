package com.sjsu.mongodb;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bson.BSONObject;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.sjsu.pojo.Bookmarkcollection;
import com.sjsu.pojo.Categories;
import com.sjsu.pojo.Friendlist;
import com.sjsu.pojo.Friends;
import com.sjsu.pojo.TrustScoreCollection;
import com.sjsu.utilities.DatabaseConstants;

public class Bookmarkrepository {

	MongoClient mongoClient = null;

	public Bookmarkrepository() throws UnknownHostException {
		mongoClient = new MongoClient(DatabaseConstants.MONGO_HOST_NAME, DatabaseConstants.MONGO_PORT_NUMBER);
		System.out.println("Connected To Mongo DB Successfully at Host " + DatabaseConstants.MONGO_HOST_NAME + " Port Number " + DatabaseConstants.MONGO_PORT_NUMBER);
	}

	public void listAllDatabases() {
		List<String> databaseNamesList = mongoClient.getDatabaseNames();

		for (String db : databaseNamesList) {
			System.out.println(db);
		}
	}

	public String getallfriendsofuser(String email){

		try {
			MongoDBClient mongoDBClient = new MongoDBClient();
			List<String> categorylist = mongoDBClient.getAllCategories();
			HashMap<String,Double>  categorymap = new HashMap<String, Double>();
			Iterator<String> itr = categorylist.iterator();
			while (itr.hasNext()) {
				categorymap.put(itr.next(), 0.00);
			}
			DBCollection collection = getUserCollection();
			DBObject query = new BasicDBObject("email", email);
			DBCursor existresult = collection.find(query);
			DBCollection trustcollection = gettrustCollection();
			Friendlist friendlist = new Friendlist();
			if (existresult.size() > 0){
				
				BSONObject json = existresult.next();
				List<Object> existfriends = (List<Object>) json.get("friends");
				System.out.println(existfriends.size());
				if (existfriends == null)
					return "{\"Success\": \"No friends exist for the user\"}";
				else{
					List<Friends> newfrdlst = new ArrayList<Friends>();
					Iterator<Object> iterator = existfriends.iterator();
					String name = "Unknown";
					while (iterator.hasNext()) {
						Object bitr= iterator.next();
						query = new BasicDBObject("email", bitr);
						DBCursor usercursor = collection.find(query);
						if (usercursor.size() > 0){
							while(usercursor.hasNext()){
								BSONObject userjson = usercursor.next();
								name = (String) userjson.get("name");
							}
						}
						
						BasicDBObject whereQuery = new BasicDBObject();
						whereQuery.put("friend", bitr);
						whereQuery.put("user", email);
						
						DBCursor cursor = trustcollection.find(whereQuery);
						Friends friends = new Friends();
						friends.setName(name);
						List<Categories> newcategorylst = new ArrayList<Categories>();
						friends.setUser(String.valueOf(bitr));
						if(cursor.size() > 0){
							while(cursor.hasNext()){
								Categories categories = new Categories();
								BSONObject bjson = cursor.next();
								categories.setCategory((String) bjson.get("category"));
								categories.setScore((Double) bjson.get("trustscore"));
								newcategorylst.add(categories);
							}
							
						}
						Iterator<String> citr = categorylist.iterator();
						while (citr.hasNext()) {
							String newcat = citr.next();
							if (!newcategorylst.contains(newcat)){
								Categories categories = new Categories();
								categories.setCategory(newcat);
								categories.setScore(0.00);
								newcategorylst.add(categories);
							}
						}
						friends.setCategories(newcategorylst);
						newfrdlst.add(friends);
					}
					friendlist.setUser(email);
					friendlist.setFriends(newfrdlst);
					String friends = new Gson().toJson(friendlist);
					System.out.println(friends);
					return friends;
				}
			}
			return "{\"Failed\": \"User doesnt exist\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"Failed\": \"Could not add bookmark\"}";
		}
}

	public String getauserbookmark(String email){

			try {
				DBCollection collection = getUserCollection();
				DBObject query = new BasicDBObject("email", email);
				DBCursor existresult = collection.find(query);
				if (existresult.size() > 0){
					
					BSONObject json = existresult.next();
					List<Object> existbookmark = (List<Object>) json.get("bookmarks");
					
					if (existbookmark == null)
						return "{\"Success\": \"No bookmark exist for the user\"}";
					else{
						List<BSONObject> bookmarkcollections = new ArrayList<BSONObject>();
						Iterator<Object> iterator = existbookmark.iterator();
						while (iterator.hasNext()) {
							Object bitr= iterator.next();
							BasicDBObject whereQuery = new BasicDBObject().append("_id", bitr);
							DBCollection bookcollection = getBookmarkCollection();
							DBCursor cursor = bookcollection.find(whereQuery);
							while(cursor.hasNext()){
								BSONObject bjson = cursor.next();
								bookmarkcollections.add(bjson);
							}
						}
						String book = new Gson().toJson(bookmarkcollections);
						return new Gson().toJson(bookmarkcollections);
					}
				}
				return "{\"Failed\": \"User doesnt exist\"}";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "{\"Failed\": \"Could not add bookmark\"}";
			}
	}

	public String updatebookmark(Bookmarkcollection bookmarkcollection){

		try {
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("email", bookmarkcollection.getEmail());
			DBCollection collection = getUserCollection();
			DBCursor cursor = collection.find(whereQuery);
			if(cursor.size() > 0){
				while(cursor.hasNext()) {
					BSONObject json = cursor.next();
					
					List<Object> existbookmark = (List<Object>) json.get("bookmarks");
					BasicDBObject whereexistbook = new BasicDBObject();
					whereexistbook.put("email", bookmarkcollection.getEmail());
					whereexistbook.put("name", bookmarkcollection.getName());
					whereexistbook.put("location", bookmarkcollection.getLocation());
					
					DBCollection bookcollection = getBookmarkCollection();
					DBCursor bookcursor = bookcollection.find(whereexistbook);
					
					if(bookcursor.size() > 0){
						return "{\"Failed\": \"Bookmark already exist\"}";
					}else{
						BasicDBObject document = new BasicDBObject();
						document.put("email", bookmarkcollection.getEmail());
						document.put("name", bookmarkcollection.getName());
						document.put("location", bookmarkcollection.getLocation());
						document.put("category", bookmarkcollection.getCategory());
						document.put("stats", bookmarkcollection.getStats());
						document.put("tried", bookmarkcollection.isTried());
						document.put("status", bookmarkcollection.getStatus());
						WriteResult result = bookcollection.insert(document);
						String error = result.getError();
						if (error != null) {
							throw new IOException("Error adding the friend with email id " + bookmarkcollection.getEmail() + error);
						}
						BasicDBObject whereQuery1 = new BasicDBObject();
						whereQuery1.put("email", bookmarkcollection.getEmail());
						bookcursor = bookcollection.find(whereQuery).sort(new BasicDBObject("_id",-1)).limit(1);;
						while(bookcursor.hasNext()){
							BSONObject bjson = bookcursor.next();
							Object _id = bjson.get("_id");
							if (existbookmark == null)
								existbookmark = new ArrayList<Object>();
							existbookmark.add(_id);
							BasicDBObject newDocument = new BasicDBObject();
							newDocument.append("$set", new BasicDBObject().append("bookmarks", existbookmark));
							collection.update(whereQuery, newDocument);
						}
					}
				}
				return "{\"Sucess\": \"Updated bookmark\"}";
			}
				return "{\"Failed\": \"User doesnt exist\"}";
				
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "{\"Failed\": \"Could not update bookmark\"}";
}
	public String deletebookmark(String bookmarkid, String email) throws IOException {

			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("_id", new ObjectId(bookmarkid));
			DBCollection bookcollection = getBookmarkCollection();
			DBCursor cursor = bookcollection.find(whereQuery);
			if(cursor.size() > 0){
				while(cursor.hasNext()){
					BasicDBObject whereuserQuery = new BasicDBObject();
					whereuserQuery.put("email", email);
					DBCollection usercollection = getUserCollection();
					DBCursor usercursor = usercollection.find(whereuserQuery);
					if(usercursor.size() > 0){
						while(usercursor.hasNext()){
							BSONObject json = usercursor.next();
							List<Object> existingbookmarks = (List<Object>) json.get("bookmarks");
							Iterator<Object> iterator = existingbookmarks.iterator();
							while (iterator.hasNext()) {
								Object bitr= iterator.next();
								if (bitr.equals(bookmarkid)){
									iterator.remove();
								}
							}
						    BasicDBObject newDocument = new BasicDBObject();
							newDocument.append("$set", new BasicDBObject().append("bookmarks", existingbookmarks));
							usercollection.update(whereuserQuery, newDocument);
						}
						WriteResult result = bookcollection.remove(whereQuery);
						String error = result.getError();
						if (error != null) {
							throw new IOException("Error deleting the bookmark with emailid " + email + error);
						}
					}else{
						return "{\"Failed\": \"User doesnt exist\"}";
					}
					return "{\"Sucess\": \"Bookmark deleted successfully\"}";
				}
			}
			return "{\"Failed\": \"Internal error either bookmark doesnt exist or error in deletion\"}";
	}
	
	private DBCollection getBookmarkCollection() {
		DB db = mongoClient.getDB(DatabaseConstants.DATABASE_NAME);
		DBCollection collection = db.getCollection(DatabaseConstants.BOOKMARK_TABLE_NAME);
		return collection;
	}
	private DBCollection getUserCollection() {
		DB db = mongoClient.getDB(DatabaseConstants.DATABASE_NAME);
		DBCollection collection = db.getCollection(DatabaseConstants.USER_TABLE_NAME);
		return collection;
	}
	private DBCollection gettrustCollection() {
		DB db = mongoClient.getDB(DatabaseConstants.DATABASE_NAME);
		DBCollection collection = db.getCollection(DatabaseConstants.TRUSTSCORE_TABLE_NAME);
		return collection;
	}

}
