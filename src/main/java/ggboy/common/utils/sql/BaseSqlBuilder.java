package ggboy.common.utils.sql;

import ggboy.common.exception.CommonUtilException;

public interface BaseSqlBuilder {
	String getSql() throws CommonUtilException;
}
