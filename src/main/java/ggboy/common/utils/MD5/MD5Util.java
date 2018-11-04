package ggboy.common.utils.MD5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ggboy.common.utils.bean.BeanUtil;
import ggboy.common.utils.string.StringUtil;

public class MD5Util {
	public static final char[] DEFAULT_DICIONARY = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
	
	public final static String digest(String data) {
		return digest(StringUtil.toBytes(data), DEFAULT_DICIONARY);
	}
	
	public final static String digest(byte[] data) {
		return digest(data, DEFAULT_DICIONARY);
	}
	
	public final static String digest(byte[] data, String dictionary) {
		return digest(data, dictionary.toCharArray());
	}
	
	public final static String digest(byte[] data, char[] dictionary) {
		try {
			if (BeanUtil.isEmpty(data) || BeanUtil.isEmpty(dictionary)) {
				return null;
			}
			
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(data);
			byte[] temp = messageDigest.digest();
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < temp.length; ++i) {
				sb.append(dictionary[temp[i] >>> 1 & 31]);
				sb.append(dictionary[temp[i] & 31]);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
