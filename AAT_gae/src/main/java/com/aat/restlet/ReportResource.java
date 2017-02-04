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
import com.aat.datastore.User;
import com.aat.datastore.Student;
import com.aat.datastore.Tutor;
import com.aat.utils.Sendgrid;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Key;
import com.google.appengine.labs.repackaged.org.json.JSONException;

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
		User tutor = ResourceUtil.checkTokenPermissions(this, Tutor.class);
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
				boolean bonus = numAttend >= reqAttend && numPresent >= reqPresent;
				StudentAttendancePOJO sa = new StudentAttendancePOJO(s, numAttend, numPresent, bonus);
				if (bonus)
					bonusStudents.add(sa);
				students.add(sa);
			}
		}

		for (StudentAttendancePOJO s : bonusStudents) {
			String message = "Congrats, you qualify for the bonus for the following course: " + c.getTitle();
			sendMail(s.getStudent().getEmail(), c.getTitle() + " Bonus Notificaiton", message);
		}

		String report = "<table><thead><tr><th>First Name</th><th>Last Name</th><th>Email</th>"
			+ "<th>Attendance Count</th><th>Presentation Count</th><th>Recieves Bonus</th></tr></thead><tbody>";
		for (StudentAttendancePOJO s : students) {
			report += "<tr>";
			report += "<td>" + s.getStudent().getFirstName() + "</td>";
			report += "<td>" + s.getStudent().getLastName() + "</td>";
			report += "<td>" + s.getStudent().getEmail() + "</td>";
			report += "<td>" + s.getNumAttend() + "</td>";
			report += "<td>" + s.getNumPresent() + "</td>";
			report += "<td>" + s.getBonus() + "</td>";

			report += "</tr>";
		}
		report += "</tbody></table>";

		sendMail(tutor.getEmail(), c.getTitle() + " Course Report", report);

		return students;
	}

	private void sendMail(String email, String subject, String message) {
		String fromEmail = "admin@guestbook-tutorial-148615.appspotmail.com";
		try {
			// set credentials
			Sendgrid mail = new Sendgrid(System.getenv("SENDGRID_API_KEY"));

			// set email data
			mail.setTo(email).setFrom(fromEmail).setSubject(subject).setText(message).setHtml(message);

			// send your message
			mail.send();
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
