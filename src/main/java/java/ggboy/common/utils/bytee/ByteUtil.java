package java.ggboy.common.utils.bytee;

import java.ggboy.common.constant.BaseConstant;
import java.ggboy.common.enums.ErrorCode;
import java.ggboy.common.exception.DeepException;
import java.nio.charset.Charset;
import java.util.List;

import java.ggboy.common.utils.array.ArrayUtil;
import java.ggboy.common.utils.bean.BeanUtil;

public class ByteUtil {

	private static final byte[] empty_bytes = {};

	public final static byte[] empty() {
		return empty_bytes;
	}

	public static String toString(byte[] data) {
		return toString(data, BaseConstant.charset_utf8);
	}

	public final static byte[] int2Byte(int data) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) ((data >> 24) & 0xFF);
		bytes[1] = (byte) ((data >> 16) & 0xFF);
		bytes[2] = (byte) ((data >> 8) & 0xFF);
		bytes[3] = (byte) (data & 0xFF);
		return bytes;
	}

	public final static byte[] long2Bytes(long data) {
		byte[] bytes = new byte[8];
		bytes[0] = (byte) ((data >> 56) & 0xFF);
		bytes[1] = (byte) ((data >> 48) & 0xFF);
		bytes[2] = (byte) ((data >> 40) & 0xFF);
		bytes[3] = (byte) ((data >> 32) & 0xFF);
		bytes[4] = (byte) ((data >> 24) & 0xFF);
		bytes[5] = (byte) ((data >> 16) & 0xFF);
		bytes[6] = (byte) ((data >> 8) & 0xFF);
		bytes[7] = (byte) (data & 0xFF);
		return bytes;
	}

	public final static int byte2Int(byte[] data) {
		if (BeanUtil.isEmpty(data) || ArrayUtil.getLength(data) != 4) {
			throw new DeepException(ErrorCode.byte_error, "byte length must be 4");
		}
		return ((data[0] & 0xFF) << 24) | ((data[1] & 0xFF) << 16) | ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);
	}

	public final static int bytes2Long(byte[] data) {
		if (data == null || data.length != 8) {
			throw new IllegalArgumentException("length must be 8");
		}
		return ((data[0] & 0xFF) << 56) | ((data[1] & 0xFF) << 48) | ((data[2] & 0xFF) << 40) | ((data[3] & 0xFF) << 32)
				| ((data[4] & 0xFF) << 24) | ((data[5] & 0xFF) << 16) | ((data[6] & 0xFF) << 8) | (data[7] & 0xFF);
	}
	
	public final static long readLong(byte[] data) {
		if (data == null || data.length < 8) {
			throw new IllegalArgumentException("length must be more than 8");
		}
		return ((data[0] & 0xFFL) << 56) | ((data[1] & 0xFFL) << 48) | ((data[2] & 0xFFL) << 40) | ((data[3] & 0xFFL) << 32)
				| ((data[4] & 0xFFL) << 24) | ((data[5] & 0xFFL) << 16) | ((data[6] & 0xFFL) << 8) | (data[7] & 0xFFL);
	}

	/*
	 * Basics Method part
	 * 
	 * Need attention null
	 */

	public static byte[] listArray2Array(List<byte[]> data) {
		if (BeanUtil.isNull(data)) {
			throw new IllegalArgumentException("data cannot be null");
		}
		long length = 0L;
		for (byte[] item : data) {
			length += item.length;
		}

		byte[] byteArray = new byte[Long.bitCount(length)];

		int flag = 0;
		for (byte[] item : data) {
			System.arraycopy(item, 0, byteArray, flag, item.length);
			flag += item.length;
		}

		return byteArray;
	}

	public static byte[] list2Array(List<Byte> data) {
		byte[] byteArray = new byte[data.size()];
		for (int i = 0; i < data.size(); i++) {
			byteArray[i] = data.get(i);
		}
		return byteArray;
	}

	public static String toString(byte[] data, Charset charset) {
		return new String(data, charset);
	}
}