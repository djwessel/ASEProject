package com.aat.restlet;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.aat.datastore.AttendanceRecord;
import com.aat.datastore.Course;
import com.aat.datastore.Group;
import com.aat.datastore.Student;
import com.aat.datastore.User;
import com.aat.interfaces.restlet.IGroupsAttendancesResource;
import com.aat.pojos.CourseGroupPOJO;
import com.aat.utils.Constants;
import com.aat.utils.ResourceUtil;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;

public class GroupsAttendancesResource extends ServerResource implements IGroupsAttendancesResource{
	@Get	
	public List<CourseGroupPOJO> retrieve() {
		List<CourseGroupPOJO> courseGroups = new ArrayList<CourseGroupPOJO>();
		
		Long userId = Long.parseLong(getAttribute(Constants.userId), 10);
		// Check if session token matches userId
		ResourceUtil.checkToken(this, userId);
		Student student = (Student) ObjectifyService.ofy()
						.load()
						.type(User.class)
						.id(userId)	
						.now();
		List<Ref<AttendanceRecord>> refAttendances = student.getGroups();
		
		for (Ref<AttendanceRecord> refAttendance : refAttendances){	
			AttendanceRecord ar = refAttendance.get();
			if (ar != null) {
				Key<Group> keygroup = ar.getParent();
			
				Group group = ObjectifyService.ofy().load().key(keygroup).now();
				Course course = ObjectifyService.ofy().load().key(group.getParent()).now();	
				CourseGroupPOJO courseGroup = new CourseGroupPOJO (course.getId(), course.getTitle(),group);
				courseGroups.add(courseGroup);
			}
		}	
		
		return courseGroups;
	}
}
