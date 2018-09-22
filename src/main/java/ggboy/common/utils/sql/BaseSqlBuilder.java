package mustry.common.utils.sql;

import mustry.common.exception.CommonUtilException;

public interface BaseSqlBuilder {
	String getSql() throws CommonUtilException;
}
