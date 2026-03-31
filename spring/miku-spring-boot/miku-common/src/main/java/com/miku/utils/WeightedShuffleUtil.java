package com.miku.utils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 加权随机打乱工具类
 */
public class WeightedShuffleUtil {
    
    // 缓存Method对象(缓存优化提升性能),避免重复反射
    private static final Map<Class<?>, Method[]> METHOD_CACHE = new HashMap<>();
    
    /**
     * 加权随机打乱
     */
    public static <T> void shuffle(List<T> list) {
        if (list == null || list.size() <= 1) return;
        
        // 预计算分数并排序（避免重复计算）
        List<T> sorted = new ArrayList<>(list);
        sorted.sort((a, b) -> Double.compare(score(b), score(a)));
        
        // 替换原列表
        for (int i = 0; i < list.size(); i++) {
            list.set(i, sorted.get(i));
        }
    }
    
    /**
     * 计算元素分数
     */
    private static <T> double score(T obj) {
        try {
            // 获取缓存的Method对象
            Method[] methods = METHOD_CACHE.computeIfAbsent(obj.getClass(), k -> {
                try {
                    return new Method[]{
                        k.getMethod("getViews"),
                        k.getMethod("getLikesCount"), 
                        k.getMethod("getCreatedTime")
                    };
                } catch (Exception e) {
                    return null;
                }
            });
            
            if (methods == null) return Math.random();
            
            // 计算分数
            Integer views = (Integer) methods[0].invoke(obj);
            Integer likes = (Integer) methods[1].invoke(obj);
            LocalDateTime time = (LocalDateTime) methods[2].invoke(obj);
            
            double hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
            return (Math.log1p(views) * 0.3 + Math.log1p(likes) * 0.5 + Math.exp(-hours / 24) * 0.2) 
                   * (0.7 + Math.random() * 0.6);
        } catch (Exception e) {
            return Math.random();
        }
    }
}