package com.aat.restlet;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.aat.datastore.Course;
import com.aat.datastore.Group;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

public class GroupsResource extends ServerResource implements IGroupsResource{

	@Get
	public List<Group> retrieve(){
		String courseId = getAttribute("courseID");
		Key<Course> course = Key.create(Course.class,Long.parseLong(courseId));
		List<Group> groups =  ObjectifyService.ofy()
				.load()
				.type(Group.class)
				.ancestor(course)
				.list();
		return  groups;
	}	
}
