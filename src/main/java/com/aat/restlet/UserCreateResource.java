package com.aat.restlet;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.data.Form;
import org.restlet.resource.ServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import com.googlecode.objectify.ObjectifyService;
import com.aat.datastore.User;
import com.aat.datastore.Tutor;
import com.aat.datastore.Course;
import com.aat.datastore.Student;
import com.aat.restlet.ResourceUtil;;

public class UserCreateResource extends ServerResource {
	
	@Post
	public Representation create() {
		Form params = getQuery();
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
			return new StringRepresentation("Incorrect User type");
		}
		ObjectifyService.ofy().save().entity(u).now();
		return new StringRepresentation("Success");
	}
	
	
	
}
