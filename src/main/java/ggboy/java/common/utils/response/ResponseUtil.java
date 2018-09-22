package ggboy.java.common.utils.response;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import ggboy.java.common.enums.ErrorCode;
import ggboy.java.common.exception.CommonUtilException;
import ggboy.java.common.utils.bean.BeanUtil;

public class ResponseUtil {
	public static String toString(HttpResponse data) throws CommonUtilException {
		if (BeanUtil.isEmpty(data)) {
			return null;
		}
		try {
			return EntityUtils.toString(data.getEntity());
		} catch (Exception e) {
			throw new CommonUtilException(ErrorCode.string_error, e);
		}
	}
}
