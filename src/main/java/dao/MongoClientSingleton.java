package dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import constants.DatabaseConstants;

public class MongoClientSingleton {
	private static MongoClient client = null;

	public static synchronized MongoDatabase getMongoClientInstance() {
	    try {
	    	if (client == null) {
	    		client = new MongoClient(DatabaseConstants.HOST, DatabaseConstants.PORT);
		    }       
		    return client.getDatabase(DatabaseConstants.DATABASENAME);
		} catch (Exception e) {
			return null;
		}   
	}
	
	public static void closeMongoClientInstance(){
		try {
			if (client!=null) {
				client.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
