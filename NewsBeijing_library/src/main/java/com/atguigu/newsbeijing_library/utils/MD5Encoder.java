package com.atguigu.newsbeijing_library.utils;

import java.security.MessageDigest;

/**
 * Created by My on 2017/6/7.
 */

public class MD5Encoder {

    /**
     * md5加密
     * @param string
     * @return
     * @throws Exception
     */
    public static String encode(String string) throws Exception {
        byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
