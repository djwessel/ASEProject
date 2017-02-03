	package com.aat.restlet;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.aat.datastore.AttendanceRecord;
import com.aat.datastore.Group;
import com.aat.datastore.Student;
import com.aat.datastore.User;
import com.aat.utils.Constants;
import com.aat.utils.ResourceUtil;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;



/**
 * Creates a token base	on Student id, group id and the date of first the day(Sunday) of the current week. 
 * */
public class QRCodeResource extends ServerResource {
	
	@Post
	public Representation create(Representation entity){
							
		Long userID = Long.parseLong(getAttribute(Constants.userId), 10);
		Long groupID = Long.parseLong(getAttribute(Constants.groupId), 10);
		Long courseID = Long.parseLong(getAttribute(Constants.courseId), 10);
		// Check if session token matches userID
		ResourceUtil.checkToken(this, userID);

		StringBuffer sBufferToken = new StringBuffer();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);;
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(calendar.getTime());
		
		AttendanceRecord attendance = getAttendance(userID, courseID, groupID);
		HashMap<String,String> token = attendance.getAttendaceToken();
		String storedToken = token.get(date); 
		if (storedToken == null){
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[20];
			random.nextBytes(bytes);
                        storedToken = bytes.toString();
			token.put(date, storedToken);
			saveTokens(token, attendance);
		}
		
		sBufferToken.append(userID);
		sBufferToken.append(",");
		sBufferToken.append(courseID);
		sBufferToken.append(",");
		sBufferToken.append(groupID);
		sBufferToken.append(",");
		sBufferToken.append(date);
		sBufferToken.append(",");
		sBufferToken.append(storedToken);	
							
		return new StringRepresentation(sBufferToken.toString());
	}
	
	
	/**
	 * Update record of tokens for an attendance
	 * */
	private void saveTokens(HashMap<String,String> tokens, AttendanceRecord attendance){
		attendance.setAttendaceToken(tokens);
		ObjectifyService.ofy().save().entity(attendance).now();
	}
	
	/**
	 * Get attendance record for a specific group for a student
	 * @param userId
	 * @param courseId
	 * @param groupId
	 * */
	private AttendanceRecord getAttendance (Long userId, Long courseId, Long groupId ){
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
				long storedGroupId = keygroup.getId();
				long storedCourseId = keygroup.getParent().getId();
				
				if (groupId == storedGroupId && courseId == storedCourseId){
					attendanceRecord = ar;
					break;
				}
			}
		}
		return attendanceRecord;
	}
	

}
