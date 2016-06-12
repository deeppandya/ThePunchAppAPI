package api;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("practice")
public class Practice01 {
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Jersey say punch practice: " + msg;
 
		return Response.status(200).entity(output).build();
 
	}
}
