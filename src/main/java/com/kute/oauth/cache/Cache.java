package com.kute.oauth.cache;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * Ä£Äâ»º´æ
 * Created by longbai on 2017/7/12.
 */
public class Cache {

    private static Map<String, Object> redis = Maps.newHashMap();

    public static Object get(String key) {
        if(!Strings.isNullOrEmpty(key) && redis.containsKey(key)) {
            redis.get(key);
        }
        return null;
    }

    public static void set(String key, Object value) {
        redis.put(key, value);
    }

}
