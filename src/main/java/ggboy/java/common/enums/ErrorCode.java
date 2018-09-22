package ggboy.java.common.enums;

import ggboy.java.common.utils.string.StringUtil;

public enum ErrorCode {
	
	byte_error("byte_error","byte类型解析异常"),
	string_error("string_error","String类型解析异常"),
	rsa_encrypt_error("rsa_encrypt_error","RSA加密异常"),
	rsa_decrypt_error("rsa_decrypt_error","RSA解密异常"),
	rsa_sign_error("rsa_sign_error","RSA加签异常"),
	rsa_verify_error("rsa_verify_error","RSA验签异常"),
	serialize_error("serialize_error","序列化异常"),
	reverse_serialize_error("reverse_serialize_error","反序列化异常"),
	http_send_request_error("http_send_request_error","发送Http请求异常"),
	bean_empty_error("bean_empty_error","bean为空异常"),
	bean_not_empty_error("bean_not_empty_error","bean不为空异常"),
	bean_null_error("bean_null_error","bean为空异常"),
	bean_not_null_error("bean_not_null_error","bean不为空异常"),
	bean_instance_error("bean_instance_error","bean实例化异常"),
	parameter_validate_failed("parameter_validate_failed", "参数校验失败"),
	build_sql_error("build_sql_error", "构建sql异常"),
	system_error("system_error","系统异常"),
	;
	
	private String code;
	private String message;
	
	private ErrorCode(String code,String message) {
		this.code = code;
		this.message = message;
	}
	
	public static ErrorCode getByCode(String code) {
		if(StringUtil.isEmpty(code)) return null;
		for(ErrorCode item : ErrorCode.values()) {
			if(item.getCode().equals(code)) {
				return item;
			}
		}
		return null;
	}
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}