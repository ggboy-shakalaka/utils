package java.ggboy.common.exception;

import java.ggboy.common.enums.ErrorCode;
import java.ggboy.common.utils.string.StringUtil;

public class CommonUtilException extends Exception {
	private static final long serialVersionUID = 1L;
	private String code;
	private String memo;

	public CommonUtilException(ErrorCode ec, String memo) {
		this(ec, null, memo);
	}

	public CommonUtilException(ErrorCode ec, String message, String memo) {
		super(StringUtil.isEmpty(message) ? ec.getMessage() : message);
		this.code = ec.getCode();
		this.memo = memo;
	}

	public CommonUtilException(String message) {
		super(message);
	}

	public CommonUtilException(ErrorCode ec) {
		super(ec.getMessage());
		this.code = ec.getCode();
	}

	public CommonUtilException(ErrorCode ec, Throwable cause) {
		this(ec, null, cause);
	}

	public CommonUtilException(ErrorCode ec, String memo, Throwable cause) {
		super(StringUtil.isEmpty(cause.getMessage()) ? ec.getMessage() : cause.getMessage());
		this.code = ec.getCode();
		this.memo = memo;
	}

	public String getCode() {
		return this.code;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}