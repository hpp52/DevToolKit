package com.dfkj.myLearning.service.Chapter_1_Redis.redis;

import com.dfkj.center.bio.entity.vo.VetCommonsVo;
import org.springframework.cache.interceptor.KeyGenerator;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 自动生成key，主要用于传参为实体的对象
 */
public class RedisKeyGenerator implements KeyGenerator {

	private final static String INVALID_KEY="serialVersionUID";
	
	/**
	 * 采用的策略为实体有值时的自动拼接
	 */
	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuilder key = new StringBuilder();
		for (Object obj : params) {
			if (obj instanceof VetCommonsVo) {
				getKeyName(key,obj,false);
			}
		}
		return key.toString();
	}

	
	
	public static void getKeyName(StringBuilder key,Object obj,boolean isParent) {
			Class<? extends Object> clazz = obj.getClass();
			Field[] fields ;
			if(isParent) {
				fields=((Class)obj).getDeclaredFields();
			}else {
				fields = clazz.getDeclaredFields();
			}
			

			try {
				// 读数据
				for (Field f : fields) {
					if(f.getName().equalsIgnoreCase("isSerialVersionUID")) {
						continue;
					}
					
					PropertyDescriptor pd = new PropertyDescriptor(f.getName(), clazz);
					Method rM = pd.getReadMethod();// 获得读方法
					if(rM==null) {
						continue;
					}
					Object temp =  rM.invoke(obj);
					if(temp!=null) {
						key.append(f.getName());
						key.append(":");
						key.append(temp.toString());
					}
				}
				Class<?> superclass = clazz.getSuperclass();
				Field[] superclassFields = superclass.getDeclaredFields();
				for (Field f : superclassFields) {
					if(f.getName().equalsIgnoreCase(INVALID_KEY)) {
						continue;
					}
					
					PropertyDescriptor pd = new PropertyDescriptor(f.getName(), clazz);
					Method rM = pd.getReadMethod();// 获得读方法
					if(rM==null) {
						continue;
					}
					Object temp =  rM.invoke(obj);
					if(temp!=null) {
						key.append(f.getName());
						key.append(":");
						key.append(temp.toString());
					}
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
