package api;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

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

import org.bson.types.ObjectId;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.DB;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSDownloadByNameOptions;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import Util.DAO;
import Util.MongoClientSingleton;
import schema.CompanySchema;
import schema.EntrySchema;


public class Practice01 {
	public static void main(String[] args) throws IOException{
		MongoDatabase db = MongoClientSingleton.getMongoClientInstance();
		
		
		System.out.println("start");
		// Reading a Image file from file system
		/*File file = new File("E://myself_resize.jpg");
		FileInputStream imageInFile = new FileInputStream(file);
		byte imageData[] = new byte[(int) file.length()];
		imageInFile.read(imageData);
		
		// Converting Image byte array into Base64 String
		String imageDataString = encodeImage(imageData);
		Entry entry = new Entry();
		entry.setImageId("1");
		entry.setImage(imageDataString);
		
		db.getCollection("images").insertOne(Document.parse(new Gson().toJson(entry)));
		MongoClientSingleton.closeMongoClientInstance();
		imageInFile.close();*/
		
		// Converting a Base64 String into Image byte array
		
		/*HashMap<String, String> querymap = new HashMap();
		querymap.put("imageId", "1");
		
		String json = new Gson().toJson(querymap);
		
		Document query = Document.parse(json);
		
		FindIterable<Document> result = db.getCollection("images").find(query).limit(1);
    	Document doc = result.first();
    	EntrySchema entry = new Gson().fromJson(doc.toJson(), EntrySchema.class);
    	System.out.println(entry.getImage());
    	byte[] imageByteArray = DAO.decodeImage(entry.getImage());
 
        // Write a image byte array into file system
    	FileOutputStream imageOutFile = new FileOutputStream(
        "E://myself_resize1.jpg");
 
        imageOutFile.write(imageByteArray);
        imageOutFile.close();
        MongoClientSingleton.closeMongoClientInstance();
       	System.out.println("end");*/
			
	}
	
}
