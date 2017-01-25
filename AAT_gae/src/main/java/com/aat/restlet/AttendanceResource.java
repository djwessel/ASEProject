package com.aat.restlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.aat.datastore.AttendanceRecord;
import com.aat.datastore.Course;
import com.aat.datastore.Group;
import com.aat.datastore.Student;
import com.aat.datastore.Tutor;
import com.aat.datastore.User;
import com.aat.utils.Constants;
import com.aat.utils.ResourceUtil;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;

public class AttendanceResource extends ServerResource {
	
	@Post	
	public Representation create(Representation entity) {
		// Check if of type Student
		ResourceUtil.checkTokenPermissions(this, Student.class);

		// Get input parameters
		Form params = new Form(entity);
		Long userId = Long.parseLong(ResourceUtil.getParam(params, "user", true), 10);
		Long courseId = Long.parseLong(getAttribute(Constants.courseId), 10);
		Long groupId = Long.parseLong(getAttribute(Constants.groupId), 10);

		// Check if token coresponds to userid
		ResourceUtil.checkToken(this, userId);

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
		AttendanceRecord ar = new AttendanceRecord(courseId, groupId, userId);
		ObjectifyService.ofy().save().entity(ar).now();
		s.addGroup(ar);
		ObjectifyService.ofy().save().entity(s).now();
		// Return...
		return new StringRepresentation(ar.getId().toString());
	}

	@Put
	public String update(Representation entity) {
		
		// Check if of type Tutor
		ResourceUtil.checkTokenPermissions(this, Tutor.class);
		
		// Get input parameters
		Form params = new Form(entity);
		Long studentId = Long.parseLong(getAttribute(Constants.userId), 10);
		Long groupId = Long.parseLong(getAttribute(Constants.groupId), 10);
		String flagMode = ResourceUtil.getParam(params, Constants.flagMode, true);
		String dateWeek = ResourceUtil.getParam(params, Constants.dateWeek, true);
		Date date;
		AttendanceRecord attendance = retrieveAttendanceRecord(studentId,groupId);
		List <Date> dates = null;
		try {
			date = new SimpleDateFormat("dd-MM-yyyy").parse(dateWeek);
			
			if (flagMode.equals("A")){
					dates = attendance.getAttendance();
			}
			else if (flagMode.equals("P")){
					dates = attendance.getPresentation();
			}
			else{
				throw new ResourceException(409, "Conflict", "Invalid flag.", null);
			}
			
			if (!dates.contains(date)){
				dates.add(date);
			}else{
				throw new ResourceException(409, "Conflict", "The student already has an attendance record for this week", null);
			}
			
			ObjectifyService.ofy().save().entity(attendance).now();
		
		} catch (ParseException e) {
			throw new ResourceException(409, "Conflict", "Date of week is not valid.", null);
		}
		
		return attendance.getId().toString();
	}

	@Get
	public AttendanceRecord retrieve() {
		// TODO: Implement
		
		return null;
	}

	@Delete
	public void remove() {
		// TODO: Implement
	}
	
	/**
	 * Get attendance record for a specific group for a student
	 * */
	private AttendanceRecord retrieveAttendanceRecord(Long userId, Long groupId){
		AttendanceRecord attendanceRecord = null;
		Student student = (Student) ObjectifyService.ofy()
				.load()
				.type(User.class)
				.id(userId)	
				.now();
		
		List<Ref<AttendanceRecord>> refAttendances =  student.getGroups();
		for (Ref<AttendanceRecord> refAttendance : refAttendances){	
			long parentId = refAttendance.getValue().getParent().getId();
			if (groupId == parentId){
				attendanceRecord = refAttendance.getValue();
				break;
			}
		}
		return attendanceRecord;
	}
}
