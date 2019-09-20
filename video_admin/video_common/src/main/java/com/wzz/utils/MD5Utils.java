package com.wzz.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * 对字符串进行MD5加密
     */
    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String newStr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
        return newStr;
    }

    public static void main(String[] args) {
        String str = null;
        try {
            str = getMD5Str("wzz");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println(str);
    }

}
