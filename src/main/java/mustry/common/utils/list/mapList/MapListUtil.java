package mustry.common.utils.list.mapList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mustry.common.exception.CommonUtilException;
import mustry.common.utils.array.ArrayUtil;
import mustry.common.utils.assertt.Assert;
import mustry.common.utils.bean.BeanUtil;
import mustry.common.utils.list.ListUtil;

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
