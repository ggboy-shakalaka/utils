package ggboy.java.common.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;

import ggboy.java.common.constant.DateConstant;
import ggboy.java.common.utils.bean.BeanUtil;
import ggboy.java.common.utils.string.StringUtil;

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
