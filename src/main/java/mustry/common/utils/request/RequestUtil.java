package mustry.common.utils.request;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import mustry.common.enums.ErrorCode;
import mustry.common.exception.CommonUtilException;
import mustry.common.utils.bean.BeanUtil;
import mustry.common.utils.io.IoUtil;

public class RequestUtil {
	public final static InputStream getInputStream(HttpServletRequest request) throws CommonUtilException {
		if (BeanUtil.isEmpty(request)) {
			return null;
		}
		try {
			return request.getInputStream();
		} catch (IOException e) {
			throw new CommonUtilException(ErrorCode.byte_error, e);
		}
	}
	
	
	public static byte[] toBytes(HttpServletRequest req) throws CommonUtilException {
		return IoUtil.in2Bytes(getInputStream(req));
	}
}
