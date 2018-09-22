package ggboy.java.common.utils.validator;

import java.lang.reflect.Field;

import ggboy.java.common.annotation.Verify;
import ggboy.java.common.enums.ErrorCode;
import ggboy.java.common.exception.CommonUtilException;
import ggboy.java.common.utils.string.StringUtil;

public class Validator {
	public final static void verify(Object data) throws CommonUtilException {
		Class<?> clazz = data.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			Verify verify = field.getAnnotation(Verify.class);
			if (verify == null) {
				continue;
			}
			try {
				Object value = clazz.getMethod("get" + StringUtil.toUpperCase(field.getName())).invoke(data);
				if (value == null) {
					if (verify.NotNull()) {
						throw new CommonUtilException(ErrorCode.parameter_validate_failed,
								field.getName() + " is null");
					}
				} else {
					switch (verify.Type()) {
					case MAIL:
						if (!(value instanceof String)) {
							throw new CommonUtilException(ErrorCode.parameter_validate_failed,
									field.getName() + " is not String");
						}
						if(!isMail(value.toString())){
							throw new CommonUtilException(ErrorCode.parameter_validate_failed,
									field.getName() + " is not mail");
						}
						break;
					case MOBILE:
						if (!(value instanceof String)) {
							throw new CommonUtilException(ErrorCode.parameter_validate_failed,
									field.getName() + " is not String");
						}
						if(!isMobile(value.toString())){
							throw new CommonUtilException(ErrorCode.parameter_validate_failed,
									field.getName() + " is not mail");
						}
						break;
					case STRING:
						if (!(value instanceof String)) {
							throw new CommonUtilException(ErrorCode.parameter_validate_failed,
									field.getName() + " is not String");
						}
						break;
					case NUMBER:
						break;
					default:
						break;
					}
					if (!maxLength(value.toString(), verify.maxLength())) {
						throw new CommonUtilException(ErrorCode.parameter_validate_failed,
								field.getName() + " is too long");
					}
					if (!minLength(value.toString(), verify.minLength())) {
						throw new CommonUtilException(ErrorCode.parameter_validate_failed,
								field.getName() + " is too short");
					}
				}
			} catch (Exception e) {
				if (e instanceof CommonUtilException) {
					throw (CommonUtilException) e;
				}
				throw new CommonUtilException(ErrorCode.system_error, e);
			}
		}
	}

	private static boolean isMail(String str) {
		// TODO
		return true;
	}

	private static boolean isMobile(String str) {
		// TODO
		return true;
	}

	private static boolean maxLength(String str, int max) {
		if (StringUtil.isEmpty(str))
			return true;
		return str.length() <= max;
	}

	private static boolean minLength(String str, int min) {
		if (StringUtil.isEmpty(str))
			return true;
		return str.length() >= min;
	}
}
