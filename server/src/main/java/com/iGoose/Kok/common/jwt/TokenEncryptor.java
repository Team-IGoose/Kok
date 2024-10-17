package com.iGoose.Kok.common.jwt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class TokenEncryptor {

    private static final String SECRET_KEY = "6NlyYLg8yoE9hqOEGkAfjmhgu0lLIujX";

    /**
     * AES 인코딩 메소드
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }


    /**
     * AES 디코딩 메소드
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)));
    }

}
