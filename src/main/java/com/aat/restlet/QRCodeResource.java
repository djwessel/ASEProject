package com.aat.restlet;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.appengine.repackaged.com.google.common.hash.Hashing;

/**
 * Creates a token based on Student id, group id and the date of first the day(Sunday) of the current week. 
 * */
public class QRCodeResource extends ServerResource {
	
	@Get
	public Representation retrieve(){
							
		String userID = getAttribute("user_id");
		String groupID = getAttribute("group_id");
		StringBuffer sBufferToken = new StringBuffer();
		String token = "";
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);;
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				
		sBufferToken.append(userID);
		sBufferToken.append("-");		
		sBufferToken.append(groupID);
		sBufferToken.append("-");
		sBufferToken.append(calendar.getTime().getDate());
		sBufferToken.append(calendar.getTime().getMonth());
		sBufferToken.append(calendar.getTime().getYear());
		token  =  userID+"/"+groupID+"/"+
				  Hashing.sha256().hashString(sBufferToken.toString(), StandardCharsets.UTF_8).toString();
						
		return new StringRepresentation(token);
	}
	

}
