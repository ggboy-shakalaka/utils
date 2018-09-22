package mustry.common.utils.binary;

import mustry.common.context.StringBuilderContext;

public class BinaryUtil {
	/*
	 * 暂时还不能转换负数，具体算法等我研究一下
	 */
	public static String toString(int data) {
		StringBuilder sb = StringBuilderContext.getContext();
		int maxBit = getBit(data);
		if (data < 0)
			data = -data;
		for (int i = 0; i < maxBit; i++) {
			if (i % 8 == 0 && i != 0) {
				sb.insert(0, "-");
			}
			if (data % 2 == 0) {
				sb.insert(0, 0);
			} else {
				sb.insert(0, 1);
			}
			data >>>= 1;
		}
		return sb.toString();
	}

	private static int getBit(Object data) {
		if (data instanceof Integer) {
			return 32;
		} else {
			return 8;
		}
	}
}