package Util;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import constants.Constants;

public class MongoClientSingleton {
	private static MongoClient client = null;

	public static synchronized MongoDatabase getMongoClientInstance() {
	    try {
	    	if (client == null) {
	    		client = new MongoClient(Constants.HOST, Constants.MONGO_PORT);
		    }       
		    return client.getDatabase(Constants.DATABASENAME);
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
