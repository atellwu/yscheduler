package com.yeahmobi.yscheduler.common;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptUtils {

    private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    private static final String IV            = "********";

    public static String encode(String key, String data) throws Exception {
        return encode(key, data.getBytes());
    }

    public static String encode(String key, byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

            byte[] bytes = cipher.doFinal(data);
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static byte[] decode(String key, byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            // e.printStackTrace();
            throw new Exception(e);
        }
    }

    public static String decode(String key, String data) throws Exception {
        byte[] datas;
        String value = null;

        datas = decode(key, Base64.decodeBase64(data));

        value = new String(datas);
        if (value.equals("")) {
            throw new Exception();
        }
        return value;
    }
}
