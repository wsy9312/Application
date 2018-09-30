package com.sd.storage.dlib.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

public class JsonUtil {

	/**
	 * 解析json集合
	 * 
	 * @param jsonStr
	 * @param type       		
	 * <code>Type type = new TypeToken<List<T>>() {}.getType();</code>
	 * @return
	 */
	public static <T> List<T> json2List(String jsonStr, Type type)
			throws com.google.gson.JsonSyntaxException {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, type);
	}

	/**
	 * json 获取对象
	 * @param jsonStr
	 * @param bean
	 * @return
	 * @throws com.google.gson.JsonSyntaxException
	 */
	public static <T> T json2Bean(String jsonStr, Class<T> bean)
			throws com.google.gson.JsonSyntaxException {
		Gson gson = new Gson();
		T t = gson.fromJson(jsonStr, bean);
		return t;
	}
	
	/**
	 * 对象获取JsonStr
	 * @param obj
	 * @return
	 * @throws com.google.gson.JsonSyntaxException
	 */
	public static String object2Json(Object obj)
			throws com.google.gson.JsonSyntaxException {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
	
	
}
