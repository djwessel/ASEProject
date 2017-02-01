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
			AttendanceRecord ar = ref.get();
			if (ar != null) {
				Key<Group> parent = ar.getParent();
				if (parent != null && groupId.equals(parent.getId()) || parent.getParent() != null && courseId.equals(parent.getParent().getId())) {
					throw new ResourceException(409, "Conflict", "Student already signed up for group in course.", null);
				}
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
		Long courseId = Long.parseLong(getAttribute(Constants.courseId), 10);
		String flagMode = ResourceUtil.getParam(params, Constants.flagMode, true);
		String dateWeek = ResourceUtil.getParam(params, Constants.dateWeek, true);
		String token = ResourceUtil.getParam(params, Constants.token, true);
		
		Date date;
		AttendanceRecord attendance = retrieveAttendanceRecord(studentId,courseId,groupId);
		String storedToken = attendance.getAttendaceToken().get(dateWeek);
		
		if (storedToken!=null && storedToken.equals(token)){
			List <Date> dates = null;
			try {
				date = new SimpleDateFormat("dd-MM-yyyy").parse(dateWeek);
				
				if (flagMode.equals("A")){
						dates = attendance.getAttendance();
				}
				else if (flagMode.equals("P")){
						dates = attendance.getPresentation();
				}
				
				System.out.println(dates);
				if (!dates.contains(date)){
					dates.add(date);
					ObjectifyService.ofy().save().entity(attendance).now();
				}
			
			} catch (ParseException e) {
				throw new ResourceException(409, "Conflict", "Date format of the week is not valid.", null);
			}
			
		}else{
			throw new ResourceException(409, "Conflict", "Invalid token", null);
		}
		
		return attendance.getId().toString();
	}

	@Get
	public AttendanceRecord retrieve() {
		Long userId = Long.parseLong(getAttribute(Constants.userId), 10);
		Long groupId = Long.parseLong(getAttribute(Constants.groupId), 10);
		Long courseId = Long.parseLong(getAttribute(Constants.courseId), 10);
		
		// Check if of type Student and if user token matches student id
		ResourceUtil.checkToken(this, userId);
		ResourceUtil.checkTokenPermissions(this, Student.class);
		
		return retrieveAttendanceRecord(userId, courseId, groupId);
	}

	@Delete
	public void remove() {
		Long userId = Long.parseLong(getAttribute(Constants.userId), 10);
		Long groupId = Long.parseLong(getAttribute(Constants.groupId), 10);
		// Check if of type Student and if user token matches student id
		ResourceUtil.checkToken(this, userId);
		ResourceUtil.checkTokenPermissions(this, Student.class);

		Ref<AttendanceRecord> ar = null;
		Student student = (Student) ObjectifyService.ofy()
				.load()
				.type(User.class)
				.id(userId)
				.now();

		List<Ref<AttendanceRecord>> refAttendances =  student.getGroups();
		for (Ref<AttendanceRecord> refAttendance : refAttendances){
			AttendanceRecord check = refAttendance.get();
			if (check != null) {
				long parentId = check.getParent().getId();
				if (groupId == parentId){
					ar = refAttendance;
					break;
				}
			}
		}

		// This deletes the record. May want to put some more checks. 
		ObjectifyService.ofy().delete().entity(ar.getValue()).now();

		// Update User to remove record
		refAttendances.remove(ar);
		student.setGroups(refAttendances);
		ObjectifyService.ofy().save().entity(student).now();
	}
	
	/**
	 * Get attendance record for a specific group for a student
	 * */
	private AttendanceRecord retrieveAttendanceRecord(Long userId, Long courseId, Long groupId){
		AttendanceRecord attendanceRecord = null;
		Student student = (Student) ObjectifyService.ofy()
				.load()
				.type(User.class)
				.id(userId)	
				.now();
		
		List<Ref<AttendanceRecord>> refAttendances =  student.getGroups();
		for (Ref<AttendanceRecord> refAttendance : refAttendances){	
			AttendanceRecord ar = refAttendance.getValue();
			if (ar!=null){
				Key<Group> keygroup = ar.getParent();
				Group group = ObjectifyService.ofy().load().key(keygroup).now();
				long storedGroupId = keygroup.getId();
				long storedCourseId = group.getParent().getId();
				
				if (groupId == storedGroupId && courseId == storedCourseId){
					attendanceRecord = ar;
					break;
				}
			}
		}
		return attendanceRecord;
	}
}
