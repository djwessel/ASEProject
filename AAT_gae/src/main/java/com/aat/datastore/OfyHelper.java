package com.aat.datastore;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import java.lang.UnsupportedOperationException;

/**
 * OfyHelper, a ServletContextListener, is setup in web.xml to run before a JSP or Restlet is run. This is
 * required to let JSPs and Restlets access Ofy.
 **/
public class OfyHelper implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {
		ObjectifyService.register(Course.class);
		ObjectifyService.register(Group.class);
		ObjectifyService.register(AttendanceRecord.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(Student.class);
		ObjectifyService.register(Tutor.class);
	}

	public void contextDestroyed(ServletContextEvent event) {
		throw new UnsupportedOperationException("App Engine does not currently invoke this method");
	}
}
