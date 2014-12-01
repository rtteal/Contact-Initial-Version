package contact.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
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
import javax.ws.rs.core.Application;
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

@ApplicationPath("/")
@Path("/")
public class Contact extends Application {
	@Context 
	private ServletContext sctx;  // dependency injection
	private static CassandraDS ds = new CassandraDS();
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Updates the user's contact info.
	 * @param user
	 * @return
	 */
	@POST
	@Path("/update-profile")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProfile(User user){
		logger.debug(user);
		return Response.ok().entity(user).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/accept-contact")
	public Response acceptContact(AcceptContact ac) {
		logger.debug("accept: " + 
				ds.acceptContact(ac.userName, ac.otherUserName));
		return Response.ok().entity(ac).build();
	} 
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/decline-contact")
	public Response declineContact(AcceptContact ac) {
		logger.debug("decline: " + 
				ds.declineContact(ac.userName, ac.otherUserName));
		return Response.ok().entity(ac).build();
	} 
	
	@GET
	@Path("/login/{userName: \\w+}")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response login(
			@PathParam("userName") String userName,
			@QueryParam("password") String password) {
		logger.info("pass: " + password);
		if (!"test".equals(password))
			return Response.serverError().build();
		return Response.ok(ds.getUserById(userName),
				MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/profile/{userName: \\w+}")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response profile(
			@PathParam("userName") String userName) {
		return Response.ok(ds.getUserById(userName),
				MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * @param userName
	 * @return The contacts wanting to connect with the user.
	 */
	@GET
	@Path("/incoming-requests/{userName: \\w+}")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response incomingRequests(
			@PathParam("userName") String userName) {
		return Response.ok(ds.getIncomingRequests(userName),
				MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * @param userName
	 * @return The user's contacts.
	 */
	@GET
	@Path("/address-book/{userName: \\w+}")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response addressBook(
			@PathParam("userName") String userName) {
		return Response.ok(ds.getAddressBook(userName),
				MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/create-user")
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
	@Path("/create-account")
	public Response createAccount(
			@FormParam("userName") String userName,	
			@FormParam("password") String password){
		return Response.ok(ds.createUser(userName, password),
				MediaType.TEXT_PLAIN_TYPE).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/request-contact")
	public Response requestContact(
			@FormParam("userName") String userName,
			@FormParam("otherUserName") String otherUserName) {
		return Response.ok(String.valueOf(ds.requestContact(userName,
				otherUserName)),
				MediaType.TEXT_PLAIN_TYPE).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Path("/delete-user")
	public Response deleteUser(
			@FormParam("userName") String userName) {
		return Response.ok(ds.deleteUser(userName),
				MediaType.TEXT_PLAIN_TYPE).build();
	}
	
	@Override
    public Set<Class<?>> getClasses() {
	Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(Contact.class);
        return set;
    }
	
	public static class AcceptContact{
		public String userName;
		public String otherUserName;
		
		public AcceptContact(){
			
		}
		
		public AcceptContact(String userName, String otherUserName){
			this.userName = userName;
			this.otherUserName = otherUserName;
		}
		
		@Override
		public String toString(){
			return "[userName: " + userName
			+ ", otherUserName: " + otherUserName + "]";
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getOtherUserName() {
			return otherUserName;
		}

		public void setOtherUserName(String otherUserName) {
			this.otherUserName = otherUserName;
		}
	}
}
