package mustry.common.utils.array;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import mustry.common.constant.BaseConstant;
import mustry.common.context.StringBuilderContext;
import mustry.common.utils.string.StringUtil;

public class ArrayUtil {

	public final static <T> T getFirst(T[] data) {
		if (data == null) {
			throw new NullPointerException("data cannot be null");
		}

		return data[0];
	}

	public final static String toString(Object[] data) {
		return toString(data, null);
	}

	public final static String toSplitString(Object[] data) {
		return toString(data, BaseConstant.default_delimiter);
	}

	/*
	 * Basics Method part
	 * 
	 * Need attention null
	 */

	public final static boolean isEmpty(Object[] data) {
		return data == null || data.length == 0;
	}

	public final static int getLength(Object[] data) {
		if (data == null) {
			throw new NullPointerException("data cannot be null");
		}
		return data.length;
	}

	public final static int getLength(byte[] bytes) {
		if (bytes == null) {
			throw new NullPointerException("data cannot be null");
		}
		return bytes.length;
	}
	
	public final static boolean isSameLength(Object[] data1, Object[] data2) {
		if (data1 == data2) {
			return true;
		}
		if (data1 == null || data2 == null) {
			return false;
		}
		return data1.length == data2.length;
	}

	public final static <T> List<T> toList(T[] data) {
		return Arrays.asList(data);
	}

	@SuppressWarnings("unchecked")
	public final static <T> T[] newArray(Class<T> clazz, int size) {
		if (clazz == null) {
			throw new NullPointerException("class cannot be null");
		}

		return (T[]) Array.newInstance(clazz, size);
	}

	public static String toString(Object[] data, String delimiter) {
		if (ArrayUtil.isEmpty(data)) {
			return null;
		}

		StringBuilder sb = StringBuilderContext.getContext();
		for (int i = 0; i < data.length; i++) {
			if (sb.length() != 0 && !StringUtil.isEmpty(delimiter)) {
				sb.append(delimiter);
			}
			sb.append(data[i].toString());
		}

		return sb.toString();
	}
	
	public final static <T> T[] merge(T[] array1, T[] array2) {
		boolean array1Flag = ArrayUtil.isEmpty(array1);
		boolean array2Flag = ArrayUtil.isEmpty(array2);
		if (array1Flag && array2Flag) {
			return null;
		}
		if (array1Flag) {
			return array2;
		}
		if (array2Flag) {
			return array1;
		}
		T[] array = Arrays.copyOf(array1, array1.length + array2.length);
		System.arraycopy(array2, 0, array, array1.length, array2.length);  
		return array;
	}
	
	public final static byte[] merge(byte[] array1, byte[] array2) {
		return merge(array1, array2, 0, array2.length);
	}
	
	public final static byte[] merge(byte[] array1, byte[] array2, int off, int length) {
		if (array1 == null && array2 == null) {
			throw new NullPointerException("array can not be null");
		}
		if (array2 == null) {
			return array1;
		}
		if (off + length > array2.length) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (array1 == null) {
			return Arrays.copyOfRange(array2, off, off + length);
		}
		byte[] array = Arrays.copyOf(array1, array1.length + length);
		System.arraycopy(array2, off, array, array1.length, length);
		return array;
	}
	
	public final static byte[] subArray(byte[] data, int start, int end) {
		if (data == null) {
			throw new NullPointerException("data cannot be null");
		}
		return Arrays.copyOfRange(data, start, end);
	}
}
