package api;

import javax.ws.rs.Path;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import Util.DAO;
import schema.CompanySchema;
import schema.EntrySchema;

@Path("/entry")
public class Entry {
	
	@POST
	@Path("/insert")
	@Consumes("application/json")
	@Produces("application/json")
	public Response insertEntry(String input){
		
		int status = 200;
		JsonObject json = new JsonObject();
		try{
			
			EntrySchema entry = new Gson().fromJson(input, EntrySchema.class);
			CompanySchema company;
			if((company = DAO.getCompanyByCredentials(entry.getCompanyEmail(), null)) != null){
				
				if ( company.getEmployees().containsKey(entry.getEmployeeId()) && 
						company.getTasks().containsKey(entry.getTaskId()) ) {
					if(DAO.insertEntry(entry))
						json.addProperty("data", true);
					else
						json.addProperty("error", "exception occurred while inserting");				
				}else{
					status = 400;
					json.addProperty("error", "invalid employee id or task id");
				}	
			}else{
				status = 400;
				json.addProperty("error", "invalid company email");
			}	
		}catch(JsonSyntaxException e){
			//TODO log error
			status = 400;
			json.addProperty("error", "invalid input json");
		}
		return Response.status(status).entity(json.toString()).build();
	}
	
	
}
