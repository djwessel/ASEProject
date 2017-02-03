package com.ase.aat_android.util;

/**
 * Created by Yesika on 1/21/2017.
 */

public class EndpointsURL {

    /* Address server where application is deployed */
    public final static String HTTP_ADDRESS="https://guestbook-tutorial-148615.appspot.com";
    //public final static String HTTP_ADDRESS="https://guestbook-149315.appspot.com";

    /* Endpoint to request a QR token given user_id and group_id */
    public final static String REQUEST_QR_CODE = "/rest/user/{user_id}/course/{course_id}/group/{group_id}";

    /* Endpoint to request all groups where a student has attendances */
    public final static String REQUEST_GROUPS_STUDENT="/rest/user/{user_id}/attendances";

    public final static String SIGNUP = "/rest/user";
    public final static String REQUEST_USER_DATA = "/rest/user/{user_id}";
    public final static String LOGIN = "/rest/user/login";
    public final static String LOGOUT = "/rest/user/{user_id}/logout";
    public final static String REQUEST_COURSES = "/rest/courses";
    public final static String REQUEST_COURSE_GROUPS = "/rest/course/{course_id}/groups";
    public final static String CREATE_ATTENDANCE = "/rest/course/{course_id}/group/{group_id}/attendance";
}
