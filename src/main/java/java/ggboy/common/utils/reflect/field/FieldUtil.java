package java.ggboy.common.utils.reflect.field;

import java.lang.reflect.Field;

import java.ggboy.common.utils.bean.BeanUtil;

public class FieldUtil {
	
	public final static Field[] getDeclaredFields(Object data) {
		return getDeclaredFields(data.getClass());
	}
	
	/*
	 * Basics Method part
	 * 
	 * Need attention null
	 */
	
	public final static String getName(Field data) {
		if (BeanUtil.isEmpty(data)) {
			return null;
		}
		return data.getName();
	}

	public final static Field[] getDeclaredFields(Class<?> clazz) {
		if (BeanUtil.isEmpty(clazz)) {
			return null;
		}
		return clazz.getDeclaredFields();
	}
}
