package com.know.wenda.util;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5Util
 *
 * @author shunhua
 */
public class MD5Util {
    /**
     *  私有化构造器
     */
    private MD5Util() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * 获取加密后的字符串
     *
     * @param value 待加密的字符串
     * @return
     */
    public static String getMD5String(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] e = md.digest(value.getBytes());
            return toHexString(e);
        } catch (NoSuchAlgorithmException e) {
            return StringUtils.EMPTY;
        }
    }


    /**
     * 字节码转字符串
     * @param bytes
     * @return
     */
    private static String toHexString(byte[] bytes) {

        StringBuilder hs = new StringBuilder();
        for (int n = 0; n < bytes.length; n++) {
            String tmp = Integer.toHexString(bytes[n] & 0xff);
            if (tmp.length() == 1) {
                hs.append("0").append(tmp);
            } else {
                hs.append(tmp);
            }
        }

        return hs.toString();
    }
}