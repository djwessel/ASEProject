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
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;



/**
 * Creates a token base	d on Student id, group id and the date of first the day(Sunday) of the current week. 
 * */
public class QRCodeResource extends ServerResource {
	
	@Get
	public Representation retrieve(){
							
		String userID = getAttribute("user_id");
		String groupID = getAttribute("group_id");
		StringBuffer sBufferToken = new StringBuffer();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);;
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(calendar.getTime());
		
		HashMap<String,String> token = getTokens(userID, groupID, date);
		
		if (token.get(date) == null){
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[20];
			random.nextBytes(bytes);
			token.put(date, bytes.toString());
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
	 * Search for a token already assigned to some date in case this exist
	 * returns the map of tokens 
	 * */
	private HashMap<String,String> getTokens(String userId,String groupId,String date){
		HashMap<String,String> tokens = null;
		Student student = (Student) ObjectifyService.ofy()
				.load()
				.type(User.class)
				.id(Long.parseLong(userId, 10))	
				.now();
		
		List<Ref<AttendanceRecord>> refAttendances = student.getGroups();
		for (Ref<AttendanceRecord> refAttendance : refAttendances){	
			long parentId = refAttendance.getValue().getParent().getId();
			if (Long.parseLong(groupId, 10)==parentId){
				tokens=refAttendance.getValue().getAttendaceToken();
				break;
			}
		}
		
		return tokens;
	}
	

}
