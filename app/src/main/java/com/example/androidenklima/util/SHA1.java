package com.example.androidenklima.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SHA1 {
    public static String getSHA1(String msg) {
        String sha1;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(msg.getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return sha1;

    }
}
