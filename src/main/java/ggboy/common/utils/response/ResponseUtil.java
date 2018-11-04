package ggboy.common.utils.response;

import ggboy.common.enums.ErrorCode;
import ggboy.common.exception.CommonUtilException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import ggboy.common.utils.bean.BeanUtil;

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
