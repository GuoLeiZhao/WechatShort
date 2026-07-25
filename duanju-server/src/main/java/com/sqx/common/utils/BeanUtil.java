package com.sqx.common.utils;

import com.alibaba.fastjson.JSON;
import com.sqx.modules.douyin.request.CreateOrderRequest;

import java.lang.reflect.Field;
import java.util.*;

public class BeanUtil {
    public BeanUtil() {
    }

    public static <T> T convert(Object source, Class<T> target) {
        return JSON.parseObject(JSON.toJSONString(source), target);
    }

    public static <T> List<T> convertList(Object source, Class<T> target) {
        return JSON.parseArray(JSON.toJSONString(source), target);
    }

    public static Map<String, Object> toMap(Object o) {
        return cn.hutool.core.bean.BeanUtil.beanToMap(o);
    }

    public static <T> Map<String, Object> convertClassToNonEmptyMap(T obj) {
        Class<?> clazz = obj.getClass();
        Map<String, Object> resultMap = new HashMap<>();

        // 获取所有字段（包括继承的字段）
        Field[] fields = getAllFields(clazz);

        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            // 设置为可访问（如果尚未如此）
            makeAccessible(field);

            // 获取字段值
            Object fieldValue;
            try {
                fieldValue = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field value", e);
            }

            // 过滤掉空值
            if (fieldValue != null) {
                resultMap.put(field.getName(), fieldValue);
            }
        }

        return resultMap;
    }

    private static Field[] getAllFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();
        while (clazz != null) {
            Collections.addAll(allFields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return allFields.toArray(new Field[0]);
    }

    private static void makeAccessible(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }
}