package java.ggboy.common.utils.response;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.ggboy.common.enums.ErrorCode;
import java.ggboy.common.exception.CommonUtilException;
import java.ggboy.common.utils.bean.BeanUtil;

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
