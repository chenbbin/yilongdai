package com.util;

import java.io.PrintStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RSAUtils
{
	private static final String modulus1 = "00c77120e32372be73305facd03732dce15b7ad05521b0e115773441061fbaea6bee369d7a6d0691eb9caf16553032644865aba21023ce6e3010a04dba37aa62b9eabfd63f092fc37a4ed1e740a8eb189cbfc81f0079e3a0a22704ada9bace6ddc54912fabeec04adaca744ef8a15c31822e0da5d6f1dd90bb0d925c36c507d7e7";
	private static final String pub_exponent = "010001";
	private static final String pri_exponent = "238bd6ee2d6a9258f597f133230e27773ab18bb9eedc262798deeed65556986fdfa64f23b83818747f3bf5425815694a89bc8810433665d49ecc98efca27c6c1c1a2cf8a1001b4ebbb42dba86e5f5f634eab6560f91ffb55b178f46e13706f0721e5c5d5e2425908936266027e05b71489c2562ccbebc88d26f3677278000ec1";

	public static HashMap<String, Object> getKeys() throws java.security.NoSuchAlgorithmException
	{
		HashMap<String, Object> map = new HashMap();
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
		map.put("public", publicKey);
		map.put("private", privateKey);
		return map;
	}









	public static RSAPublicKey getPublicKey(String modulus, String exponent)
	{
		try
		{
			BigInteger b1 = new BigInteger(modulus, 16);

			BigInteger b2 = new BigInteger(exponent, 16);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
			java.security.spec.RSAPublicKeySpec keySpec = new java.security.spec.RSAPublicKeySpec(b1, b2);
			return (RSAPublicKey)keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace(); }
		return null;
	}









	public static RSAPrivateKey getPrivateKey(String modulus, String exponent)
	{
		try
		{
			BigInteger b1 = new BigInteger(modulus, 16);
			BigInteger b2 = new BigInteger(exponent, 16);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
			java.security.spec.RSAPrivateKeySpec keySpec = new java.security.spec.RSAPrivateKeySpec(b1, b2);
			return (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace(); }
		return null;
	}









	public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
			throws Exception
			{
		Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
		cipher.init(1, publicKey);

		int key_len = publicKey.getModulus().bitLength() / 8;

		String[] datas = splitString(data, key_len - 11);
		String mi = "";

		for (String s : datas) {
			mi = mi + bcd2Str(cipher.doFinal(s.getBytes()));
		}
		return mi;
			}








	public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
			throws Exception
			{
		Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
		cipher.init(2, privateKey);

		int key_len = privateKey.getModulus().bitLength() / 8;
		byte[] bytes = data.getBytes();
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		System.err.println(bcd.length);

		String ming = "";
		byte[][] arrays = splitArray(bcd, key_len);
		for (byte[] arr : arrays) {
			ming = ming + new String(cipher.doFinal(arr));
		}
		return ming;
			}



	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len)
	{
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[(j++)]);
			bcd[i] = ((byte)((j >= asc_len ? 0 : asc_to_bcd(ascii[(j++)])) + (bcd[i] << 4)));
		}
		return bcd;
	}

	public static byte asc_to_bcd(byte asc) {
		byte bcd;
		if ((asc >= 48) && (asc <= 57)) {
			bcd = (byte)(asc - 48); } else {
				if ((asc >= 65) && (asc <= 70)) {
					bcd = (byte)(asc - 65 + 10); } else {
						if ((asc >= 97) && (asc <= 102)) {
							bcd = (byte)(asc - 97 + 10);
						} else
							bcd = (byte)(asc - 48); } }
		return bcd;
	}



	public static String bcd2Str(byte[] bytes)
	{
		char[] temp = new char[bytes.length * 2];

		for (int i = 0; i < bytes.length; i++) {
			char val = (char)((bytes[i] & 0xF0) >> 4 & 0xF);
			temp[(i * 2)] = ((char)(val > '\t' ? val + 'A' - 10 : val + '0'));

			val = (char)(bytes[i] & 0xF);
			temp[(i * 2 + 1)] = ((char)(val > '\t' ? val + 'A' - 10 : val + '0'));
		}
		return new String(temp);
	}



	public static String[] splitString(String string, int len)
	{
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if ((i == x + z - 1) && (y != 0)) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}



	public static byte[][] splitArray(byte[] data, int len)
	{
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];

		for (int i = 0; i < x + z; i++) {
			byte[] arr = new byte[len];
			if ((i == x + z - 1) && (y != 0)) {
				System.arraycopy(data, i * len, arr, 0, y);
			} else {
				System.arraycopy(data, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}

	public static String getKeyString(java.security.Key key) throws Exception {
		byte[] keyBytes = key.getEncoded();
		String s = new sun.misc.BASE64Encoder().encode(keyBytes);
		return s;
	}





	public static String encryptPassword(String password)
			throws Exception
			{
		RSAPublicKey rsaPublicKey = getPublicKey("00c77120e32372be73305facd03732dce15b7ad05521b0e115773441061fbaea6bee369d7a6d0691eb9caf16553032644865aba21023ce6e3010a04dba37aa62b9eabfd63f092fc37a4ed1e740a8eb189cbfc81f0079e3a0a22704ada9bace6ddc54912fabeec04adaca744ef8a15c31822e0da5d6f1dd90bb0d925c36c507d7e7", "010001");
		StringBuilder stringBuilder = new StringBuilder();
		password = stringBuilder.append(password).reverse().toString();
		String ePassword = encryptByPublicKey(password, rsaPublicKey);
		return ePassword.toLowerCase();
			}

	public static void main(String[] args) throws Exception
	{
		HashMap<String, Object> map = getKeys();

		RSAPublicKey publicKey = (RSAPublicKey)map.get("public");
		RSAPrivateKey privateKey = (RSAPrivateKey)map.get("private");


		String modulus = publicKey.getModulus().toString();

		String public_exponent = publicKey.getPublicExponent().toString();

		String private_exponent = privateKey.getPrivateExponent().toString();

		String modulus_16 = "00c77120e32372be73305facd03732dce15b7ad05521b0e115773441061fbaea6bee369d7a6d0691eb9caf16553032644865aba21023ce6e3010a04dba37aa62b9eabfd63f092fc37a4ed1e740a8eb189cbfc81f0079e3a0a22704ada9bace6ddc54912fabeec04adaca744ef8a15c31822e0da5d6f1dd90bb0d925c36c507d7e7";
		String public_exponent_16 = "010001";
		String private_exponent_16 = "238bd6ee2d6a9258f597f133230e27773ab18bb9eedc262798deeed65556986fdfa64f23b83818747f3bf5425815694a89bc8810433665d49ecc98efca27c6c1c1a2cf8a1001b4ebbb42dba86e5f5f634eab6560f91ffb55b178f46e13706f0721e5c5d5e2425908936266027e05b71489c2562ccbebc88d26f3677278000ec1";
		EloanCodeUtils.printlog("modulus:" + modulus_16);
		EloanCodeUtils.printlog("public_exponent:" + public_exponent_16);
		EloanCodeUtils.printlog("private_exponent:" + private_exponent_16);
		String ming = "123456789";
		StringBuilder stringBuilder = new StringBuilder();
		ming = stringBuilder.append(ming).reverse().toString();

		RSAPublicKey pubKey = getPublicKey(modulus_16, public_exponent_16);
		RSAPrivateKey priKey = getPrivateKey(modulus_16, private_exponent_16);

		String mi = encryptByPublicKey(ming, pubKey);

		System.err.println("密:" + mi);


		ming = decryptByPrivateKey(mi, priKey);
		System.err.println("明:" + ming);
	}
}
