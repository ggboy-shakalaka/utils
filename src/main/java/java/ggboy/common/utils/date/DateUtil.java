package java.ggboy.common.utils.date;

import java.ggboy.common.constant.DateConstant;
import java.ggboy.common.utils.bean.BeanUtil;
import java.ggboy.common.utils.string.StringUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public final static Date toDate(String dateStr, String format) {
		return null;
	}

	public final static String toString(Date date) {
		return toString(date, DateConstant.default_date_format);
	}

	public final static String toMysqlString(Date date) {
		return StringUtil.toString("STR_TO_DATE('", toString(date, DateConstant.default_date_format), "','%Y-%m-%d %H:%i:%s')");
	}

	/*
	 * Basics Method part
	 * 
	 * Need attention null
	 */

	public final static String toString(Date date, String format) {
		if (BeanUtil.isNull(date) || StringUtil.isEmpty(format)) {
			throw new IllegalArgumentException("date : " + date + ",format : " + format);
		}
		return new SimpleDateFormat(format).format(date);
	}
}
