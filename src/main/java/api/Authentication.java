package api;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Util.Authenticator;
import Util.DAO;
import Util.Helper;
import constants.Constants;
import schema.CompanySchema;;

@Path("/auth")
public class Authentication {
	
	Authenticator authenticator = Authenticator.getInstance();
	JsonParser parser = null;
	@POST
	@Path("/login")
	@Consumes("application/json")
	@Produces("application/json")
	public Response loginUser(String input, @HeaderParam("User-Agent") String useragent){
		int status = 200;
		JsonObject json = new JsonObject();
		String authToken = null;
		JsonParser parser = new JsonParser();
		try{
			JsonObject jsonobj = parser.parse(input).getAsJsonObject();
			
			JsonElement email = ( (jsonobj.has("email")) ) ? jsonobj.get("email") : null ;
			JsonElement pwd = ( (jsonobj.has("password")) ) ? jsonobj.get("password") : null ;
			
			if ( email != null && pwd != null && email.getAsString() != null && pwd.getAsString() != null && 
					!email.getAsString().equals("") && !pwd.getAsString().equals("") ){
				
				CompanySchema user = DAO.getCompanyByCredentials(email.getAsString(), pwd.getAsString());	
				System.out.println(new Gson().toJson(user));
				if (user!=null){ 
					DAO.updateIsActive(user.getEmail(), 1);	//1 = active
					
					//if its browser then generate session id
					if(Helper.isBrowser(useragent)){
						Authenticator authenticator = Authenticator.getInstance();
						authToken = authenticator.generateAuthToken(user.getEmail());
						return Response.ok().entity(new Gson().toJson(user))
								.cookie(new NewCookie("sessionId", authToken,Constants.COOKIE_PATH,
										Constants.COOKIE_DOMAIN,null,Constants.COOKIE_AGE,false))
								.build();
					}else{	//not a browser call, from mobile app, not session id
						return Response.ok().entity(new Gson().toJson(user)).build();
					}
				}else{
					status = 400;
					json.addProperty("error", "invalid user credentials");
				}
			}else{
				json.addProperty("error", "invalid parameters");
				status = 400;
			}
		}catch(Exception e){
			//TODO log error
			e.printStackTrace();
			json.addProperty("data", e.getMessage());	
		}
		
		return Response.status(status).entity(json.toString()).build();
		
	}
	
	@POST
	@Path("/logout")
	@Produces("application/json")
	@Consumes("application/json")
	public Response logoutUser(String input, @Context HttpHeaders headers, @HeaderParam("User-Agent") String useragent){
		System.out.println("inside logout");
		int status = 200;
		String sessionId = null;
		
		JsonObject returnJson = new JsonObject();
		parser = new JsonParser();
		try{
			JsonObject jsonobj = parser.parse(input).getAsJsonObject();
			JsonElement email = ( (jsonobj.has("email")) ) ? jsonobj.get("email") : null ;
			
			CompanySchema user = null;
			
			if ( email != null && email.getAsString() != null && 
					((user = DAO.getCompanyByCredentials(email.getAsString(), null)) != null) ){
				
				//check if it's from browser
				if(Helper.isBrowser(useragent)){
					//check if header has session id AND if its valid
					Authenticator authenticator = Authenticator.getInstance();
					if ( headers.getCookies().containsKey("sessionId") && 
							authenticator.isAuthTokenValid(sessionId = headers.getCookies().get("sessionId").getValue())){
						
						authenticator.removeAuthToken(sessionId);
						DAO.updateIsActive(user.getEmail(), 0);	//1 = not active
						returnJson.addProperty("data", true);
					}
					else
						return Response.status(401).build();
					
				}else{	//request from mobile
					DAO.updateIsActive(user.getEmail(), 0);	//1 = not active
					returnJson.addProperty("data", true);
				}
				
			}
			else{	//if email==null
				returnJson.addProperty("error", "invalid email");
				status = 400;
			}
		}catch(Exception e){
			//TODO log error
			e.printStackTrace();
			status = 400;
			returnJson.addProperty("data", e.getMessage());	
		}
		return Response.status(status).entity(returnJson.toString()).build();
	}
	
}
