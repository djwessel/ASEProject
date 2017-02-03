package com.aat.pojos;

import com.aat.datastore.Student;

public class StudentAttendancePOJO {
	
	private Student s;
	private int numAttend;
	private int numPresent;
	private boolean bonus;
	
	public StudentAttendancePOJO(Student s, int numAttend, int numPresent, boolean bonus){
		this.s = s;
		this.numAttend = numAttend;
		this.numPresent = numPresent;
		this.bonus = bonus;
	}
	
	public Student getStudent() { return s; }
	public void setStudent(Student s) { this.s = s; }
	public int getNumAttend() { return numAttend; }
	public void setNumAttend(int numAttend) { this.numAttend = numAttend; }
	public int getNumPresent() { return numPresent; }
	public void setNumPresent(int numPresent) { this.numPresent = numPresent; }
	public boolean getBonus() { return bonus; }
	public void setBonus(boolean bonus) { this.bonus = bonus; }
}
