package com.aat.utils;

public class Constants {
	// String used in URl
	public final static String course = "course";
	public final static String courseID = "course_id";
	public final static String group = "group";
	public final static String groupId = "group_id";
	public final static String userId = "user_id";
	
	// Exception messages
	public final static String noGroupMsg = "Group does not exist";
	public final static String incorrectRequestFormat = "Incorrect request format.";
	public final static String missingParam = "Missing required parameter: ";
	public final static String missingAttribute = "Missing required attribute: ";

	// Authentication
	public final static boolean ON_HTTPS = false;		// Set to true on deployment
	public final static int TIMEOUT_SECONDS = 3600;
	public final static String ALGORITHM = "PBKDF2WithHmacSHA1";
}
