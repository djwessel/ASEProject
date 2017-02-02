package com.aat.restlet;

import org.restlet.data.Form;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.resource.ResourceException;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.engine.util.Base64;

import com.aat.datastore.User;
import com.aat.datastore.Student;
import com.aat.utils.ResourceUtil;
import com.googlecode.objectify.ObjectifyService;

import com.aat.utils.ResourceUtil;
import com.aat.utils.Constants;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.util.Calendar;

public class UserLogin extends ServerResource {
	
	@Post
	public Long login(Representation entity) {
		Form params = new Form(entity);
		String email = ResourceUtil.getParam(params, "email", true);
		String password = ResourceUtil.getParam(params, "password", true);
		User u = retrieveUser(email);

		if (u == null) {
			throw new ResourceException(401, "Incorrect Credentials", "Email or password incorrect", null);
		}

		// encrypt password and then compare
		Base64 enc = new Base64();
		byte[] salt = enc.decode(u.getSalt());
		byte[] hash = ResourceUtil.hash(password, salt);
		password = enc.encode(hash, false);

		// Compare message digests
		if (!MessageDigest.isEqual(hash, enc.decode(u.getPassword()))) {
			throw new ResourceException(401, "Incorrect Credentials", "Email or password incorrect", null);
		}
		else {
			// Create Token
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[16];
			random.nextBytes(bytes);
			String token = bytes.toString();
			// Set token
			u.setToken(token);
			// Set timeout to 1 hour from now
			Calendar c = Calendar.getInstance();
			c.add(Calendar.SECOND, Constants.TIMEOUT_SECONDS);
			u.setTimeout(c.getTime());
			// Save User to datastore
			ObjectifyService.ofy().save().entity(u);
			String userType = (u instanceof Student) ? "student" : "tutor";
			// Set Request cookies to session token
			getResponse().getCookieSettings().add(new CookieSetting(0, "sessionToken", token, "/", null, "User session token", Constants.TIMEOUT_SECONDS, Constants.ON_HTTPS, true));
			getResponse().getCookieSettings().add(new CookieSetting(0, "userType", userType, "/", null, "User type", Constants.TIMEOUT_SECONDS, Constants.ON_HTTPS, false));
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
