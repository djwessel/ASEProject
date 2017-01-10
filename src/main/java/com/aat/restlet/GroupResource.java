package com.aat.restlet;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Key;
import com.aat.datastore.Course;
import com.aat.datastore.Group;
import com.aat.utils.Constants;
import com.aat.utils.ResourceUtil;;

public class GroupResource extends ServerResource {

	@Post
	public void create()
	{
		String courseID = getAttribute(Constants.courseID);
		ResourceUtil.checkAttributeValue(Constants.courseID, courseID, true);
		assert(courseID != null);
		String groupName = ResourceUtil.getParam(getQuery(), "name", true);
		assert(groupName != null);
		
		Group group = new Group(Long.parseLong(courseID, 10), groupName);
		ObjectifyService.ofy().save().entity(group).now();
	}
	
	@Put
	public void update()
	{
		String courseID = getAttribute(Constants.courseID);
		ResourceUtil.checkAttributeValue(Constants.courseID, courseID, true);
		assert(courseID != null);
		
		String groupID = getAttribute(Constants.groupId);
		ResourceUtil.checkAttributeValue(Constants.groupId, groupID, true);
		assert(groupID != null);
		
		String newName = ResourceUtil.getParam(getQuery(), "name", true);
		assert(newName != null);
		
		Group group = retrieveGroup(courseID, groupID);
		assert(group != null);
		
		group.setName(newName);
		ObjectifyService.ofy().save().entity(group).now();
	}
	
	@Get
	public Group retrieve()
	{
		String courseID = getAttribute(Constants.courseID);
		ResourceUtil.checkAttributeValue(Constants.courseID, courseID, true);
		assert(courseID != null);
		
		String groupID = getAttribute(Constants.groupId);
		ResourceUtil.checkAttributeValue(Constants.groupId, groupID, true);
		assert(groupID != null);
		
		Group group = retrieveGroup(courseID, groupID);
		assert(group != null);
		
		return group;
	}
	
	@Delete
	public void remove()
	{
		String courseID = getAttribute(Constants.courseID);
		ResourceUtil.checkAttributeValue(Constants.courseID, courseID, true);
		assert(courseID != null);
		
		String groupID = getAttribute(Constants.groupId);
		ResourceUtil.checkAttributeValue(Constants.groupId, groupID, true);
		assert(groupID != null);
		
		Group group = retrieveGroup(courseID, groupID);
		assert(group != null);
		
		ObjectifyService.ofy().delete().entity(group);
	}
	
	private Group retrieveGroup(String courseID, String groupID) {
		Key<Course> course = Key.create(Course.class, Long.parseLong(courseID, 10));
		Group group = ObjectifyService.ofy()
				.load()
				.type(Group.class)
				.parent(course)
				.id(Long.parseLong(groupID, 10))
				.now();
		if (group == null) {
			throw new RuntimeException(Constants.noGroupMsg);
		}
		return group;
	}
	
}
