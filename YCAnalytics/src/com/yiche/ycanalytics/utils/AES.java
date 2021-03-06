package com.yiche.ycanalytics.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Java AES
 */
public class AES {
    private SecretKey sSecretKey = null;// key
    private Cipher sCipher = null; // Cipher
    private String sKeyString = "va/SZFAr/EUuygJ=";



    public AES() {
        try {
            sSecretKey = new SecretKeySpec(sKeyString.getBytes(), "AES");
            sCipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // AES/CBC/NoPadding
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * aesEncrypt
     * 加密
     * @param message
     * @return
     */
    public String aesEncrypt(byte[] message) {
        String result = "";
        String newResult = "";
        try {
            sCipher.init(Cipher.ENCRYPT_MODE, sSecretKey);
            byte[] resultBytes = sCipher.doFinal(message);
            result = new String(Base64.encode(resultBytes, Base64.DEFAULT));
            newResult = filter(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newResult;
    }
    
    /**
     * aesEncrypt
     * 加密
     * @param message
     * @return
     */
    public String aesEncrypt(String message) {
        String result = "";
        String newResult = "";
        try {
            sCipher.init(Cipher.ENCRYPT_MODE, sSecretKey);
            byte[] resultBytes = sCipher.doFinal(message.getBytes());
            result = new String(Base64.encode(resultBytes, Base64.DEFAULT));
            newResult = filter(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newResult;
    }

    /**
     * aesDecrypt
     * 解密
     * @param message
     * @return
     */
    public String aesDecrypt(String message) {
        String result = "";
        try {

            byte[] messageBytes = Base64.decode(message, Base64.DEFAULT);
            sCipher.init(Cipher.DECRYPT_MODE, sSecretKey);
            byte[] resultBytes = sCipher.doFinal(messageBytes);
            result = new String(resultBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * aesDecrypt
     * 解密
     * @param message
     * @return
     */
    public byte[] aesDecrypt(byte[] messageBytes) {
    	byte[] resultBytes = null ;
        try {
//            byte[] messageBytes = Base64.decode(message, Base64.DEFAULT);
            sCipher.init(Cipher.DECRYPT_MODE, sSecretKey);
            resultBytes = sCipher.doFinal(messageBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultBytes;
    }

    /**
     * filter
     * 
     * @param str
     * @return
     */
    public static String filter(String str) {
        String output = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            int asc = str.charAt(i);
            if (asc != 10 && asc != 13) {
                sb.append(str.subSequence(i, i + 1));
            }
        }
        output = new String(sb);
        return output;
    }

}