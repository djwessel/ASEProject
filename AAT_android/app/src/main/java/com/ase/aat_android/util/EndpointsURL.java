package com.ase.aat_android.util;

/**
 * Created by Yesika on 1/21/2017.
 */

public class EndpointsURL {

    /* Endpoints' url attributes names */
    public final static String user_id = "{user_id}";
    public final static String course_id = "{course_id}";
    public final static String group_id = "{group_id}";


    private final static String full_user_id = "/user/" + user_id;
    private final static String full_course_id = "/course/" + course_id;
    private final static String full_group_id = "/group/" + group_id;

    /* Request parameter names */
    public final static String user = "user";

    /* Address server where application is deployed */
    public final static String HTTP_ADDRESS="https://guestbook-tutorial-148615.appspot.com";
    //public final static String HTTP_ADDRESS="https://guestbook-149315.appspot.com";

    /* Endpoint to request a QR token given user_id and group_id */
    public final static String REQUEST_QR_CODE = "/rest"+full_user_id + full_course_id + full_group_id;

    /* Endpoint to request all groups where a student has attendances */
    public final static String REQUEST_GROUPS_STUDENT="/rest" + full_user_id + "/attendances";

    public final static String SIGNUP = "/rest/user";
    public final static String REQUEST_USER_DATA = "/rest" + full_user_id;
    public final static String LOGIN = "/rest/user/login";
    public final static String LOGOUT = "/rest" + full_user_id + "/logout";
    public final static String REQUEST_COURSES = "/rest/courses";
    public final static String REQUEST_COURSE_GROUPS = "/rest" + full_course_id + "/groups";
    public final static String CREATE_ATTENDANCE = "/rest" + full_course_id + full_group_id + "/attendance";
    public final static String DELETE_ATTENDANCE = "/rest" + full_user_id + full_course_id + full_group_id + "/attendance";
}
