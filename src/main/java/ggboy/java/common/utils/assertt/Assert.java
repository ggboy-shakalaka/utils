package ggboy.java.common.utils.assertt;

import ggboy.java.common.enums.ErrorCode;
import ggboy.java.common.exception.DeepException;
import ggboy.java.common.utils.array.ArrayUtil;
import ggboy.java.common.utils.bean.BeanUtil;

public class Assert {
	
	public final static void isEmpty(Object data) {
		if (!BeanUtil.isEmpty(data))
			throw new DeepException(ErrorCode.bean_not_null_error);
	}

	public final static void isNotEmpty(Object data) {
		if (BeanUtil.isEmpty(data))
			throw new DeepException(ErrorCode.bean_null_error);
	}

	public final static void isNull(Object data) {
		if (!BeanUtil.isNull(data))
			throw new DeepException(ErrorCode.bean_not_null_error);
	}
	
	public final static void isNotNull(Object data) {
		if (BeanUtil.isNull(data))
			throw new DeepException(ErrorCode.bean_not_null_error);
	}
	
	public final static void sameLength(Object[] data1, Object[] data2) {
		if (!ArrayUtil.isSameLength(data1, data2))
			throw new DeepException(ErrorCode.bean_not_null_error);
	}
}