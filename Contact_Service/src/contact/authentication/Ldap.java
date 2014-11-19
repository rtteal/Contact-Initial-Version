package contact.authentication;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class Ldap  {

	public static boolean authenticateUser(String uid, String password) throws NamingException {
		Hashtable<String,String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:10389/");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");


		DirContext ctx = null;

		try {            
			// Step 1: Bind anonymously
			// TODO change this before going to production...
			ctx = new InitialDirContext(env);

			// Step 2: Search the directory
			String base = "o=contact";
			String filter = "(&(objectClass=inetOrgPerson)(uid={0}))";           
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			ctls.setReturningAttributes(new String[0]);
			ctls.setReturningObjFlag(true);
			NamingEnumeration<SearchResult> enm = ctx.search(base, filter, new String[] { uid }, ctls);

			String dn = null;

			if (enm.hasMore()) {
				SearchResult result =  enm.next();
				dn = result.getNameInNamespace();
			}

			if (dn == null || enm.hasMore()) {
				// uid not found or not unique
				return false;
			}

			// Step 3: Bind with found DN and given password
			ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
			ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
			// Perform a lookup in order to force a bind operation with JNDI
			ctx.lookup(dn);
			enm.close();
			return true;
		} catch (NamingException e) {
			System.out.println(e.getMessage());
		} finally {
			ctx.close();
		}
		return false;
	}
}
