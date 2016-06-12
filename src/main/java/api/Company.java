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

import dao.DAO;
import dao.DAO;
import schema.CompanySchema;
import schema.CompanySchema;

@Path("/company")
public class Company {
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCompany(String input){
		JsonObject json = new JsonObject();
		if (input.equals("")){ 
			json.addProperty("data", "invalid request");
			return Response.status(400).entity(json.toString()).build();
		}
		try{
			CompanySchema company = new Gson().fromJson(input, CompanySchema.class);
			if (company != null){
				if(!DAO.checkIfEmailExist(company.getEmail()))
				{
					if(company.getName()==null || company.getPassword()==null  
							|| company.getEmail()==null || company.getAddress()==null ){
						json.addProperty("data", "not enough parameters");
						return Response.status(400).entity(json.toString()).build();
					}
					
					boolean result = DAO.insertCompany(company);
					json.addProperty("data", "db insertion error");
					
					return ( (result) ? (Response.ok().entity(new Gson().toJson(company)).build()) 
							: (Response.status(400).entity(json.toString()).build()) );
				}else{
					json.addProperty("data", "company already exists");
					return Response.status(400).entity(json.toString()).build();
				}
				
			}else{
				json.addProperty("data", "invalid parameters");
				return Response.status(400).entity(json.toString()).build();
			}
				
		}catch(Exception e){
			json.addProperty("data", "json parse exception");
			return Response.status(400).entity(json.toString()).build();
		}
	}
	
	@GET
	@Path("/doesEmailExist")
	@Produces("application/json")
	public Response doesEmailExists(@QueryParam("email") String email){
		int status = 200;
		JsonObject json = new JsonObject();
		if (email==null){
			json.addProperty("error", "invalid parameters");
			status = 400;	
		}else{
			boolean result = DAO.checkIfEmailExist(email);
			json.addProperty("data", result);
		} 
		return Response.status(status).entity(json.toString()).build();
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/addEmployee")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addEmployee(String input){
		int status = 200;
		JsonObject json = new JsonObject();
		JsonParser parser = new JsonParser();
		try{
			JsonObject jsonobj = parser.parse(input).getAsJsonObject();
			
			JsonElement email = jsonobj.get("email");
			JsonElement emp = jsonobj.get("employees");
			
			HashMap<Integer,String> map = new HashMap<Integer,String>();
			map = (HashMap<Integer,String>)(new Gson()).fromJson(emp.getAsJsonObject(), map.getClass());
			boolean result = DAO.addEmployee(email.toString(), map);
			
			json.addProperty("data", result);
		}catch(JsonSyntaxException e){
			//TODO log error
			status = 400;
			json.addProperty("error", "invalid input json");
		}
		return Response.status(status).entity(json.toString()).build();
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/addTask")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addTask(String input){
		int status = 200;
		JsonObject json = new JsonObject();
		JsonParser parser = new JsonParser();
		try{
			JsonObject jsonobj = parser.parse(input).getAsJsonObject();
			
			JsonElement email = jsonobj.get("email");
			JsonElement task = jsonobj.get("tasks");
			
			HashMap<Integer,String> map = new HashMap<Integer,String>();
			map = (HashMap<Integer,String>)(new Gson()).fromJson(task.getAsJsonObject(), map.getClass());
			boolean result = DAO.addTask(email.toString(), map);
			
			json.addProperty("data", result);
		}catch(JsonSyntaxException e){
			//TODO log error
			status = 400;
			json.addProperty("error", "invalid input json");
		}
		return Response.status(status).entity(json.toString()).build();
	}
	
}
