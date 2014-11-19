package contact.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlElementWrapper; 
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name = "addressBook")
public class AddressBook {
	private final List<User> contacts;
	
	public AddressBook (){
		contacts = null;
		//throw new IllegalStateException("IncomingRequsts no-arg"
		//		+ " constructor should not be called");
	}
	
	public AddressBook (List<User> contacts){
		this.contacts = contacts; 
	}
	
	//@XmlElement 
	//@XmlElementWrapper(name = "requests") 
	public List<User> getContacts() { 
		return this.contacts;
	} 
}
