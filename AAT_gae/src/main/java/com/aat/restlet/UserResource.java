package com.aat.restlet;

import org.restlet.resource.Post;
import org.restlet.resource.Get;
import org.restlet.data.Form;
import org.restlet.data.Cookie;
import org.restlet.Request;
import org.restlet.resource.ServerResource;
import org.restlet.resource.ResourceException;
import org.restlet.Response;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.engine.util.Base64;

import com.googlecode.objectify.ObjectifyService;
import com.aat.datastore.User;
import com.aat.datastore.Tutor;
import com.aat.datastore.Student;
import com.aat.utils.ResourceUtil;
import com.aat.utils.Constants;

import java.security.SecureRandom;

public class UserResource extends ServerResource {
	
	@Post
	public Representation create(Representation entity) {
		// TODO: add check to see if username already taken?
		Form params = new Form(entity);
		String type = ResourceUtil.getParam(params, "type", true).toLowerCase();
		String email = ResourceUtil.getParam(params, "email", true);
		String password = ResourceUtil.getParam(params, "password", true);
		String first = ResourceUtil.getParam(params, "first", true);
		String last = ResourceUtil.getParam(params, "last", true);

		// Create Salt
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[16];
		random.nextBytes(bytes);
		// Encrypt password
		byte[] hash = ResourceUtil.hash(password, bytes);
		Base64 enc = new Base64();
		String salt = enc.encode(bytes, false);;
		password = enc.encode(hash, false);

		User u;
		if (type.equals("student")) {
			u = new Student(email, password, salt, first, last);
		}
		else if (type.equals("tutor")) {
			String pin = ResourceUtil.getParam(params, "pin", true);
			u = new Tutor(email, password, salt, first, last, pin);
		}
		else {
			throw new ResourceException(404, "Incorrect parameter", "User typw is incorrect", null);
		}
		ObjectifyService.ofy().save().entity(u).now();

		return new StringRepresentation(u.getId().toString());
	}

	@Get
	public User retrieve()
	{
		Long userId = Long.parseLong(getAttribute(Constants.userId), 10);
		ResourceUtil.checkToken(this, userId);
		User u = getUser(userId);

		return u;
	}

	private User getUser(Long userId) {
		User u = ObjectifyService.ofy().load().type(User.class).id(userId).now();
		if (u == null) {
			throw new ResourceException(404, "Not found", "Cannot find User with given id", null);
		}
		return u;
	}
}
