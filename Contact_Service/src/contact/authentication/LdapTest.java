package contact.authentication;

import static org.junit.Assert.*;

import javax.naming.NamingException;

import org.junit.Test;

public class LdapTest {
	
	@Test
	public void authenticateUserSuccessfully() throws NamingException{
		boolean result = Ldap.authenticateUser("test", "test");
		assertEquals(result, "true");
	}
}
