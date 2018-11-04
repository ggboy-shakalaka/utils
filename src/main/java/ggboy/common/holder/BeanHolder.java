package ggboy.common.holder;

import ggboy.common.exception.CommonUtilException;

import java.util.HashMap;
import java.util.Map;

public class BeanHolder {

	private final static Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();

	public final static <T> T getBean(Class<T> clazz) throws CommonUtilException {
		if (clazz == null)
			return null;

		T bean = null;
		if ((bean = get(clazz)) == null) {
			try {
				synchronized (clazz) {
					if ((bean = get(clazz)) == null)
						bean = put(clazz, clazz.getConstructor().newInstance());
				}
			} catch (Exception e) {
				throw new CommonUtilException("");
			}
		}
		
		return bean;
	}

	@SuppressWarnings("unchecked")
	private static <T> T get(Class<T> clazz) {
		return (T) beanMap.get(clazz);
	}

	@SuppressWarnings("unchecked")
	private static <T> T put(Class<T> clazz, Object bean) {
		return (T) beanMap.put(clazz, bean);
	}
}