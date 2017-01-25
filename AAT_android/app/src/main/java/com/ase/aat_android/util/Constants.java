package com.ase.aat_android.util;

/**
 * Created by anahitik on 09.01.17.
 */

public class Constants {

    //public static String AATUrl = "http://localhost:8800/rest/";
    public static String AATUrl = "http://guestbook-149315.appspot.com/rest/";
    public static String loginResourceEndpoint = "user/login";
    public static String userResourceEndpoint = "user";
    public static String coursesRetrieveResourceEndpoint = "courses";

    public static String userIdAttribute = "user_id";

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

    // Duplicates from gae constant.BAD!!
    public static String loginSucceed = "login successfully";

    // Bundle keys
    public static String userIdKey = "userID";
    public static String courseKey = "course";
}
