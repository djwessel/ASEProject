package com.aat.restlet;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.aat.datastore.Course;
import com.aat.datastore.Group;
import com.aat.interfaces.restlet.IGroupsResource;
import com.aat.utils.Constants;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

public class GroupsResource extends ServerResource implements IGroupsResource{

	@Get
	public List<Group> retrieve(){

		String courseId = getAttribute(Constants.courseId);
		Key<Course> course = Key.create(Course.class,Long.parseLong(courseId, 10));
		
		List<Group> groups =  ObjectifyService.ofy()
				.load()
				.type(Group.class)
				.ancestor(course)
				.list();
		return  groups;
	}	
}
