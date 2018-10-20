/**
 * 
 */
package com.test;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.sun.mail.util.BASE64DecoderStream;

/**
 * @author IBM_ADMIN
 *
 */
public class Sample {

	// Passphrase
	private static final byte[] pass = new byte[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0','1', '2', '3', '4', '5' };


	public static String encrypt(String Data) throws Exception {
	    Key key = generateKey();
	    Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
	    c.init(Cipher.ENCRYPT_MODE, key);
	    byte[] encVal = c.doFinal(Data.getBytes());
	    String encryptedValue = DatatypeConverter.printBase64Binary(encVal);
	    return encryptedValue;
	}

	public static String decrypt(String encryptedData) throws Exception {
	    Key key = generateKey();
	    Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
	    c.init(Cipher.DECRYPT_MODE, key);
//	    byte[] decordedValue = DatatypeConverter.;
//	    byte[] decValue = c.doFinal(decordedValue);
	    String decryptedValue = "";
	    return decryptedValue;
	}

	private static Key generateKey() throws Exception {
	    Key key = new SecretKeySpec(pass, "AES");
	    return key;
	}
}
