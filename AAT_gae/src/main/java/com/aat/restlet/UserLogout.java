package com.aat.restlet;

import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;
import org.restlet.resource.ResourceException;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;

import com.aat.datastore.User;
import com.googlecode.objectify.ObjectifyService;

import com.aat.utils.ResourceUtil;
import com.aat.utils.Constants;

import java.security.SecureRandom;

public class UserLogout extends ServerResource {
	private final static String LOGOUT = "logout successfully";
	
	@Delete
	public String logout() {
		User u = retrieveUser(getAttribute(Constants.userId));
		if (u == null) {
			throw new ResourceException(404, "Not found", "User does not exist", null);
		}
		else {
			getResponse().getCookieSettings().add(new CookieSetting(0, "sessionToken", "INVALID", "/", null, "User session token", 0, Constants.ON_HTTPS, true));
			getResponse().getCookieSettings().add(new CookieSetting(0, "userType", "INVALID", "/", null, "User type", 0, Constants.ON_HTTPS, false));
			// invalidate token
			u.setToken(null);
			ObjectifyService.ofy().save().entity(u);
			// Finish request
	   		return LOGOUT;
		}
	}
	
	/**
	 * @param id
	 * @return User user
	 * Retrieves a user by id.
	 * */
	private User retrieveUser (String id){
		User user = ObjectifyService.ofy()
				   .load()
				   .type(User.class)
				   .id(Long.parseLong(id, 10)).now();
		return user;
	} 

}
