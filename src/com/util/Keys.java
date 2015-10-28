package com.util;

import java.io.PrintStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Keys
{
  public static final String KEY_ALGORITHM = "RSA";
  public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
  private static final String PUBLIC_KEY = "RSAPublicKey";
  private static final String PRIVATE_KEY = "RSAPrivateKey";
  
  public static void main(String[] args)
  {
    try
    {
      Map<String, Object> keyMap = initKey();
      String publicKey = getPublicKey(keyMap);
      EloanCodeUtils.printlog(publicKey);
      String privateKey = getPrivateKey(keyMap);
      EloanCodeUtils.printlog(privateKey);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static String getPublicKey(Map<String, Object> keyMap) throws Exception { Key key = (Key)keyMap.get("RSAPublicKey");
    byte[] publicKey = key.getEncoded();
    return encryptBASE64(key.getEncoded());
  }
  
  public static String getPrivateKey(Map<String, Object> keyMap) throws Exception { Key key = (Key)keyMap.get("RSAPrivateKey");
    byte[] privateKey = key.getEncoded();
    return encryptBASE64(key.getEncoded());
  }
  
  public static byte[] decryptBASE64(String key) throws Exception {
    return new BASE64Decoder().decodeBuffer(key);
  }
  
  public static String encryptBASE64(byte[] key) throws Exception {
    return new BASE64Encoder().encodeBuffer(key);
  }
  
  public static Map<String, Object> initKey() throws Exception {
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
    keyPairGen.initialize(1024);
    KeyPair keyPair = keyPairGen.generateKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
    Map<String, Object> keyMap = new java.util.HashMap(2);
    keyMap.put("RSAPublicKey", publicKey);
    keyMap.put("RSAPrivateKey", privateKey);
    return keyMap; }
  
  public static String publicKeyL = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLD2enOW0YRae6J5IRoETBZOgZEyI+wn8UBUQbFnI2viANTy0EdHAk73+CwXoNYGjgPciWzHkkSW8yRPJtyAL4hev/ERvz5E6igzGYS7r4Ip8KK9mVHbnUZ3VotW876fWwdEYFxWCvcZMW0AD0uYq58MqTVMKFqXNFuHQLmZ3FAwIDAQAB";
  public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMO0o8vYsqInbD/8uraIdWqP8Ycc7KQuLS7w0VbCWocyMRYu582LwzycBOPvbbEKt2feqpUKQ+F3peq+HQnI6gL9d66l0ZG3KjflZTQJ8M847USfUNGVbAi3PJG/NiwQHddUUudmjIEAXwadelp/g+/p97YcBAz8caQDcEyI0AjQIDAQAB";
  public static String privateKeyL = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIsPZ6c5bRhFp7onkhGgRMFk6BkT\nIj7CfxQFRBsWcja+IA1PLQR0cCTvf4LBeg1gaOA9yJbMeSRJbzJE8m3IAviF6/8RG/PkTqKDMZhL\nuvginwor2ZUdudRndWi1bzvp9bB0RgXFYK9xkxbQAPS5irnwypNUwoWpc0W4dAuZncUDAgMBAAEC\ngYAdC82xzzvrWvlK1+MZzWkvIB2exzFa/JNAokz0f37B0nqtbQFRMeUOwfvXWwRccOvXEvCoFvOi\n0bfpPx+IMMP1yDuwsUw5MX3xepGscSCH+uAqf35FOVaf+TDBjdFiGnNFCLSfNTn6Hmq+DS0uvyxe\nOffqQRtAfMiQdxBVo8nAsQJBAMnGOC0N5gM0OqRXMtXm4mYm10rRNvZsAIgdMXPaQ3IfyKPA3MMU\nhMukIrTWssaJQ+cxJqri5WVZ3nszHFaK2HkCQQCwbotiTtNn+SfYz05slzEk/M01D69q1jDIfzGd\nzGb57raABKXEZK1mM9b9bQWrNbkrFyXqrqfDtwMLpKxiaOJbAkBrvsF4rZdJ1rYJ09Z9ipES42LL\nvPSQmhOem5gwuTiS1I4bg31fXrGa3Mb7sjo88kekSQbQ8eB1QowfVaeoPvRhAkAs5LQK28vnVPrS\nrE1kTERxCPKeYdNc+AuJuPPDF1PMnPVLNeOISaXlr6Mw8SDUI+NuF4HKaY6+uj7II6rJj66pAkBG\n+IEn3Gnfs8BMaxfU/v8kO7n5EhCN8wIyXJlqVpMXG4f03P6kcM95ho3UFgNCcKFPwANuNH+CXHRc\nfoA9oC1i";
}
