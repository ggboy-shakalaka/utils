package ggboy.common.utils.bean;

import ggboy.common.enums.ErrorCode;
import ggboy.common.exception.CommonUtilException;
import ggboy.common.utils.array.ArrayUtil;
import ggboy.common.utils.clazz.ClassUtil;
import ggboy.common.utils.list.ListUtil;
import ggboy.common.utils.map.MapUtil;
import ggboy.common.utils.reflect.field.FieldUtil;
import ggboy.common.utils.serialize.SerializableUtil;
import ggboy.common.utils.string.StringUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class BeanUtil {
	/*
	 * bean 需要实现序列化 - Serializable
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deepCloneBean(T obj) throws CommonUtilException {
		return (T) SerializableUtil.reverseSerialize(SerializableUtil.serialize(obj));
	}

	public static boolean isBeanEqual(Object... beans) {
		Field[] field = FieldUtil.getDeclaredFields(ArrayUtil.getFirst(beans));
		for (int i = 0; i < ArrayUtil.getLength(field); i++)
			try {
				Object[] data = new Object[ArrayUtil.getLength(beans)];
				for (int j = 0; j < ArrayUtil.getLength(beans); j++) {
					data[j] = getValueByAttributeName(beans[j], FieldUtil.getName(field[i]));
				}
				if (!equal(data)) {
					return false;
				}
			} catch (Exception e) {
				// 异常不做处理
			}
		return true;
	}

	/**
	 * 调用对象 obj - attributeName 属性的 getter
	 * 
	 * @author switch2018@outlook.com
	 * @param  obj
	 * @param  attributeName
	 * @return result
	 * @throws CommonUtilException
	 */
	public final static Object getValueByAttributeName(Object obj, String attributeName) throws CommonUtilException {
		return invoke(obj, StringUtil.toString("get", StringUtil.toUpperCase(attributeName)));
	}

	/**
	 * 调用对象 obj - attributeName 属性的 setter
	 * 
	 * @author switch2018@outlook.com
	 * @param  obj
	 * @param  attributeName
	 * @param  args
	 * @throws CommonUtilException
	 */
	public final static void setValueByAttributeName(Object obj, String attributeName, Object... args)
			throws CommonUtilException {
		invoke(obj, StringUtil.toString("set", StringUtil.toUpperCase(attributeName)), args);
	}

	/**
	 * 执行 obj - methodName方法
	 * 
	 * @author switch2018@outlook.com
	 * @param  obj
	 * @param  methodName
	 * @param  args
	 * @return result
	 * @throws CommonUtilException
	 */
	public final static Object invoke(Object obj, String methodName, Object... args) throws CommonUtilException {
		Method method = getMethod(obj.getClass(), methodName, ClassUtil.getClass(args));
		return invoke(obj, method, args);
	}

	public final static String toString(Object data) {
		if (data.getClass().isArray()) {
			return ArrayUtil.toString((Object[]) data);
		} else if (data instanceof List) {
			return ListUtil.toString((List<?>) data);
		} else if (data instanceof Map) {
			return MapUtil.toString((Map<?, ?>) data);
		} else if (data instanceof String) {
			return StringUtil.toString((String) data);
		} else {
			if (isNull(data)) {
				return null;
			}
			return data.toString();
		}
	}

	public final static int getLength(Object data) {
		if (data.getClass().isArray()) {
			return ArrayUtil.getLength((Object[]) data);
		} else if (data instanceof List) {
			return ListUtil.getLength((List<?>) data);
		} else if (data instanceof String) {
			return StringUtil.getLength((String) data);
		} else {
			return 0;
		}
	}

	/*
	 * Basics Method part
	 * 
	 * Need attention null
	 */
	
	/**
	 * 创建一个类的实例
	 * 
	 * @author switch2018@outlook.com
	 * @param  className
	 * @return
	 * @throws CommonUtilException
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T createBean(String className) throws CommonUtilException {
		try {
			return (T) createBean(Class.forName(className));
		} catch (Exception e) {
			throw new CommonUtilException(ErrorCode.bean_instance_error, e);
		}
	}

	/**
	 * 创建一个类的实例
	 * 
	 * @author switch2018@outlook.com
	 * @param  clazz
	 * @return
	 * @throws CommonUtilException
	 */
	public final static <T> T createBean(Class<T> clazz) throws CommonUtilException {
		if (isNull(clazz)) {
			throw new NullPointerException("class cannot be null");
		}

		try {
			return clazz.getConstructor().newInstance();
		} catch (Exception e) {
			throw new CommonUtilException(ErrorCode.bean_instance_error, e);
		}
	}

	/**
	 * 判断多个对象是否相等
	 * 
	 * @author switch2018@outlook.com
	 * @param  data
	 * @return true / false
	 * @throws CommonUtilException
	 */
	public final static boolean equal(Object... data) {
		for (int i = 1; i < getLength(data); i++) {
			if (data[0] == data[i]) {
				continue;
			}

			if (data[0] != null && !data[0].equals(data[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断一个对象是否为空
	 * 
	 * @author switch2018@outlook.com
	 * @param  data
	 * @return true / false
	 * @throws CommonUtilException
	 */
	public final static boolean isEmpty(Object data) {
		if (isNull(data)) {
			return true;
		} else if (data.getClass().isArray()) {
			int arrayLength = Array.getLength(data);
			Object[] obj = new Object[arrayLength];
			for (int i = 0; i < arrayLength; i++)
				obj[i] = Array.get(data, i);
			return ArrayUtil.isEmpty(obj);
		} else if (data instanceof List) {
			return ListUtil.isEmpty((List<?>) data);
		} else if (data instanceof Map) {
			return MapUtil.isEmpty((Map<?, ?>) data);
		} else if (data instanceof String) {
			return StringUtil.isEmpty((String) data);
		}
		return false;
	}

	/**
	 * 判断一个对象是否为空
	 * 
	 * @author switch2018@outlook.com
	 * @param  data
	 * @return boolean
	 * @throws CommonUtilException
	 */
	public final static boolean isNull(Object data) {
		return data == null;
	}

	/**
	 * 获取类的方法
	 * 
	 * @author switch2018@outlook.com
	 * @param  clazz
	 * @param  methodName
	 * @param  attributeType
	 * @return method
	 * @throws CommonUtilException
	 */
	public final static Method getMethod(Class<?> clazz, String methodName, Class<?>... attributeType) throws CommonUtilException {
		if (isNull(clazz) || StringUtil.isEmpty(methodName)) {
			throw new CommonUtilException(ErrorCode.bean_instance_error, StringUtil.toString("Class : ", clazz, " , Method name : ", methodName));
		}

		try {
			return clazz.getDeclaredMethod(methodName, attributeType);
		} catch (Exception e) {
			throw new CommonUtilException(ErrorCode.bean_instance_error, e);
		}
	}

	/**
	 * 执行method方法
	 * 
	 * @author switch2018@outlook.com
	 * @param obj
	 * @param method
	 * @param args
	 * @return result
	 * @throws CommonUtilException
	 */
	public final static Object invoke(Object obj, Method method, Object... args) throws CommonUtilException {
		if (isNull(obj) || isNull(method)) {
			throw new NullPointerException("when invoke method, obj/method cannot be null");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw new CommonUtilException(ErrorCode.bean_instance_error, e);
		}
	}

	/**
	 * if equal so null
	 * 
	 * @author switch2018@outlook.com
	 * @param bean
	 * @return result
	 * @throws CommonUtilException
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T simpleCopyBean(T... bean) throws CommonUtilException {
		if (isEmpty(bean)) {
			return null;
		}
		Class<?> clazz = bean[0].getClass();
		Object result = createBean(clazz);
		Field[] fields = FieldUtil.getDeclaredFields(clazz);
		for (int i = 0; i < ArrayUtil.getLength(fields); i++) {
			try {
				Object[] data = new Object[ArrayUtil.getLength(bean)];
				for (int j = 0; j < ArrayUtil.getLength(bean); j++) {
					data[i] = getValueByAttributeName(bean[i], FieldUtil.getName(fields[i]));
				}
				if (!equal(data)) {
					setValueByAttributeName(result, FieldUtil.getName(fields[i]), data[0]);
				}
			} catch (Exception e) {
				// 异常不做处理
			}
		}
		return (T) result;
	}

	public static <T> T toBean(Map<String, Object> map, Class<T> clazz) throws CommonUtilException {
		T bean = createBean(clazz);
		setValue(bean, map, clazz);
		return bean;
	}

	private final static void setValue(Object bean, Map<String, Object> map, Class<?> clazz) throws CommonUtilException {
		Field[] field = FieldUtil.getDeclaredFields(clazz);
		for (int i = 0; i < ArrayUtil.getLength(field); i++) {
			String fieldName = FieldUtil.getName(field[i]);
			Object fieldValue = map.get(fieldName);
			if (BeanUtil.isNull(fieldValue)) {
				continue;
			}
			setValueByAttributeName(bean, fieldName, fieldValue);
		}

		if (clazz.getSuperclass().equals(Object.class)) {
			return;
		}

		setValue(bean, map, clazz.getSuperclass());
	}
}