package contact.dao;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class Config implements ServletContextListener{

	@Override
    public void contextInitialized(ServletContextEvent event) {
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        CassandraDS.close();
    	
        // http://stackoverflow.com/questions/8903379/standalone-jdbc-pool-implementation-memory-leak
    	// deregister JDBC driver to prevent Tomcat 7 from complaining about memory leaks
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                System.out.println(String.format("Deregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
                System.out.println(String.format("Error deregistering driver %s", driver));
                e.printStackTrace();
            }
        }
    	
    	
    }
}
