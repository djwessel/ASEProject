package com.aat.util;

/**
 * Created by Yesika on 1/21/2017.
 */

public class EnpointsURL {

    /* Address server where application is deployed */
    public final static String HTTP_ADDRESS="http://skillful-hope-148521.appspot.com";

    /* Endpoint to request a QR token given user_id and group_id */
    public final static String REQUEST_QR_CODE = "/rest/user/{user_id}/group/{group_id}";

    /* Endpoint to request all groups where a student has attendances */
    public final static String REQUEST_GROUPS_STUDENT="/rest/user/{user_id}/attendances";
}
