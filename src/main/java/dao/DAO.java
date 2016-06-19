package dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import constants.TableNameMapping;
import schema.CompanySchema;
import schema.EntrySchema;

public class DAO {
	
	public static CompanySchema getCompanyByCredentials(String email, String password){
		String collection = TableNameMapping.CLASS_TO_TABLENAME_MAPPING.get(CompanySchema.class);
		MongoDatabase db = MongoClientSingleton.getMongoClientInstance();
		
		//String collection = TableNameMapping.CLASS_TO_TABLENAME_MAPPING.get(UserTableSchema.class);
		
		HashMap<String, String> querymap = new HashMap<String, String>();
		querymap.put("email", email);
		
		//If we want to fetch company by just email then password will be null
		if (password != null) 
			querymap.put("password", CompanySchema.md5(password));
		
		String json = new Gson().toJson(querymap);
		
		Document query = Document.parse(json);
		FindIterable<Document> result = db.getCollection(collection).find(query).limit(1);
		
		Document doc = result.first();
		if( doc == null )
			return null;
		CompanySchema company = null;
		try{
			company = new Gson().fromJson(doc.toJson(), CompanySchema.class);
			if(company == null)
				return null;
		}catch(JsonParseException e){
			//TODO log error
			return null;
		}
		
		return company;
	}
	
	public static boolean checkIfEmailExist(String email){
		String collection = TableNameMapping.CLASS_TO_TABLENAME_MAPPING.get(CompanySchema.class);
		MongoDatabase db = MongoClientSingleton.getMongoClientInstance();
		
		Bson filter = Filters.eq("email", email);
		long result = 0;
		try{
			result = db.getCollection(collection).count(filter);
		}catch(MongoException e){
			//TODO log error
			return false;
		}
		//System.out.println("result: "+result);
		/*iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {
				if (doc != null) 
					returnList.add(doc.getString("username"));
			}
		});*/
		return (result == 1) ? true : false;
	}
	
	public static boolean insertCompany(CompanySchema user){
		String collection = TableNameMapping.CLASS_TO_TABLENAME_MAPPING.get(CompanySchema.class);
		MongoDatabase db = MongoClientSingleton.getMongoClientInstance();
		try{
			user.setPassword(CompanySchema.md5(user.getPassword()));
			db.getCollection(collection).insertOne(Document.parse(new Gson().toJson(user)));
		}catch(MongoException e){
			//TODO log error	
			return false;
		}
		return true;
	}
	
	public static boolean insertEntry(EntrySchema entry){
		String collection = TableNameMapping.CLASS_TO_TABLENAME_MAPPING.get(EntrySchema.class);
		MongoDatabase db = MongoClientSingleton.getMongoClientInstance();
		try{
			//user.setPassword(CompanySchema.md5(user.getPassword()));
			db.getCollection(collection).insertOne(Document.parse(new Gson().toJson(entry)));
		}catch(MongoException e){
			//TODO log error	
			return false;
		}
		return true;
	}
	
	public static void updateIsActive(String email, int isActive){
		String collection = TableNameMapping.CLASS_TO_TABLENAME_MAPPING.get(CompanySchema.class);
		MongoDatabase db = MongoClientSingleton.getMongoClientInstance();
		try{
			
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set", new BasicDBObject().append("isActive", isActive));
			BasicDBObject searchQuery = new BasicDBObject().append("email", email);
			
			db.getCollection(collection).updateOne(searchQuery, newDocument);
		}catch(MongoException e){
			//TODO log error	
			return;
		}		
	}
	
	public static HashMap<String, String> addTask(String email, Map<String, String> taskmap){
		String collection = TableNameMapping.CLASS_TO_TABLENAME_MAPPING.get(CompanySchema.class);
		MongoDatabase db = MongoClientSingleton.getMongoClientInstance();
		HashMap<String, String> tasks =null;
		try{
			CompanySchema company = getCompanyByCredentials(email, null);
			if (company != null) {
				tasks = company.getTasks();
				tasks.putAll(taskmap);
				
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append("$set", new BasicDBObject().append("tasks", tasks));
				BasicDBObject searchQuery = new BasicDBObject().append("email", email);
				
				db.getCollection(collection).updateOne(searchQuery, newDocument);
				return tasks;
			}else	//email doesn't exist
				return null;
		}catch(MongoException e){
			//TODO log error
			return null;
		}
	}
	
	public static HashMap<String, String> addEmployee(String email, Map<String, String> empmap){
		String collection = TableNameMapping.CLASS_TO_TABLENAME_MAPPING.get(CompanySchema.class);
		MongoDatabase db = MongoClientSingleton.getMongoClientInstance();
		
		HashMap<String, String> employees =null;
		try{
			CompanySchema company = getCompanyByCredentials(email, null);
			if (company != null) {
				employees = company.getEmployees();
				employees.putAll(empmap);
				
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append("$set", new BasicDBObject().append("employees", employees));
				BasicDBObject searchQuery = new BasicDBObject().append("email", email);
				
				db.getCollection(collection).updateOne(searchQuery, newDocument);
				return employees;
			}else	//email doesn't exist
				return null;
			
		}catch(MongoException e){
			//TODO log error
			return null;
		}
		
	}
	
	public static HashMap<String, String> getEmployeesMap(String email){
		try{
			CompanySchema company = getCompanyByCredentials(email, null);
			if (company != null) {
				return company.getEmployees();
			}else	//invalid credentials
				return null;
			
		}catch(MongoException e){
			//TODO log error
			return null;
		}
	}
	
	public static HashMap<String, String> getTasksMap(String email){
		try{
			CompanySchema company = getCompanyByCredentials(email, null);
			if (company != null) {
				return company.getTasks();
			}else	//invalid credentials
				return null;
			
		}catch(MongoException e){
			//TODO log error
			return null;
		}
	}
	
	/**
     * Encodes the byte array into base64 string
     *
     * @param imageByteArray - byte array
     * @return String a {@link java.lang.String}
     */
    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeBase64URLSafeString(imageByteArray);
    }
 
    /**
     * Decodes the base64 string into byte array
     *
     * @param imageDataString - a {@link java.lang.String}
     * @return byte array
     */
    public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }
	
}
