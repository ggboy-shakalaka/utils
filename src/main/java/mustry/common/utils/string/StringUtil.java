package mustry.common.utils.string;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import mustry.common.constant.BaseConstant;
import mustry.common.constant.SymbolConstant;
import mustry.common.utils.array.ArrayUtil;
import mustry.common.utils.bean.BeanUtil;

public class StringUtil {

	public static String toUpperCase(String str) {
		return changeFirstCharacterCase(str, true);
	}

	public static String toLowerCase(String str) {
		return changeFirstCharacterCase(str, false);
	}

	public static byte[] toBytes(String str) {
		return toBytes(str, BaseConstant.charset_utf8);
	}

	public static String toString(Object... data) {
		return ArrayUtil.toString(data, null);
	}

	public final static String[] split(String data, String delimiter) {
		return split(data, delimiter, 0);
	}

	public final static String[] split(String data) {
		return split(data, SymbolConstant.COMMA, 0);
	}

	public final static List<String> toList(String data) {
		return toList(data, SymbolConstant.COMMA);
	}

	public final static List<String> toList(String data, String delimiter) {
		List<String> list = new ArrayList<String>();
		String[] str = StringUtil.split(data, delimiter);
		for (int i = 0; i < ArrayUtil.getLength(str); i++) {
			list.add(str[i]);
		}
		return list;
	}

	/*
	 * Basics Method part
	 * 
	 * is show
	 */

	public static boolean isEmpty(String str) {
		return BeanUtil.isNull(str) || str.length() == 0;
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (BeanUtil.isEmpty(str))
			return str;
		StringBuilder sb = new StringBuilder(str.length());
		if (capitalize)
			sb.append(Character.toUpperCase(str.charAt(0)));
		else
			sb.append(Character.toLowerCase(str.charAt(0)));
		return sb.append(str.substring(1)).toString();
	}

	public static byte[] toBytes(String str, Charset charset) {
		if (BeanUtil.isEmpty(str)) {
			return null;
		}
		return str.getBytes(charset);
	}

	public final static String[] split(String data, String delimiter, int index) {
		if (BeanUtil.isEmpty(data)) {
			return null;
		} else if (BeanUtil.isEmpty(delimiter)) {
			return new String[] { data };
		}
		return data.split(delimiter, index);
	}

	public final static int getLength(String data) {
		if (BeanUtil.isEmpty(data)) {
			return 0;
		}
		return data.length();
	}
	
	public final static String toStringAndClear(StringBuilder sb) {
		String result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}
}
