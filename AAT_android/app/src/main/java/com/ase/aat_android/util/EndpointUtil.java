package com.ase.aat_android.util;

/**
 * Created by Yesika on 1/21/2017.
 */

public class EndpointUtil {

    public static String solveUrl (String url, String parameter, String value){
        url = url.replace(parameter, value);
        return url;
    }
}
