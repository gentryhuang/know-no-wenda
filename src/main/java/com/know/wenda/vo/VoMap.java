package com.know.wenda.vo;

import java.util.HashMap;
import java.util.Map;


/**
 * VoMap
 * 用于封装不同数据
 * @author hlb
 */
public class VoMap {

    /**
     * 定义一个Map集合
     */
    private Map<String, Object> map = new HashMap<String, Object>();

    /**
     * 设置值
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        map.put(key, value);
    }

    /**
     * 获取值
     * velocity在获取值的时候，直接通过key，就会调用这个方法，方法的参数就是key
     * @param key
     * @return
     */
    public Object get(String key) {
        return map.get(key);
    }
}
