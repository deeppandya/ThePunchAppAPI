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

@Path("/company")
public class Company {
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCompany(String input){
		int status=200;
		JsonObject json = new JsonObject();
		if (input.equals("")){ 
			json.addProperty("error", "invalid request");
			status = 400;
		}
		try{
			CompanySchema company = new Gson().fromJson(input, CompanySchema.class);
			if (company != null){
				if(!DAO.checkIfEmailExist(company.getEmail()))
				{
					if(company.getName()==null || company.getPassword()==null  
							|| company.getEmail()==null || company.getAddress()==null ){
						json.addProperty("error", "not enough parameters");
						status = 400;
					}
					
					if(DAO.insertCompany(company)){
						return Response.ok().entity(new Gson().toJson(company)).build();
					}
					else{
						status = 400;
						json.addProperty("error", "db insertion error");
					}
				}else{
					json.addProperty("error", "company already exists");
					status = 400;
				}
			}else{
				json.addProperty("error", "invalid parameters");
				status = 400;
			}
		}catch(Exception e){
			json.addProperty("error", "json parse exception");
			status = 400;
		}
		return Response.status(status).entity(json.toString()).build();
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
			
			HashMap<String,String> map = new HashMap<String,String>();
			map = (HashMap<String,String>)(new Gson()).fromJson(emp.getAsJsonObject(), map.getClass());
			
			HashMap<String,String> updatedEmployees;
			
			if((updatedEmployees = DAO.addEmployee(email.getAsString(), map)) != null){
				json.addProperty("data", new Gson().toJson(updatedEmployees));
			}
			else{
				status = 400;
				json.addProperty("error", "invalid email or exception during insertion");
			}
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
			
			HashMap<String,String> map = new HashMap<String,String>();
			map = (HashMap<String,String>)(new Gson()).fromJson(task.getAsJsonObject(), map.getClass());
			
			HashMap<String,String> updatedTasks;
			if((updatedTasks = DAO.addTask(email.getAsString(), map)) != null)
				json.addProperty("data", new Gson().toJson(updatedTasks));
			else{
				status = 400;
				json.addProperty("error", "invalid email or exception during insertion");
			}
		}catch(JsonSyntaxException e){
			//TODO log error
			status = 400;
			json.addProperty("error", "invalid input json");
		}
		return Response.status(status).entity(json.toString()).build();
	}
	
	@GET
	@Path("/getEmployee")
	@Produces("application/json")
	public Response getEmployeesMap(@QueryParam("email") String email){
		JsonObject json = new JsonObject();
		if (email==null){
			json.addProperty("error", "invalid parameters");
			return Response.status(400).entity(json.toString()).build();
		}else{
			HashMap<String, String> map = DAO.getEmployeesMap(email);
			if (map == null) {
				json.addProperty("error", "invalid email");
				return Response.status(400).entity(json.toString()).build();
			}
			else{
				json.addProperty("employees", new Gson().toJson(map));
				return Response.ok().entity(new Gson().toJson(map)).build();
			}
		} 
	}
	
	@GET
	@Path("/getTask")
	@Produces("application/json")
	public Response getTasksMap(@QueryParam("email") String email){
		JsonObject json = new JsonObject();
		if (email==null){
			json.addProperty("error", "invalid parameters");
			return Response.status(400).entity(json.toString()).build();
		}else{
			HashMap<String, String> map = DAO.getTasksMap(email);
			if (map == null) {
				json.addProperty("error", "invalid email");
				return Response.status(400).entity(json.toString()).build();
			}
			else{
				json.addProperty("tasks", new Gson().toJson(map));
				return Response.ok().entity(new Gson().toJson(map)).build();
			}
		}
	}
	
}
