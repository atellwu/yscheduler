package com.yeahmobi.yscheduler.common;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * 安全工具类
 */
public final class HmacSha1Util {

    private static final String  SECRET_KEY        = "yscheduler";

    public static final String   HMAC_SHA1         = "HmacSHA1";

    private static final String  CHARSET_NAME_UTF8 = "utf-8";

    private static SecretKeySpec signingKey;
    static {
        try {
            signingKey = new SecretKeySpec(SECRET_KEY.getBytes(CHARSET_NAME_UTF8), HMAC_SHA1);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 大约0.28ms
     */
    public static String hmacSha1(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
            mac.update(data.getBytes(CHARSET_NAME_UTF8));
            return Hex.encodeHexString(mac.doFinal());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
