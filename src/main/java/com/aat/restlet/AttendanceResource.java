package com.aat.restlet;

import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.Get;
import org.restlet.resource.Delete;
import org.restlet.data.Form;
import org.restlet.resource.ServerResource;
import org.restlet.resource.ResourceException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.aat.datastore.AttendanceRecord;
import com.aat.datastore.Student;
import com.aat.datastore.Group;
import com.aat.datastore.Course;
import com.aat.utils.ResourceUtil;

public class AttendanceResource extends ServerResource {
	
	@Post
	public Representation create() {
		// Get input parameters
		Form params = getQuery();
		Long userId = Long.parseLong(ResourceUtil.getParam(params, "user", true), 10);
		Long courseId = Long.parseLong(getAttribute("course_id"), 10);
		Long groupId = Long.parseLong(getAttribute("group_id"), 10);

		// Throws a ClassCastException if userId isnt of type Student
		Student s = ObjectifyService.ofy()
			.load()
			.type(Student.class)
			.id(userId)
			.now();
		Group g = ObjectifyService.ofy()
			.load()
			.type(Group.class)
			.parent(Key.create(Course.class, courseId))
			.id(groupId)
			.now();
		// check to see if group exists
		if (g == null) {
			throw new ResourceException(404, "Not found", "Group does not exist", null);
		}

		// Check to see if Student already has attendance record for course/group
		for (Ref<AttendanceRecord> ref: s.getGroups()) {
			if (groupId.equals(ref.get().getParent().getId())) {
				throw new ResourceException(409, "Conflict", "Student already signed up for group.", null);
			}
		}

		// Create new AttendanceRecord and add to Students list of AttendanceRecords
		AttendanceRecord ar = new AttendanceRecord(groupId, userId);
		ObjectifyService.ofy().save().entity(ar).now();
		s.addGroup(ar);
		ObjectifyService.ofy().save().entity(s).now();
		// Return...
		return new StringRepresentation(ar.getId().toString());
	}

	@Put
	public void update() {
		// TODO: Implement
	}

	@Get
	public AttendanceResource retrieve() {
		// TODO: Implement
		return null;
	}

	@Delete
	public void deltete() {
		// TODO: Implement
	}
}
