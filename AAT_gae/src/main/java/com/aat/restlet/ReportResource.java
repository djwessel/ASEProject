package com.aat.restlet;


import java.util.List;
import java.util.ArrayList;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.resource.ResourceException;
import com.aat.utils.ResourceUtil;
import com.aat.utils.Constants;
import com.aat.pojos.StudentAttendancePOJO;
import com.aat.datastore.Course;
import com.aat.datastore.AttendanceRecord;
import com.aat.datastore.Student;
import com.aat.datastore.Tutor;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Key;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public class ReportResource extends ServerResource {

	@Get
	public List<StudentAttendancePOJO> retrieve() {
		// Check if of type Tutor
		ResourceUtil.checkTokenPermissions(this, Tutor.class);
	   	Long courseId = Long.parseLong(getAttribute(Constants.courseId), 10);

		Course c = retrieveCourse(courseId);
		int reqAttend = c.getReqAtten();
		int reqPresent = c.getReqPresent();
	   	List<AttendanceRecord> ars = retrieveRecords(courseId);

		List<StudentAttendancePOJO> students = new ArrayList();
		List<StudentAttendancePOJO> bonusStudents = new ArrayList();
		for (AttendanceRecord ar : ars) {
			Student s = ar.getStudent();
			if (s != null) {
				int numAttend = ar.getAttendance().size();
				int numPresent = ar.getPresentation().size();
				boolean bonus = numAttend == reqAttend && numPresent == reqPresent;
				StudentAttendancePOJO sa = new StudentAttendancePOJO(s, numAttend, numPresent, bonus);
				if (bonus)
					bonusStudents.add(sa);
				students.add(sa);
			}
		}

		// TODO: Send email to those in bonusStudents
		String fromEmail = "admin@guestbook-tutorial-148615.appspotmail.com";
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromEmail, "AAT Admin"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress("djwesselmail@gmail.com", "Mr. Wessel"));
			msg.setSubject("This is a test email...");
			msg.setText("This is the body of the test...");
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return students;
	}
		
	private List<AttendanceRecord> retrieveRecords(Long courseId) {
		return ObjectifyService.ofy()
			.load()
			.type(AttendanceRecord.class)
			.ancestor(Key.create(Course.class, courseId)).list();
	}

	/**
	 * @param String courseId
	 * @return Course course
	 * Retrieves a Course by Id.
	 **/
	private Course retrieveCourse (Long courseId){
		Course course = ObjectifyService.ofy()
			.load()
			.type(Course.class)
			.id(courseId).now();

		if (course == null)
			throw new ResourceException(404, "Not found", "Cannot find course with id: " + courseId, null);
		return course;
	} 
}
