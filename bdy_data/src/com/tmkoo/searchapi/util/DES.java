package com.tmkoo.searchapi.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.tmkoo.searchapi.common.Global;

public class DES {
	private static String Algorithm = "DESede"; 
   
    public static String encryptString(String value) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
        return encryptString(Global.webProperties.DES_KEY, value);
    }
   
   
    public static String encryptString(String key, String value) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
        byte[] bytesKey = null, bytes = null, bytesCipher = null;
        SecretKey deskey = null;
        if (value == null)
            new IllegalArgumentException("待加密字串不允许为空");
        //密码器
        Cipher desCipher = Cipher.getInstance(Algorithm);
        try{
            bytesKey = Base64.decodeBase64(key);
            deskey = new SecretKeySpec(bytesKey, Algorithm);
            bytes = value.getBytes("UTF-8");//待解密的字串
            desCipher.init(Cipher.ENCRYPT_MODE, deskey);//初始化密码器，用密钥deskey,进入解密模式 
            bytesCipher = desCipher.doFinal(bytes);
            String tmp = Base64.encodeBase64String(bytesCipher).trim();
           return  tmp.replaceAll("\\+", "%2b").replaceAll("\\r", "").replaceAll("\\n", "");//url中传输时，+要转化。
        }
        finally{
            bytesKey = null;
            bytes = null;
            bytesCipher = null;
        }
     }
   
    public static String decryptString(String value) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
        return decryptString(Global.webProperties.DES_KEY, value);
    }
   
    public static String decryptString(String key, String value) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
        byte[] bytesKey = null, bytes = null, bytesCipher = null;
        SecretKey deskey = null;
        if (value == null)
            new IllegalArgumentException("待解密字串不允许为空");
        //密码器
        Cipher desCipher = Cipher.getInstance(Algorithm);
        try{
            bytesKey = Base64.decodeBase64(key);
            deskey = new SecretKeySpec(bytesKey, Algorithm);
            bytes = Base64.decodeBase64(value);//加密后的字串
            desCipher.init(Cipher.DECRYPT_MODE, deskey);//初始化密码器，用密钥deskey,进入解密模式 
            bytesCipher = desCipher.doFinal(bytes);
            return (new String(bytesCipher,"UTF-8"));
        }
        finally{
            bytesKey = null;
            bytes = null;
            bytesCipher = null;
        }
     } 
}
