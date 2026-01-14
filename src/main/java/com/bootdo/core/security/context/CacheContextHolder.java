package com.bootdo.core.security.context;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登录用户缓存服务
 *
 * @author caiyz
 * @since 2026-01-08 15:12
 */
@Getter
@Component
public class CacheContextHolder {

    public static final String SESSION_KEY_IP = "%s_ip";
    public static final String SESSION_KEY_LOGIN_DATE = "%s_login_date";

    private final static TimedCache<String, Object> TIMED_CACHE = CacheUtil.newTimedCache(TimeUnit.MINUTES.toMillis(30));

    public static CacheContextHolder me() {
        return SpringUtil.getBean(CacheContextHolder.class);
    }

    /**
     * 存放 key value
     */
    public void put(String key, Object value) {
        TIMED_CACHE.put(key, value);
    }

    /**
     * 存放 prefix key value
     */
    public void put(String prefix, String key, Object value) {
        TIMED_CACHE.put(cacheKey(prefix, key), value);
    }

    /**
     * 存放 key value，并将 key 的生存时间设为 timeout (以毫秒为单位)。
     */
    public void putEx(String key, Object value, long timeout) {
        TIMED_CACHE.put(key, value, timeout);
    }

    /**
     * 返回 key 所关联的 value 值
     */
    public <T> T get(String key) {
        return (T) TIMED_CACHE.get(key);
    }

    /**
     * 返回 key 所关联的 value 值
     */
    public <T> T get(String prefix, String key) {
        return (T) TIMED_CACHE.get(cacheKey(prefix, key));
    }

    /**
     * 删除给定的一个 key
     */
    public void remove(String key) {
        TIMED_CACHE.remove(key);
    }

    /**
     * 删除给定的一个 key
     */
    public void remove(String prefix, String key) {
        TIMED_CACHE.remove(cacheKey(prefix, key));
    }

    /**
     * 刷新缓存
     */
    public void refresh() {
        TIMED_CACHE.schedulePrune(TimeUnit.MINUTES.toMillis(30));
    }

    /**
     * 缓存 key
     */
    public String cacheKey(String prefix, String key) {
        return prefix.formatted(key);
    }
}