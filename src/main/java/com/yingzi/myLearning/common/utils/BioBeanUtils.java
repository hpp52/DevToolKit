package com.yingzi.myLearning.common.utils;

import org.springframework.beans.BeanUtils;

/**
 * TODO
 *
 * @author hk
 * @version V1.0
 * @DATE 2018/12/29 8:50
 */
public class BioBeanUtils {


    /**
     * bean的转换
     * @param source 被转换的对象
     * @param target 转换的对象
     * @author hk
     * @date 2018/12/29
     * @return
     */
    public static <V> V convertBean(Object source,Class<V> target){
        if(source==null){
            return null;
        }
        V v= null;
        try {
            v = target.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(source,v);
        return v;
    }
}
