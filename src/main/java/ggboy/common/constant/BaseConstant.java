package mustry.common.constant;

import java.nio.charset.Charset;

public class BaseConstant {
	public static final long version = 1L;
	public static final String default_charset = "utf-8";
	public static final Charset charset_utf8 = Charset.forName("UTF-8");
	public static final String default_delimiter = ",";
	public static final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

	private BaseConstant() {
	}
}
