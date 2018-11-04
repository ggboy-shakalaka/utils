package java.ggboy.common.utils.base64;

import org.springframework.util.Base64Utils;

import java.ggboy.common.utils.string.StringUtil;
import java.ggboy.common.utils.bytee.ByteUtil;

public class Base64Util {
	public final static byte[] encode(byte[] data) {
		return Base64Utils.encode(data);
	}
	
	public final static byte[] encode(String data) {
		return encode(StringUtil.toBytes(data));
	}
	
	public final static String encodeToString(byte[] data) {
		return ByteUtil.toString(encode(data));
	}
	
	public final static String encodeToString(String data) {
		return ByteUtil.toString(encode(data));
	}
	
	public final static byte[] decode(byte[] data) {
		return Base64Utils.decode(data);
	}
	
	public final static byte[] decode(String data) {
		return decode(StringUtil.toBytes(data));
	}
	
	public final static String decodeToString(byte[] data) {
		return ByteUtil.toString(decode(data));
	}
	
	public final static String decodeToString(String data) {
		return ByteUtil.toString(decode(data));
	}
}
