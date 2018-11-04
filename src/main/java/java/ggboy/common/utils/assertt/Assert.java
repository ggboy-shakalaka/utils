package java.ggboy.common.utils.assertt;

import java.ggboy.common.enums.ErrorCode;
import java.ggboy.common.exception.DeepException;
import java.ggboy.common.utils.array.ArrayUtil;
import java.ggboy.common.utils.bean.BeanUtil;

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