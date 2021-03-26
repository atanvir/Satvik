package com.satvick.utils;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by Dev-PC on 2/25/2018.
 */

public class Utility {

    public final static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
