package mustry.common.utils.sql.mysql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mustry.common.context.StringBuilderContext;
import mustry.common.enums.ErrorCode;
import mustry.common.exception.CommonUtilException;
import mustry.common.utils.bean.BeanUtil;
import mustry.common.utils.sql.BaseSqlBuilder;
import mustry.common.utils.string.StringUtil;

public class Insert implements BaseSqlBuilder {
	private String table;
	private List<String> column;
	private List<Map<String, String>> params;
	private String baseSql;

	// insert into table () vlaue ();

	public Insert(String table) {
		this.table = table;
		column = new ArrayList<String>();
		params = new ArrayList<Map<String, String>>();
	}
	
	public Insert(String sql, boolean dosql) {
		this.baseSql = sql;
	}

	public void addColumn(String column) {
		this.column.add(column);
	}

	public void addColumn(List<String> column) {
		this.column.addAll(column);
	}

	public void setColumn(String column) {
		this.column = StringUtil.toList(column);
	}

	public void setColumn(List<String> column) {
		this.column = column;
	}

	public void addParam(String column, String value) {
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put(column, value);
		this.params.add(valueMap);
	}

	@Override
	public String getSql() throws CommonUtilException {
		if (!BeanUtil.isEmpty(this.baseSql)) {
			return this.baseSql;
		}
		if (StringUtil.isEmpty(this.table)) {
			throw new CommonUtilException(ErrorCode.system_error);
		}
		String columnStr = buildColumn(column);
		String paramStr = buildParam(params, column);
		StringBuilder sb = StringBuilderContext.getContext();
		sb.append("insert into ");
		sb.append(this.table);
		sb.append(" ");
		sb.append(columnStr);
		sb.append(" values ");
		sb.append(paramStr);
		return StringUtil.toStringAndClear(sb);
	}

	private static String buildColumn(List<String> column) throws CommonUtilException {
		if (column == null || column.size() == 0) {
			throw new CommonUtilException(ErrorCode.system_error);
		}
		StringUtil.toString(column);
		return StringUtil.toString("(", StringUtil.toString(column), ")");
	}

	// TODO
	private static String buildParam(List<Map<String, String>> params, List<String> column) throws CommonUtilException {
		if (params == null || params.size() == 0) {
			throw new CommonUtilException(ErrorCode.system_error);
		}
//		StringBuilder sb = new StringBuilder();
//		for (Map<String, String> param : params) {
//			if (sb.length() != 0) {
//				sb.append(",");
//			}
//			sb.append("(");
//			for (String key : column) {
//
//			}
//		}
		return "";
		// return StringUtil.toString(column);
	}
}
