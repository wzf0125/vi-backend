package com.quanta.vi.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description redis工具类
 * @author gdufs
 * @date 2021/11/25
 * */
@Slf4j
@Component // 实现bean的注入
@SuppressWarnings("ConstantConditions")
public class RedisUtils {


    @Autowired
    @Qualifier("redisTemplate") //自定义缓存配置类
    private RedisTemplate<String,Object> redisTemplate;

    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }


    // 获取缓存值
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    // 设置缓存值
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    // 设置缓存值+时间
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    //删除缓存
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 获取剩余时间
     * -1为未设置
     * -2为键不存在
     * */
    public Long getExpire(String key){
        return redisTemplate.opsForValue().getOperations().getExpire(key);
    }

    /**
     * 设置过期时间
     * */
    public boolean  setExpire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获取数值类型 若键不存在则返回0
     * */
    public int getIntOrZero(String key) {
        return redisTemplate.opsForValue().get(key) == null ? 0 : (int) redisTemplate.opsForValue().get(key);
    }

    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("Incr factor must greater than zero.");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("Decr factor must greater than zero.");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    // HASHMAP
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                setExpire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                setExpire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    public double hincr(String key, String item, double delta) {
        return redisTemplate.opsForHash().increment(key, item, delta);
    }

    public double hdecr(String key, String item, double delta) {
        return redisTemplate.opsForHash().increment(key, item, -delta);
    }

}
