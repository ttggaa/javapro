package com.kariqu.wssrv.app.util;


import org.springframework.security.crypto.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.Security;


public class AESCoder {

	private static final String ALGORITHM = "AES/ECB/PKCS7Padding";
	public  static final byte[] encryptKey = new byte[] {'A','b', 'c', 'd', 'E', 'f',
		'g', 'H', 'i', 'j', 'k', 'L', '2', '0', '1', '7'};
//		'A', 'b', 'c', 'D',
//			'e', 'f', 'G', 'h', 'I', 'L', 'm', 'n', 'o', 'P', 'Q', 'r' };

	public static String encrypt(byte[] keyValue, String content) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Key key = generateKey(keyValue);
		Cipher c = Cipher.getInstance(ALGORITHM, "BC");
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = c.doFinal(content.getBytes("UTF8"));
		String encryptedValue = new String(Base64.encode(encValue));
		return encryptedValue;
	}

	public static String decrypt(byte[] keyValue, String content) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Key key = generateKey(keyValue);
		Cipher c = Cipher.getInstance(ALGORITHM, "BC");
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = Base64.decode(content.getBytes());
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	private static Key generateKey(byte[] keyValue) throws Exception {
		// System.out.println(keyValue[0]);
		Key key = new SecretKeySpec(keyValue, ALGORITHM);
		// SecretKeyFactory keyFactory =
		// SecretKeyFactory.getInstance(ALGORITHM);
		// key = keyFactory.generateSecret(new DESKeySpec(keyValue));
		return key;
	}

	private static Key generateKey() throws Exception {
		return generateKey(AESCoder.encryptKey);
	}

	public static void main(String[] args) throws Exception {

		String s = URLEncoder.encode("花花", "UTF-8");


		AESCoder c = new AESCoder();

		String encryptedStr = c.encrypt(AESCoder.encryptKey,s);
		System.out.println("Encrypted – " + encryptedStr);

		String decryptedStr = c.decrypt(AESCoder.encryptKey,encryptedStr);
		System.out.println("Decrypted – " + URLDecoder.decode(decryptedStr, "UTF-8"));

		//System.out.println ( c.encrypt(AESCoder.encryptKey,"15824187007") );

	}
}