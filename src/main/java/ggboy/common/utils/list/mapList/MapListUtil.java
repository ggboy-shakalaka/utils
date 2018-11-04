package ggboy.common.utils.list.mapList;

import ggboy.common.exception.CommonUtilException;
import ggboy.common.utils.array.ArrayUtil;
import ggboy.common.utils.assertt.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ggboy.common.utils.bean.BeanUtil;
import ggboy.common.utils.list.ListUtil;

public class MapListUtil {
	public final static List<Map<String, Object>> parse(List<?> data, String[] attribute) throws CommonUtilException {
		return parse(data, attribute, attribute);
	}

	public final static String[] getAttributeNames(Object data) {
		return null;
	}

	/*
	 * Basics Method part
	 * 
	 * Need attention null
	 */
	
	public final static List<Map<String, Object>> parse(List<?> data, String[] attribute, String[] name) throws CommonUtilException {
		Assert.sameLength(attribute, name);
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>(ListUtil.getLength(data));
		for (int i = 0; i < ListUtil.getLength(data); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < ArrayUtil.getLength(attribute); j++) {
				map.put(name[j], BeanUtil.getValueByAttributeName(data.get(i), attribute[j]));
			}
			mapList.add(map);
		}
		return mapList;
	}
}
