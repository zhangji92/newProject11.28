package com.yoka.mrskin.util;

import android.text.TextUtils;

public class ClassPathResource {   
    public static boolean isMobileNO(String mobiles) {   
        //        Pattern p = Pattern   
        //                .compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");   
        String telRegex = "[1][123456789]\\d{9}";
        //        Matcher m = p.matcher(mobiles);   
        //        System.out.println(m.matches() + "---");   
        //        return m.matches();   
        if (TextUtils.isEmpty(mobiles)) return false;  
        else return mobiles.matches(telRegex);  
    }   
}  
