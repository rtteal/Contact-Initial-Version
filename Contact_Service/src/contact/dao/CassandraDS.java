package contact.dao;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;

import contact.model.AddressBook;
import contact.model.IncomingRequests;
import contact.model.User;

import org.apache.log4j.Logger;

/**
 * @author rtteal
 *
 */
public class CassandraDS {
	private static final String host = "127.0.0.1";
	private static final Cluster cluster = Cluster.builder()
			.addContactPoint(host)
			.withLoadBalancingPolicy(new DCAwareRoundRobinPolicy("datacenter1"))
			.build();
	private static final Session session = cluster.connect("contact");

	private static final PreparedStatement createUser = session.prepare(
			"insert into users (username, password) "
					+ "values ( ?, ? ) "
					+ "IF NOT EXISTS;");
	
	private static final PreparedStatement updateUser = session.prepare(
			"update users set email = ?, fname = ?, lname = ?,"
			+ " phone = ? where username = ?;");

	private static final PreparedStatement addressBook = session.prepare(
			"SELECT * FROM address_book where username = ? "
					+ "and authorized = ?;");

	private static final PreparedStatement users = session.prepare(
			"SELECT * FROM users where username = ? ;");

	private static final PreparedStatement acceptRequest = session.prepare(
			"update address_book set authorized = true "
					+ "where username = ? and contact = ?;");

	private static final PreparedStatement contactRequest = session.prepare(
			"insert into address_book (username, authorized,"
					+ " contact, fname, lname, email, phone) values ("
					+ "?, ?, ?, ?, ?, ?, ?) IF NOT EXISTS;");
	
	private static final PreparedStatement deleteUser1 = session.prepare(
			"delete from address_book where username = ?;");
	
	private static final PreparedStatement deleteUser2 = session.prepare(
			"delete from users where username = ?;");
	
	private static final PreparedStatement allContacts = session.prepare(
			"select contact from address_book where username = ?;");
	
	private static final PreparedStatement removeContact = session.prepare(
			"delete from address_book where username = ? and contact = ?;"); 
	

	private static Logger logger = Logger.getLogger(CassandraDS.class);
	
	public CassandraDS(){
		init();
	}
	
	private void init(){
		// attempting to prevent memory leaks with this
		// probably unnecessary...
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				close();
			}
		});
	}
	
	public String createUser(String userName, String password){
		if (userName.length() == 0 || password.length() == 0)
			throw new IllegalArgumentException("userName and password must be populated");

		BoundStatement boundStatement = new BoundStatement(
				createUser);
		boundStatement.bind(userName, password);
		ResultSet rset = session.execute(boundStatement);
		Row row = rset.one();
		if (row.getBool(0))
			return "Success";

		return "Fail";
	}

	/**
	 * updates changes to user's contact info
	 * @param user
	 * @return
	 */
	public String updateUser(User user){
		if (user.userName.length() == 0)
			throw new IllegalArgumentException("userName must be populated");

		BoundStatement boundStatement = new BoundStatement(
				updateUser);
		boundStatement.bind(user.email,
				user.fName,
				user.lName,
				user.tel,
				user.userName);
		ResultSet rset = session.execute(boundStatement);

		return "Success";
	}
	
	/**
	 * Returns the users who are connected to this user
	 * @param userName
	 * @return
	 */
	public AddressBook getAddressBook(String userName){
		if (userName.length() == 0)
			throw new IllegalArgumentException("userName must be populated");

		return new AddressBook(
				getAddressBookOrIncomingRequests(userName, true));
	}

	/**
	 * Returns the users who want to share contact info with this user
	 * @param userName 
	 * @return
	 */
	public IncomingRequests getIncomingRequests(String userName){
		if (userName.length() == 0)
			throw new IllegalArgumentException("userName must be populated");

		return new IncomingRequests(
				getAddressBookOrIncomingRequests(userName, false));
	}

	private List<User> getAddressBookOrIncomingRequests (String userName,
			boolean isIncomingRequest){
		List<User> requests = new ArrayList<>();

		BoundStatement boundStatement = new BoundStatement(addressBook);
		boundStatement.bind(userName, isIncomingRequest);
		ResultSet results = session.execute(boundStatement);
		for (Row row : results){
			requests.add(new User(row.getString("contact"),
					row.getString("fname"),
					row.getString("lname"),
					row.getString("email"),
					row.getString("phone"),
					row.getString("photo")));
		}

		return requests;
	}

	/**
	 * gets a user
	 * @param userName of requested user
	 * @return
	 */
	public User getUserById(String userName){
		if (userName.length() == 0)
			throw new IllegalArgumentException("userName must be populated");

		User u = null;

		BoundStatement boundStatement = new BoundStatement(users);
		boundStatement.bind(userName);
		ResultSet results = session.execute(boundStatement);
		for (Row row : results){
			u = new User(row.getString("username"),
					row.getString("fname"),
					row.getString("lname"),
					row.getString("email"),
					row.getString("phone"),
					row.getString("photo"));
		}

		return u;
	}

	/**
	 * creates request for otherUserName to accept or reject
	 * @param userName user making request
	 * @param otherUserName user who's contact info is being requested
	 * @return whether the request was created successfully or not
	 */
	public boolean requestContact(String userName, String otherUserName){
		if (userName.length() == 0 || otherUserName.length() == 0)
			throw new IllegalArgumentException();

		User requestingUser =  getUserById(userName);
		if (requestingUser == null)
			throw new IllegalStateException("requesting user not found: " + userName);

		User requestedUser =  getUserById(otherUserName);
		if (requestedUser == null)
			throw new IllegalStateException("requesting user not found: " + otherUserName);

		boolean result = false;

		BoundStatement boundStatement = new BoundStatement(
				contactRequest);
		boundStatement.bind(otherUserName,
				true,
				requestingUser.userName,
				requestingUser.fName,
				requestingUser.lName,
				requestingUser.email,
				requestingUser.tel);
		ResultSet rset = session.execute(boundStatement);
		Row row = rset.one();
		result = row.getBool(0);

		boundStatement = new BoundStatement(
				contactRequest);
		boundStatement.bind(userName,
				true,
				requestedUser.userName,
				requestedUser.fName,
				requestedUser.lName,
				requestedUser.email,
				requestedUser.tel);
		rset = session.execute(boundStatement);
		row = rset.one();
		// if the previous write failed, 
		// still want to return false
		if (result) result = row.getBool(0);

		return result;
	}

	/**
	 * Users are now authorized to see eachother's info
	 * @param userName
	 * @param otherUserName
	 * @return "Success" if the transaction completed
	 */
	public String acceptContact(String userName, String otherUserName){
		if (userName.length() == 0 || otherUserName.length() == 0)
			throw new IllegalArgumentException();

		BoundStatement boundStatement = new BoundStatement(acceptRequest);
		boundStatement.bind(userName, otherUserName);
		session.execute(boundStatement);

		boundStatement = new BoundStatement(acceptRequest);
		boundStatement.bind(otherUserName, userName);
		session.execute(boundStatement);

		return "Success";
	}
	
	/**
	 * 
	 * @param userName of user revoking otherUserName's authorization
	 * @param otherUserName person losing userName's contact info
	 * @return "Success" if the transaction completed
	 */
	public String declineContact(String userName, String otherUserName){
		if (userName.length() == 0 || otherUserName.length() == 0)
			throw new IllegalArgumentException();

		BoundStatement boundStatement = new BoundStatement(removeContact);
		boundStatement.bind(userName, otherUserName);
		session.execute(boundStatement);
		
		boundStatement = new BoundStatement(removeContact);
		boundStatement.bind(otherUserName, userName);
		session.execute(boundStatement);
		
		return "Success";
	}
	
	/**
	 * delete all trace of user
	 * @param userName to be deleted
	 * @return "Success" if the transaction completed
	 */
	public String deleteUser(String userName){
		if (userName.length() == 0)
			throw new IllegalArgumentException();

		BoundStatement boundStatement = new BoundStatement(allContacts);
		boundStatement.bind(userName);
		ResultSet contacts = session.execute(boundStatement);
		
		for (Row contactUserName : contacts){
			boundStatement = new BoundStatement(removeContact);
			boundStatement.bind(contactUserName.getString(0), userName);
			session.execute(boundStatement);
		}
		
		boundStatement = new BoundStatement(deleteUser1);
		boundStatement.bind(userName);
		session.execute(boundStatement);
		
		boundStatement = new BoundStatement(deleteUser2);
		boundStatement.bind(userName);
		session.execute(boundStatement);
		
		return "Success";
	}

	public static void close() {
		logger.info("Closing Cassandra, " + cluster.getClusterName());
		cluster.close();
	}

}
