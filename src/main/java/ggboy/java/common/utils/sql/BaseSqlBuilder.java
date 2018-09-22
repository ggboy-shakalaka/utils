package ggboy.java.common.utils.sql;

import ggboy.java.common.exception.CommonUtilException;

public interface BaseSqlBuilder {
	String getSql() throws CommonUtilException;
}
