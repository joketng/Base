package com.jointem.fire.utils;

import com.jointem.base.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Description:用于加密解密
 * Created by wyd on 2017/4/18.
 */

public class CipherUtils {
    //将字符串转换成MD5字符串
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException("Huh, MD5 should be supported?", var7);
        } catch (UnsupportedEncodingException var8) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", var8);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        byte[] var6 = hash;
        int var5 = hash.length;
        for (int var4 = 0; var4 < var5; ++var4) {
            byte b = var6[var4];
            if ((b & 255) < 16) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 255));
        }
        return hex.toString();
    }

    //获取密钥
    public static Key getDESKey(byte[] key) throws Exception {
        DESKeySpec des = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(des);
    }

    //解密
    public static String decrypt(String data, Key key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(2, key);
        return new String(cipher.doFinal(StringUtils.hexStringToByteArray(data)), "utf8");
    }

    //加密
    public static String encrypt(String data, Key key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(1, key);
        return StringUtils.byteArrayToHexString(cipher.doFinal(data.getBytes("utf8")));
    }
}
