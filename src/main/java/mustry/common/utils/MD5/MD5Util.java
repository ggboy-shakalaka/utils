package mustry.common.utils.MD5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mustry.common.utils.bean.BeanUtil;

public class MD5Util {
	public static final char[] DEFAULT_DICIONARY = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
	
	/**
	 * MD5 摘要
	 * @param data
	 * @param dictionary
	 * @return
	 */
	public final static String digest(byte[] data, String dictionary) {
		return digest(data, dictionary.toCharArray());
	}
	
	/**
	 * MD5 摘要
	 * @param data
	 * @param dictionary
	 * @return
	 */
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
