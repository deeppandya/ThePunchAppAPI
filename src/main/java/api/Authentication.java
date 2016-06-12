package api;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.DAO;
import schema.CompanySchema;;

@Path("/auth")
public class Authentication {
	
	@POST
	@Path("/login")
	@Produces("application/json")
	public Response loginUser(@FormParam("email") String email, @FormParam("password") String password){
		
		JsonObject json = new JsonObject();
		
		if (email==null || password==null){
			json.addProperty("error", "invalid parameters");
			return Response.status(400).entity(json.toString()).build();
		} 
			
		CompanySchema user = DAO.getCompanyByCredentials(email, password);
		json.addProperty("error", "invalid user credentials");
		
		if (user!=null) 
			DAO.updateIsActive(user.getEmail(), 1);	//1 = active
		
		return ( (user==null) ? (Response.status(400).entity(json.toString()).build()) 
				: (Response.ok().entity(new Gson().toJson(user)).build()) );
	}
	
	@POST
	@Path("/logout")
	@Produces("application/json")
	public Response logoutUser(@FormParam("email") String email, @FormParam("password") String password){
		
		JsonObject json = new JsonObject();
		
		if (email==null || password==null){
			json.addProperty("error", "invalid parameters");
			return Response.status(400).entity(json.toString()).build();
		} 
			
		CompanySchema user = DAO.getCompanyByCredentials(email, password);
		
		if (user!=null){ 
			DAO.updateIsActive(user.getEmail(), 0);	//1 = not active
			json.addProperty("success", "Okay");
		}
		else
			json.addProperty("error", "invalid user credentials");
		
		return ( (user==null) ? (Response.status(400).entity(json.toString()).build()) 
				: (Response.ok().entity(json.toString()).build()) );
	}
	
}
