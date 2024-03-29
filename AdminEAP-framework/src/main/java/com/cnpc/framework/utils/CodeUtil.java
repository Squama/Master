package com.cnpc.framework.utils;

import java.text.DecimalFormat;

public class CodeUtil {


	/**
	 * 根据当前编号和前缀生成下一个编号
	 *
	 * @param prefix
	 *            生成编号前缀
	 * @param maxCode
	 *            最大编号
	 * @param length
	 *            自动累加部分长度
	 * @return String 返回计算后的编号
	 *
	 * @Author: 江日念
	 * @Date: 2016年4月14日 上午11:27:12
	 */
	public synchronized static String nextCode(String prefix, String maxCode, int length) {

		String code;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append("0");
		}
		DecimalFormat df = new DecimalFormat(sb.toString());
		if (StrUtil.isEmpty(maxCode)) {
			code = prefix + df.format(1);
		} else {
			String num = maxCode.substring(prefix.length());
			code = prefix + df.format(1 + Integer.valueOf(num));
		}
		return code;
	}

}
