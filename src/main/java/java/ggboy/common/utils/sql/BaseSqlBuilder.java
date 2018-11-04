package java.ggboy.common.utils.sql;

import java.ggboy.common.exception.CommonUtilException;

public interface BaseSqlBuilder {
	String getSql() throws CommonUtilException;
}
