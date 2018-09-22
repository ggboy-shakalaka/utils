package ggboy.java.common.utils.sql.mysql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ggboy.java.common.constant.ExceptionMessageConstant;
import ggboy.java.common.constant.SqlConstant;
import ggboy.java.common.constant.SymbolConstant;
import ggboy.java.common.context.StringBuilderContext;
import ggboy.java.common.enums.ErrorCode;
import ggboy.java.common.exception.CommonUtilException;
import ggboy.java.common.utils.bean.BeanUtil;
import ggboy.java.common.utils.list.ListUtil;
import ggboy.java.common.utils.sql.BaseSqlBuilder;
import ggboy.java.common.utils.string.StringUtil;

public class Select implements BaseSqlBuilder {
	private List<String> property;
	private String table;
	private List<Map<String,String>> condition;
	private List<String> orderBy;
	private String limit;
	private String baseSql;
	
	private final static String operator_key = "operator";
	private final static String key_key = "key";
	private final static String symbol_key = "symbol";
	private final static String value_key = "value";
	
	public Select(String tableName) {
		this.table = tableName;
		this.property = new ArrayList<String>();
		this.condition = new ArrayList<Map<String, String>>();
	}
	
	public Select(String sql, boolean dosql) {
		this.baseSql = sql;
	}

	public void setProperty(List<String> propertys) {
		this.property.clear();
		this.property.addAll(propertys);
	}

	public void addProperty(String property) {
		this.property.add(property);
	}

	public void and(String key, String condition, String value) {
		addCondition(key,condition,value,SqlConstant.AND);
	}
	
	public void or(String key, String condition, String value) {
		addCondition(key,condition,value,SqlConstant.OR);
	}
	
	private void addCondition(String key, String condition, String value, String operator) {
		Map<String, String> map = new HashMap<String,String>();
		map.put(operator_key, operator);
		map.put(key_key, key);
		map.put(symbol_key, condition);
		map.put(value_key, value);
		this.condition.add(map);
	}

	public void setOrderBy(List<String> orderBy) {
		this.orderBy.clear();
		this.orderBy.addAll(orderBy);
	}

	public void addOrderBy(String orderBy) {
		this.orderBy.add(orderBy);
	}

	public void setLimit(int page, String pageSize) {
		this.limit = StringUtil.toStringAndClear(StringBuilderContext.getContext().append(page - 1).append(",").append(pageSize));
	}

	public void setLimit(int start, int end) {
		this.limit = StringUtil.toStringAndClear(StringBuilderContext.getContext().append(start - 1).append(",").append(end - start + 1));
	}

	public String getSql() throws CommonUtilException {
		
		if (!StringUtil.isEmpty(this.baseSql)) {
			return this.baseSql;
		}
		
		if (StringUtil.isEmpty(this.table)) {
			CommonUtilException e = new CommonUtilException(ErrorCode.build_sql_error);
			e.setMemo(ExceptionMessageConstant.sql_table_must_not_empty);
			throw e;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("select ").append(buildProperty(this.property)).append(" from ").append(this.table);

		String conditionStr = buildCondition(this.condition);
		if (!BeanUtil.isEmpty(conditionStr)) {
			sb.append(" where ").append(conditionStr);
		}
		
		String orderByStr = buildOrderBy(this.orderBy);
		if (!BeanUtil.isEmpty(orderByStr)) {
			sb.append(" order by ").append(orderByStr);
		}

		if (!StringUtil.isEmpty(this.limit)) {
			sb.append(" limit ").append(this.limit);
		}
		return sb.toString();
	}
	
	private static String buildProperty(List<String> property) throws CommonUtilException {
		if (ListUtil.isEmpty(property)) {
			return "*";
		}
		return ListUtil.toSplitString(property, SymbolConstant.COMMA);
	}
	
	private static String buildCondition(List<Map<String,String>> condition) {
		if (BeanUtil.isEmpty(condition)) {
			return null;
		}
		StringBuilder sb = StringBuilderContext.getContext();
		String flag = "";
		for (int i = 0; i < BeanUtil.getLength(condition); i++) {
			String operator = condition.get(i).get(operator_key);
			String key = condition.get(i).get(key_key);
			String symbol = condition.get(i).get(symbol_key);
			String value = condition.get(i).get(value_key);
			
			if (BeanUtil.isEmpty(operator) || BeanUtil.isEmpty(key) || BeanUtil.isEmpty(symbol)) {
				continue;
			}
			
			if (sb.length() == 0) {
				flag = operator;
			}
			
			if(!BeanUtil.equal(flag,operator)) {
				sb.insert(0, "(").insert(sb.length(), ")");
				flag = operator;
			}
			
			if (sb.length() != 0) {
				sb.append(" ").append(operator).append(" ");
			}
			
			sb.append(key).append(" ").append(symbol).append(" '").append(value).append("'");
		}
		return StringUtil.toStringAndClear(sb);
	}
	
	private static String buildOrderBy(List<String> orderBy) throws CommonUtilException {
		if(BeanUtil.isEmpty(orderBy)) {
			return null;
		}
		return ListUtil.toSplitString(orderBy, SymbolConstant.COMMA);
	}

	public void setSql(String sql) {
		this.baseSql = sql;
	}
}
