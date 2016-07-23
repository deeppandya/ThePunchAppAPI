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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Util.Authenticator;
import Util.DAO;
import Util.Helper;
import schema.CompanySchema;
 
@Path("/hello")
public class HelloWorldService {
	
	Authenticator authenticator = Authenticator.getInstance();
	
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg, @Context HttpHeaders headers) {
		
		String output = "Jersey say punch: " + msg;
		System.out.println("inside hello");
		
		//check if authorized
		if ( !(headers.getCookies().containsKey("sessionId")) 
				|| !(authenticator.isAuthTokenValid(headers.getCookies().get("sessionId").getValue())) ) {
			return Response.status(401).build();
		}
		return Response.ok().build();
 
	}

	@POST
	@Path("/trypost")
	@Consumes("application/json")
	@Produces("application/json")
	public Response loginUser(String input, @HeaderParam("User-Agent") String useragent){
		System.out.println(useragent);
		
		JsonObject json = new JsonObject();
		JsonParser parser = new JsonParser();
		try{
			JsonObject jsonobj = parser.parse(input).getAsJsonObject();
			
			JsonElement email = ( (jsonobj.has("email")) ) ? jsonobj.get("email") : null ;
			JsonElement pwd = ( (jsonobj.has("password")) ) ? jsonobj.get("password") : null ;
			
			if (!(email.getAsString() == null || pwd.getAsString() == null)){
				System.out.println(email.getAsString());
				System.out.println(pwd.getAsString());
				json.addProperty("data", "okay");
				return Response.ok().entity(json.toString()).build();
			}
		}catch(Exception e){
			json.addProperty("data", "exception");
			return Response.status(400).entity(json.toString()).build();
		}
		System.out.println("sending from trypost");
		json.addProperty("data", "not okay");
		return Response.status(400).entity(json.toString()).build();
	}
	
	@GET
	@Path("/tryHeader")
	public Response getHeader(@HeaderParam("username") String username, @HeaderParam("User-Agent") String useragent){
		System.out.println("username is:: "+username); //will be null if not
		System.out.println("useragent is:: "+useragent);
		Helper.isBrowser(useragent);
		return Response.ok().build();
	}
	
	@GET
    @Path("getallheader")
    public Response getAllHeader(@Context HttpHeaders httpHeaders){
 
        // local variables
        StringBuffer stringBuffer = new StringBuffer();
        String headerValue = "";
 
        for(String header : httpHeaders.getRequestHeaders().keySet()) {
            headerValue = httpHeaders.getRequestHeader(header).get(0);
            stringBuffer.append(header + ": " + headerValue + "\n");
        }
        return Response.status(200).entity(stringBuffer.toString()).build();
    }
}