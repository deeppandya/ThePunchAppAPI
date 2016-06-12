package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import constants.TableNameMapping;
import schema.CompanySchema;

public class DAO {
	
	private static String collection = TableNameMapping.CLASS_TO_TABLENAME_MAPPING.get(CompanySchema.class);
	
	public static CompanySchema getCompanyByCredentials(String email, String password){
		MongoDatabase db = MongoClientSingleton.getMongoClientInstance();
		
		//String collection = TableNameMapping.CLASS_TO_TABLENAME_MAPPING.get(UserTableSchema.class);
		
		HashMap<String, String> querymap = new HashMap();
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
		List<String> returnList = new ArrayList<String>();
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
	
	public static void updateIsActive(String email, int isActive){
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
	
	public static boolean addTask(String email, Map<String, String> taskmap){
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
				return true;
			}else	//email doesn't exist
				return false;
		}catch(MongoException e){
			//TODO log error
			return false;
		}
	}
	
	public static boolean addEmployee(String email, Map<String, String> empmap){
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
				return true;
			}else	//invalid credentials
				return false;
			
		}catch(MongoException e){
			//TODO log error
			return false;
		}
		
	}
	
	public static HashMap<String, String> getEmployeesMap(String email){
		MongoDatabase db = MongoClientSingleton.getMongoClientInstance();
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
		MongoDatabase db = MongoClientSingleton.getMongoClientInstance();
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
	
}
