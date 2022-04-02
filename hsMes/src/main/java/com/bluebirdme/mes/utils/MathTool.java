package com.bluebirdme.mes.utils;


/**
 * @author Goofy
 * @Date 2017年8月14日 下午4:11:15
 */
public class MathTool {

	/**
	 * 返回多个数字的最小公倍数
	 * 
	 * @param intNumber
	 * @return
	 */
	public static int LCM(int[] intNumber) {
		int result = 0;
		for (int i = 1; i < Integer.MAX_VALUE; i++) {
			int num = intNumber.length;
			while (num > 0) {
				int count = 0;
				for (int array : intNumber) {
					if (i % array != 0) {
						break;
					} else {
						count++;
					}
				}
				if (count == intNumber.length) {
					result = i;
					break;
				}
				num--;
			}
			if (result > 0) {
				break;
			}
		}
		return result;
	}
}
