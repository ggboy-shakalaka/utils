package mustry.common.utils.list;

import java.util.List;

import mustry.common.constant.BaseConstant;
import mustry.common.utils.array.ArrayUtil;
import mustry.common.utils.bean.BeanUtil;

public class ListUtil {
	
	
	public final static Object[] toArray(List<?> data) {
		return toArray(data, Object.class);
	}
	
	public static String toString(List<?> data) {
		return ArrayUtil.toString(toArray(data), null);
	}

	public static String toSplitString(List<?> data) {
		return ArrayUtil.toString(toArray(data), BaseConstant.default_delimiter);
	}

	public static String toSplitString(List<?> data, String delimiter) {
		return ArrayUtil.toString(toArray(data), delimiter);
	}
	
	/*
	 * Basics Method part 
	 * 
	 * Need attention null
	 */
	
	public final static boolean isEmpty(List<?> data) {
		return BeanUtil.isNull(data) || data.size() == 0;
	}

	public final static int getLength(List<?> data) {
		if (BeanUtil.isEmpty(data)) {
			return 0;
		}
		return data.size();
	}
	
	public final static <T> T get(List<T> data, int index) {
		if (BeanUtil.isEmpty(data) || index > data.size()) {
			return null;
		}
		return data.get(index);
	}

	public final static <T> T[] toArray(List<? extends T> data, Class<T> clazz) {
		if (BeanUtil.isEmpty(data)) {
			return null;
		}
		return data.toArray(ArrayUtil.newArray(clazz, data.size()));
	}
}
