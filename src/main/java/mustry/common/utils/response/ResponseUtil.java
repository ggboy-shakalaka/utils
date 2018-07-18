package mustry.common.utils.response;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import mustry.common.enums.ErrorCode;
import mustry.common.exception.CommonUtilException;
import mustry.common.utils.bean.BeanUtil;

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
