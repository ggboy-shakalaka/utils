package java.ggboy.common.utils.RSA;

import java.util.Map;

import java.ggboy.common.exception.CommonUtilException;
import java.ggboy.common.utils.base64.Base64Util;
import java.ggboy.common.utils.string.StringUtil;

public class StringRSA extends BaseRSA {
	private Map<String, Object> keyMap;

	public static String sign(String str, String privateKey) throws CommonUtilException {
		return encodeToString(sign(StringUtil.toBytes(str), decode(privateKey)));
	}

	public static boolean verify(String str, String sign, String publicKey) throws CommonUtilException {
		return verify(StringUtil.toBytes(str), decode(sign), decode(publicKey));
	}

	public static String encryptByPublicKey(String data, String key) throws CommonUtilException {
		return encodeToString(encryptByPublicKey(encode(data), decode(key)));
	}

	public static String decryptByPrivateKey(String data, String key) throws CommonUtilException {
		return decodeToString(decryptByPrivateKey(decode(data), decode(key)));
	}
	
	public static String encryptByPrivateKey(String data, String key) throws CommonUtilException {
		return encodeToString(encryptByPrivateKey(encode(data), decode(key)));
	}

	public static String decryptByPublicKey(String data, String key) throws CommonUtilException {
		return decodeToString(decryptByPublicKey(decode(data), decode(key)));
	}
	
	private static byte[] encode(String data) {
		return Base64Util.encode(data);
	}
	
	private static String encodeToString(byte[] data) {
		return Base64Util.encodeToString(data);
	}

	private static byte[] decode(String data) {
		return Base64Util.decode(data);
	}
	
	private static String decodeToString(byte[] data) {
		return Base64Util.decodeToString(data);
	}

	// ------------------------------------------- static 方法分割线

	public String getPublicKey() throws CommonUtilException {
		if (keyMap == null)
			gainKeyMap();
		return encodeToString(super.getPublicKey(keyMap));
	}

	public String getPrivateKey() throws CommonUtilException {
		if (keyMap == null)
			gainKeyMap();
		return encodeToString(super.getPrivateKey(keyMap));
	}

	public Map<String, Object> getKeyMap() throws CommonUtilException {
		if (keyMap == null)
			gainKeyMap();
		return keyMap;
	}

	private void gainKeyMap() throws CommonUtilException {
		this.keyMap = genKeyPair();
	}
}