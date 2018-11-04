package ggboy.common.utils.clazz;

public class ClassUtil {

	/*
	 * Basics Method part
	 * 
	 * Need attention null
	 */
	
	public final static Class<?>[] getClass(Object[] obj) {
		if (obj == null) {
			throw new NullPointerException("obj cannot be null");
		}

		Class<?>[] clazzes = new Class<?>[obj.length];
		for (int i = 0; i < obj.length; i++) {
			clazzes[i] = obj[i].getClass();
		}

		return clazzes;
	}
}
