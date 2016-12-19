package com.aat.restlet;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Key;
import com.aat.datastore.Course;
import com.aat.datastore.Group;
import com.aat.utils.Constants;

public class GroupsResource extends ServerResource {

	@Get
	public List<Group> retrieve()
	{
		String courseID = getAttribute(Constants.courseID);
		if (courseID == null) {
			throw new RuntimeException(Constants.incorrectRequestFormat);
		}
		Key<Course> course = Key.create(Course.class, courseID);
		return ObjectifyService.ofy().load()
			    .type(Group.class)
			    .ancestor(course)
			    .list();
	}

}
