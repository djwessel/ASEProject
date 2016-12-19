package com.aat.restlet;

import org.restlet.resource.Post;
import org.restlet.data.Form;
import org.restlet.resource.ServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

import com.googlecode.objectify.ObjectifyService;
import com.aat.datastore.User;
import com.aat.datastore.Tutor;
import com.aat.datastore.Student;

public class UserCreateResource extends ServerResource {
	
	@Post
	public Representation create() {
		Form params = getQuery();
		String type = getParam(params, "type", true);
		String email = getParam(params, "email", true);
		String password = getParam(params, "password", true);
		String first = getParam(params, "first", true);
		String last = getParam(params, "last", true);

		User u;
		if (type.toLowerCase().equals("student")) {
			u = new Student(email, password, first, last);
		}
		else if (type.toLowerCase().equals("tutor")) {
			String pin = getParam(params, "pin", true);
			u = new Tutor(email, password, first, last, pin);
		}
		else {
			return new StringRepresentation("Incorrect User type");
		}
		ObjectifyService.ofy().save().entity(u).now();
		return new StringRepresentation("Success");
	}

	public String getParam(Form params, String key, boolean required) {
		String val = params.getFirstValue(key, true, "");
		if (required && "".equals(val))
			throw new ResourceException(404);
		return val;
	}
	
	//private Representation toJSONString() {}
}
