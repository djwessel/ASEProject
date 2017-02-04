package com.ase.aat_android.util;

/**
 * Created by anahitik on 09.01.17.
 */

public class Constants {

    public static String emailParamName = "email";
    public static String passwordParamName = "password";
    public static String firstnameParamName = "first";
    public static String lastnameParamName = "last";
    public static String userTypePramName = "type";
    // Maybe this is insecure
    public static String userType = "student";

    // Load messages
    public static  String loading = "Loading...";
    public static String loginLoad = "Login...";
    public static String logoutLoad = "Logout...";
    public static String signupLoad = "Signing up...";
    public static  String registerLoad = "Registering...";
    public static  String deregisterLoad = "De-registering...";

    // Request completed messages
    public static String registered = "Successfully registered";
    public static String deregistered = "Successfully de-registered";

    // Bundle keys
    public static String userIdKey = "userID";
    public static String courseKey = "course";
    public static String groupKey = "group";

    // HashMap keys for objects retrieved from datastore
    // Maybe it would be better to have this constants from gae side. It will decrease duplication
    // and may prevent possible inconsistency.
    public static String group = "group";
    public static String courseId = "courseId";
    public static String id = "id";
    public  static String name = "name";
    public  static String courseName = "courseName";

    public static String sessionToken = "sessionToken";

}
