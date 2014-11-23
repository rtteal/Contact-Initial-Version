package contact.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author rtteal
 */
@XmlRootElement(name = "user")
public class User implements Comparable<User> {
	public String userName, fName, lName, email, tel, image;
	
	public User() {
		
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
	
	@XmlElement
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
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

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
