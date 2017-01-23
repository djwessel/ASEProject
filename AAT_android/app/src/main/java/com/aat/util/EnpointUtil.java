package com.aat.util;

import java.util.regex.Pattern;

/**
 * Created by Yesika on 1/21/2017.
 */

public class EnpointUtil {

    public static String solveUrl (String url, String parameter, String value){
        url = url.replace("{"+parameter+"}",value);
        return url;
    }
}
