package com.aat.restlet;

import org.restlet.resource.Post;
import org.restlet.resource.Get;
import org.restlet.data.Form;
import org.restlet.resource.ServerResource;
import org.restlet.resource.ResourceException;
import org.restlet.Response;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import com.googlecode.objectify.ObjectifyService;
import com.aat.datastore.User;
import com.aat.datastore.Tutor;
import com.aat.datastore.Student;
import com.aat.utils.ResourceUtil;
import com.aat.utils.Constants;

public class UserResource extends ServerResource {
	
	@Post
	public Representation create(Representation entity) {
		// TODO: add check to see if username already taken?
		Form params = new Form(entity);
		String type = ResourceUtil.getParam(params, "type", true);
		String email = ResourceUtil.getParam(params, "email", true);
		String password = ResourceUtil.getParam(params, "password", true);
		String first = ResourceUtil.getParam(params, "first", true);
		String last = ResourceUtil.getParam(params, "last", true);

		User u;
		if (type.toLowerCase().equals("student")) {
			u = new Student(email, password, first, last);
		}
		else if (type.toLowerCase().equals("tutor")) {
			String pin = ResourceUtil.getParam(params, "pin", true);
			u = new Tutor(email, password, first, last, pin);
		}
		else {
			throw new ResourceException(404, "Incorrect parameter", "User is incorrect", null);
		}
		ObjectifyService.ofy().save().entity(u).now();

		return new StringRepresentation(u.getId().toString());
	}

	@Get
	public User retrieve()
	{
		String userId = getAttribute(Constants.userId);
		User u = getUser(userId);

		return u;
	}

	private User getUser(String userId) {
		User u = ObjectifyService.ofy().load().type(User.class).id(Long.parseLong(userId, 10)).now();
		if (u == null) {
			throw new ResourceException(404, "Not found", "Cannot find User with given id", null);
		}
		return u;
	}
}
