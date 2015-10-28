package com.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAUtil
{
  private static Cipher cipher;
  
  static
  {
    try
    {
      cipher = Cipher.getInstance("RSA");
    } catch (java.security.NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    }
  }
  



  public static Map<String, String> generateKeyPair(String filePath)
  {
    try
    {
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
      
      keyPairGen.initialize(1024);
      
      KeyPair keyPair = keyPairGen.generateKeyPair();
      
      PublicKey publicKey = (java.security.interfaces.RSAPublicKey)keyPair.getPublic();
      
      PrivateKey privateKey = (java.security.interfaces.RSAPrivateKey)keyPair.getPrivate();
      
      String publicKeyString = getKeyString(publicKey);
      
      String privateKeyString = getKeyString(privateKey);
      
      FileWriter pubfw = new FileWriter(filePath + "/publicKey.keystore");
      FileWriter prifw = new FileWriter(filePath + "/privateKey.keystore");
      BufferedWriter pubbw = new BufferedWriter(pubfw);
      BufferedWriter pribw = new BufferedWriter(prifw);
      pubbw.write(publicKeyString);
      pribw.write(privateKeyString);
      pubbw.flush();
      pubbw.close();
      pubfw.close();
      pribw.flush();
      pribw.close();
      prifw.close();
      
      Map<String, String> map = new java.util.HashMap();
      map.put("publicKey", publicKeyString);
      map.put("privateKey", privateKeyString);
      return map;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  






  public static PublicKey getPublicKey(String key)
    throws Exception
  {
    byte[] keyBytes = new BASE64Decoder().decodeBuffer(key);
    java.security.spec.X509EncodedKeySpec keySpec = new java.security.spec.X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(keySpec);
    return publicKey;
  }
  






  public static PrivateKey getPrivateKey(String key)
    throws Exception
  {
    byte[] keyBytes = new BASE64Decoder().decodeBuffer(key);
    java.security.spec.PKCS8EncodedKeySpec keySpec = new java.security.spec.PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
    return privateKey;
  }
  



  public static String getKeyString(java.security.Key key)
    throws Exception
  {
    byte[] keyBytes = key.getEncoded();
    String s = new BASE64Encoder().encode(keyBytes);
    return s;
  }
  




  public static String encrypt(PublicKey publicKey, String plainText)
  {
    try
    {
      cipher.init(1, publicKey);
      byte[] enBytes = cipher.doFinal(plainText.getBytes());
      
      return new BASE64Encoder().encode(enBytes);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }
  




  public static String encrypt(String publicKeystore, String plainText)
  {
    try
    {
      FileReader fr = new FileReader(publicKeystore);
      BufferedReader br = new BufferedReader(fr);
      String publicKeyString = "";
      String str;
      while ((str = br.readLine()) != null) {
        publicKeyString = publicKeyString + str;
      }
      br.close();
      fr.close();
      cipher.init(1, getPublicKey(publicKeyString));
      byte[] enBytes = cipher.doFinal(plainText.getBytes());
      return new BASE64Encoder().encode(enBytes);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  




  public static String decrypt(PrivateKey privateKey, String enStr)
  {
    try
    {
      cipher.init(2, privateKey);
      byte[] deBytes = cipher.doFinal(new BASE64Decoder().decodeBuffer(enStr));
      return new String(deBytes);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  




  public static String decrypt(String privateKeystore, String enStr)
  {
    try
    {
      FileReader fr = new FileReader(privateKeystore);
      BufferedReader br = new BufferedReader(fr);
      String privateKeyString = "";
      String str;
      while ((str = br.readLine()) != null) {
        privateKeyString = privateKeyString + str;
      }
      br.close();
      fr.close();
      cipher.init(2, getPrivateKey(privateKeyString));
      byte[] deBytes = cipher.doFinal(new BASE64Decoder().decodeBuffer(enStr));
      return new String(deBytes);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public static void main(String[] ss) throws Exception
  {
    String privateKey = Keys.privateKeyL;
    String publicKey = Keys.publicKey;
    String sss = encrypt(getPublicKey(publicKey), it.sauronsoftware.base64.Base64.encode(""));
    

    String s1 = decrypt(getPrivateKey(privateKey), "hSPjTziUGEONZ5EJgQ6atOiowDv7Q+fjjVFQzc75UM9bwNKZRjR6o0JcRvR6qui6TvCmz3g3U7mymsNhFfL4B0k9rRQyRCBsrW09hxiyFGCqpwDqcnR1SW4dGYbK3XTDiqzAF6xJFetPyp8arimDqrWIm6Uw+pt8iVhihbP3Qjo=");
    
    EloanCodeUtils.printlog(it.sauronsoftware.base64.Base64.decode(s1));
    EloanCodeUtils.printlog(s1);
  }
}
