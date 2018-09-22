package ggboy.java.common.utils.map;

import java.util.Map;

import ggboy.java.common.utils.bean.BeanUtil;

public class MapUtil {
	public final static boolean isEmpty(Map<?,?> data) {
		return data == null || data.size() == 0;
	}
	
	public final static String toString(Map<?,?> data) {
		if (BeanUtil.isEmpty(data)) {
			return null;
		}
		return data.toString();
	}
}
