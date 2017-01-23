package com.aat.restlet;

import java.util.HashMap;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.aat.datastore.AttendanceRecord;
import com.aat.datastore.Course;
import com.aat.datastore.Group;
import com.aat.datastore.Student;
import com.aat.datastore.User;
import com.aat.interfaces.restlet.IGroupsAttendancesResource;
import com.aat.utils.Constants;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;

public class GroupsAttendancesResource extends ServerResource implements IGroupsAttendancesResource{
	@Get	
	public HashMap <String,Group> retrieve() {
		HashMap <String,Group> courseGropus = new HashMap<>(); 
		String userId = getAttribute(Constants.userId);
		Student student = (Student) ObjectifyService.ofy()
						.load()
						.type(User.class)
						.id(Long.parseLong(userId, 10))	
						.now();
		List<Ref<AttendanceRecord>> refAttendances = student.getGroups();
		
		for (Ref<AttendanceRecord> refAttendance : refAttendances){	
			Key<Group> keygroup = refAttendance.getValue().getParent();
			
			Group group=ObjectifyService.ofy().load().key(keygroup).now();
			Course course = ObjectifyService.ofy().load().key(group.getParent()).now();
			
			courseGropus.put(course.getTitle(), group);
			
		}	
		
		return courseGropus;
	}
}
