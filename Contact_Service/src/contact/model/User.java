package contact.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author rtteal
 * Immutable class for modeling a user
 * Thread safe
 */
@XmlRootElement(name = "user")
public class User implements Comparable<User> {
	public final String userName, fName, lName, email, tel, image;
	
	// required by jetty
	public User() {
		//throw new IllegalStateException("User's no-arg constructor should not be called.");
		this.userName = "";
		this.fName = "";
		this.lName = "";
		this.email = "";
		this.tel = ""; 
		this.image = "";
		
	}

	public User (String userName,
				 String fName,
				 String lName,
				 String email,
				 String tel,
				 String image){
		this.userName = userName;
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.tel = tel;
		this.image = image;
	}
	
	/*@XmlElement
	public String getUserName() {
		return userName;
	}
	
	@XmlElement
	public String getfName() {
		return fName;
	}

	@XmlElement
	public String getlName() {
		return lName;
	}

	@XmlElement
	public String getEmail() {
		return email;
	}

	@XmlElement
	public String getTel() {
		return tel; 
	} */

	@Override
	public int compareTo(User other) {
		return lName.compareTo(other.lName);
	}
	
	@Override
	public String toString(){
		return "user [userName: " + userName
				+ ", fName: " + fName 
				+ ", lName: " + lName
				+ ", email: " + email
				+ ", tel: " + tel + "]";
	}
}
