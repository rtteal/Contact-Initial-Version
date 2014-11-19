package contact.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlElementWrapper; 
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "incomingRequests")
public class IncomingRequests {
	private final List<User> requests;
	
	public IncomingRequests (){
		throw new IllegalStateException("IncomingRequsts no-arg"
				+ " constructor should not be called");
	}
	
	public IncomingRequests (List<User> requests){
		this.requests = requests; 
	}
	
	@XmlElement 
	@XmlElementWrapper(name = "requests") 
	public List<User> getRequests() { 
		return this.requests;
	} 
}
