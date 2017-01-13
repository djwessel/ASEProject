package com.aat.restlet;

import org.restlet.data.Form;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.resource.ResourceException;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.representation.Representation;

import com.aat.datastore.User;
import com.aat.utils.ResourceUtil;
import com.googlecode.objectify.ObjectifyService;

import com.aat.utils.ResourceUtil;

import java.security.SecureRandom;

public class UserLogin extends ServerResource {
	private final static String LOGIN = "login successfully";
	
	@Post
	public Long login(Representation entity) {
		Form params = new Form(entity);
		String email = ResourceUtil.getParam(params, "email", true);
		String password = ResourceUtil.getParam(params, "password", true);
		User u = retrieveUser(email);
		if (u == null || !password.equals(u.getPassword())) {
			throw new ResourceException(401, "Incorrect Credentials", "Email or password incorrect", null);
		}
		else {
			// Create Token
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[20];
			random.nextBytes(bytes);
			String token = bytes.toString();
			// Save token
			u.setToken(token);
			ObjectifyService.ofy().save().entity(u);
			// Set Request cookies to session token
			getResponse().getCookieSettings().add(new CookieSetting(0, "sessionToken", token));
			// Finish request
	   		return u.getId();
		}
	}
	
	/**
	 * @param email
	 * @return User user
	 * Retrieves a user by email.
	 * */
	private User retrieveUser (String email){
		User user = ObjectifyService.ofy()
				   .load()
				   .type(User.class)
				   .filter("email", email).first().now();
		return user;
	} 

}
