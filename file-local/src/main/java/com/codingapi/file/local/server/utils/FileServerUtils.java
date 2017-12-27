package com.codingapi.file.local.server.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by lorne on 2017/12/27
 */
public class FileServerUtils {

    public static boolean isCutImgName(String name){
        String ipAddressRegex = ".*(_)[0-9]*(x)[0-9]*[.][a-zA-Z]*";
        Pattern ipAddressPattern = Pattern.compile(ipAddressRegex);
        Matcher matcher = ipAddressPattern.matcher(name);
        return matcher.matches();
    }

}
