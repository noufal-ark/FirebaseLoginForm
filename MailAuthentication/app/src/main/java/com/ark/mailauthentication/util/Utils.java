package com.ark.mailauthentication.util;

/**
 * Created by noufal on 23/2/18.
 */

public class Utils {

    //Email Validation pattern
    public static final String REG_EX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
    //Fragments Tags
    public static final String LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
    public static final String SIGN_UP_FRAGMENT = "SIGN_UP_FRAGMENT";
    public static final String FORGOT_PASS_FRAGMENT = "FORGOT_PASS_FRAGMENT";
    private Utils() {
        //Do nothing
    }

}
