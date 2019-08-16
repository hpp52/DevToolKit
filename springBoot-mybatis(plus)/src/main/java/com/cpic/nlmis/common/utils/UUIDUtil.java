package com.cpic.nlmis.common.utils;

import java.util.UUID;


/**
 * @Author: hk
 * @Description: UUID生成器
 * UUID全球唯一，性能好，缺点是太长了，无序，存储空间大，索引性能不好
 * 是长度为8-4-4-4-12的36个字符 于此处理掉“-”号
 * @Date: 2019/8/8 16:56
 */
public class UUIDUtil {

	public UUIDUtil() {
	}

	/**
	 * 获得一个UUID,并去掉"-"符号
	 * @return UUID
	 */
	public static String generatorUUID() {
		String uuid = UUID.randomUUID().toString();
		// 去掉“-”符号
		return uuid.replaceAll(SymbolUtil.DASH, "");
	}

}