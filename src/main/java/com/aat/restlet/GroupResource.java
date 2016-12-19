package com.aat.restlet;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.aat.datastore.Course;
import com.aat.datastore.Group;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

public class GroupResource extends ServerResource {
	private final static String noGroupMsg = "Group does not exist";
	

	@Post
	public void create(String courseID, String groupName)
	{
		Group group = new Group(getAttribute("course_id"), getAttribute("group_name"));
		ObjectifyService.ofy().save().entity(group).now();
	}
	
	@Put
	public void update(String courseID, String groupID, String newName)
	{
		Group group = retrieveGroup(courseID, groupID);
		if (group == null) {
			throw new RuntimeException(noGroupMsg);
		}
		group.setName(newName);
		ObjectifyService.ofy().save().entity(group).now();
	}
	
	@Get
	public Group retrieve(String courseID, String groupID)
	{
		Group group = retrieveGroup(courseID, groupID);
		if (group == null) {
			throw new RuntimeException(noGroupMsg);
		}
		return group;
	}
	
	@Delete
	public void remove(String courseID, String groupID)
	{
		Group group = retrieveGroup(courseID, groupID);
		if (group == null)  {
			throw new RuntimeException(noGroupMsg);
		}
		ObjectifyService.ofy().delete().entity(group);
	}
	
	private Group retrieveGroup(String courseID, String groupID) {
		Key<Course> course = Key.create(Course.class, courseID);
		return ObjectifyService.ofy()
				.load()
				.type(Group.class)
				.parent(course)
				.id(Long.parseLong(groupID, 10))
				.now();
	}
}
