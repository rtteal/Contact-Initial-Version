package contact.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import org.apache.log4j.Logger;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.servlet.ServletContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import contact.dao.CassandraDS;
import contact.model.AddressBook;
import contact.model.User;

@Path("/")
public class ContactService {
	@Context 
	private ServletContext sctx;  // dependency injection
	private static CassandraDS ds = new CassandraDS();
	private Logger logger = Logger.getLogger(this.getClass());
	
	@POST
	@Path("/updateProfile")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProfile(User user){
		String output = user.toString();
		logger.info(output); 
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/login/{userName: \\w+}")
	@Produces({MediaType.APPLICATION_XML}) 
	public Response login(
			@PathParam("userName") String userName,
			@QueryParam("password") String password) {
		System.out.println("pass: " + password);
		if (!"test".equals(password))
			return Response.serverError().build();
		return Response.ok(ds.getUserById(userName),
				MediaType.APPLICATION_XML).build();
	}
	
	@GET
	@Path("/profile/{userName: \\w+}")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response profile(
			@PathParam("userName") String userName) {
		return Response.ok(ds.getUserById(userName),
				MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/incomingRequests/{userName: \\w+}")
	@Produces({MediaType.APPLICATION_XML}) 
	public Response incomingRequests(
			@PathParam("userName") String userName) {
		return Response.ok(ds.getIncomingRequests(userName),
				MediaType.APPLICATION_XML).build();
	}
	
	@GET
	@Path("/addressBook/{userName: \\w+}")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response addressBook(
			@PathParam("userName") String userName) {
		return Response.ok(ds.getAddressBook(userName),
				MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/updateUser")
	public Response create(
			@FormParam("userName") String userName,	
			@FormParam("fName") String fName,
			@FormParam("lName") String lName,
			@FormParam("email") String email,
			@FormParam("tel") String tel){
		// TODO figure out how to add photos
		User user = new User(userName, fName, lName, email, tel, "");
		return Response.ok(ds.updateUser(user),
				MediaType.TEXT_PLAIN_TYPE).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/createAccount")
	public Response createAccount(
			@FormParam("userName") String userName,	
			@FormParam("password") String password){
		return Response.ok(ds.createUser(userName, password),
				MediaType.TEXT_PLAIN_TYPE).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/requestContact")
	public Response requestContact(
			@FormParam("userName") String userName,
			@FormParam("otherUserName") String otherUserName) {
		return Response.ok(String.valueOf(ds.requestContact(userName,
				otherUserName)),
				MediaType.TEXT_PLAIN_TYPE).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/acceptContact")
	public Response acceptContact(
			@FormParam("userName") String userName,
			@FormParam("otherUserName") String otherUserName) {
		return Response.ok(ds.acceptContact(userName, otherUserName),
				MediaType.TEXT_PLAIN_TYPE).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/removeContact")
	public Response removeContact(
			@FormParam("userName") String userName,
			@FormParam("otherUserName") String otherUserName) {
		return Response.ok(ds.removeContact(userName, otherUserName),
				MediaType.TEXT_PLAIN_TYPE).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/deleteUser")
	public Response deleteUser(
			@FormParam("userName") String userName) {
		return Response.ok(ds.deleteUser(userName),
				MediaType.TEXT_PLAIN_TYPE).build();
	}
}
