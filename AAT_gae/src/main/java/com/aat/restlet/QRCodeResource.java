package com.aat.restlet;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.aat.datastore.AttendanceRecord;
import com.aat.datastore.Student;
import com.aat.datastore.User;
import com.aat.utils.ResourceUtil;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;



/**
 * Creates a token base	on Student id, group id and the date of first the day(Sunday) of the current week. 
 * */
public class QRCodeResource extends ServerResource {
	
	@Get
	public Representation retrieve(){
							
		Long userID = Long.parseLong(getAttribute("user_id"), 10);
		Long groupID = Long.parseLong(getAttribute("group_id"), 10);
		// Check if session token matches userID
		ResourceUtil.checkToken(this, userID);

		StringBuffer sBufferToken = new StringBuffer();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);;
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(calendar.getTime());
		
		AttendanceRecord attendance = getAttendance(userID, groupID);
		HashMap<String,String> token = attendance.getAttendaceToken();
		
		if (token.get(date) == null){
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[20];
			random.nextBytes(bytes);
			token.put(date, bytes.toString());
			saveTokens(token, attendance);
		}
		
		sBufferToken.append(userID);
		sBufferToken.append("/");		
		sBufferToken.append(groupID);
		sBufferToken.append("/");
		sBufferToken.append(date);
		sBufferToken.append("/");
		sBufferToken.append(token.get(date));	
							
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
	 * */
	private AttendanceRecord getAttendance (Long userId, Long groupId ){
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
