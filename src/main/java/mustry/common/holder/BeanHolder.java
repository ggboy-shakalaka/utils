package mustry.common.holder;

import java.util.HashMap;
import java.util.Map;

public class BeanHolder {

	private final static Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();

	@SuppressWarnings({ "unchecked" })
	public final static <T> T getBean(Class<T> clazz) {
		Object bean = null;
		if ((bean = get(clazz)) != null)
			return (T) bean;
		try {
			synchronized (clazz) {
				if ((bean = get(clazz)) != null)
					return (T) bean;
				bean = clazz.getConstructor().newInstance();
				put(clazz, bean);
				return (T) bean;
			}
		} catch (Exception e) {
			return null;
		}
	}

	private static Object get(Class<?> clazz) {
		return beanMap.get(clazz);
	}

	private static Object put(Class<?> clazz, Object bean) {
		return beanMap.put(clazz, bean);
	}
}