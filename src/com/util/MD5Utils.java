package com.util;

import java.security.MessageDigest;

public class MD5Utils
{
  public static String toMd5(byte[] bytes)
  {
    try {
      MessageDigest algorithm = MessageDigest.getInstance("MD5");
      algorithm.reset();
      algorithm.update(bytes);
      return toHexString(algorithm.digest(), "");
    } catch (java.security.NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static String toHexString(byte[] bytes, String separator)
  {
    StringBuilder hexString = new StringBuilder();
    for (byte b : bytes) {
      hexString.append(Integer.toHexString(0xFF & b)).append(separator);
    }
    
    return hexString.toString();
  }
  



  public static String string2MD5(String inStr)
  {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (Exception e) {
      EloanCodeUtils.printlog(e.toString());
      e.printStackTrace();
      return "";
    }
    char[] charArray = inStr.toCharArray();
    byte[] byteArray = new byte[charArray.length];
    
    for (int i = 0; i < charArray.length; i++)
      byteArray[i] = ((byte)charArray[i]);
    byte[] md5Bytes = md5.digest(byteArray);
    StringBuffer hexValue = new StringBuffer();
    for (int i = 0; i < md5Bytes.length; i++) {
      int val = md5Bytes[i] & 0xFF;
      if (val < 16)
        hexValue.append("0");
      hexValue.append(Integer.toHexString(val));
    }
    return hexValue.toString();
  }
}
