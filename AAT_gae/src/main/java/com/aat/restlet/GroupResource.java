package com.aat.restlet;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.restlet.resource.ResourceException;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Key;
import com.aat.datastore.Course;
import com.aat.datastore.Group;
import com.aat.datastore.Tutor;
import com.aat.utils.Constants;
import com.aat.utils.ResourceUtil;;

public class GroupResource extends ServerResource {

	@Post
	public Representation create(Representation entity)
	{
		// Check if of type Tutor
		ResourceUtil.checkTokenPermissions(this, Tutor.class);
		String courseId = getAttribute(Constants.courseId);
		ResourceUtil.checkAttributeValue(Constants.courseId, courseId, true);
		assert(courseId != null);
		String groupName = ResourceUtil.getParam(new Form(entity), "name", true);
		assert(groupName != null);
		
		if (ObjectifyService.ofy().load().type(Group.class)
				.ancestor(Key.create(Course.class, Long.parseLong(courseId, 10)))
				.filter("name", groupName).first().now() != null) {
			throw new ResourceException(409, "Already Exists", "Group with given title already exists for given course", null);
		}
		Group group = new Group(Long.parseLong(courseId, 10), groupName);
		ObjectifyService.ofy().save().entity(group).now();
		
		return new StringRepresentation(group.getId().toString());
	}
	
	@Put
	public void update()
	{
		// Check if of type Tutor
		ResourceUtil.checkTokenPermissions(this, Tutor.class);
		String courseId = getAttribute(Constants.courseId);
		ResourceUtil.checkAttributeValue(Constants.courseId, courseId, true);
		assert(courseId != null);
		
		String groupId = getAttribute(Constants.groupId);
		ResourceUtil.checkAttributeValue(Constants.groupId, groupId, true);
		assert(groupId != null);
		
		String newName = ResourceUtil.getParam(getQuery(), "name", true);
		assert(newName != null);
		
		Group group = retrieveGroup(courseId, groupId);
		assert(group != null);
		
		group.setName(newName);
		ObjectifyService.ofy().save().entity(group).now();
	}
	
	@Get
	public Group retrieve()
	{
		String courseId = getAttribute(Constants.courseId);
		ResourceUtil.checkAttributeValue(Constants.courseId, courseId, true);
		assert(courseId != null);
		
		String groupId = getAttribute(Constants.groupId);
		ResourceUtil.checkAttributeValue(Constants.groupId, groupId, true);
		assert(groupId != null);
		
		Group group = retrieveGroup(courseId, groupId);
		assert(group != null);
		
		return group;
	}
	
	@Delete
	public void remove()
	{
		// Check if of type Tutor
		ResourceUtil.checkTokenPermissions(this, Tutor.class);
		String courseId = getAttribute(Constants.courseId);
		ResourceUtil.checkAttributeValue(Constants.courseId, courseId, true);
		assert(courseId != null);
		
		String groupId = getAttribute(Constants.groupId);
		ResourceUtil.checkAttributeValue(Constants.groupId, groupId, true);
		assert(groupId != null);
		
		Group group = retrieveGroup(courseId, groupId);
		assert(group != null);
		
		ObjectifyService.ofy().delete().entity(group);
	}
	
	private Group retrieveGroup(String courseId, String groupId) {
		Key<Course> course = Key.create(Course.class, Long.parseLong(courseId, 10));
		Group group = ObjectifyService.ofy()
				.load()
				.type(Group.class)
				.parent(course)
				.id(Long.parseLong(groupId, 10))
				.now();
		if (group == null) {
			throw new RuntimeException(Constants.noGroupMsg);
		}
		return group;
	}
	
}
