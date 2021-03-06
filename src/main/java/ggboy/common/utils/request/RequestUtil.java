package ggboy.common.utils.request;

import ggboy.common.enums.ErrorCode;
import ggboy.common.exception.CommonUtilException;
import ggboy.common.utils.io.IoUtil;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import ggboy.common.utils.bean.BeanUtil;

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
