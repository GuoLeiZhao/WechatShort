package com.sqx.common.utils;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;
    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;
    private final static Gson Gson = new Gson();

    /**
     * 设置
     * @param key
     * @param value
     * @param expire 过期时间，单位 秒
     */
    public void set(String key, Object value, long expire){
        valueOperations.set(key, toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void deleteAll(String prefix){
        ScanOptions scanOptions = ScanOptions.scanOptions().match(prefix+"*").build();
        try (Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection((RedisConnection connection) ->
                connection.scan(scanOptions))){
            if (cursor == null) {
                return;
            }
            while (cursor.hasNext()) {
                redisTemplate.delete(new String(cursor.next()));
            }
        }
    }

    /**
     * 设置位图
     * @param key
     * @param offset
     * @param value
     */
    public void setBitMap(String key, long offset, boolean value){
        redisTemplate.opsForValue().setBit(key, offset, value);
    }

    /**
     * 设置位图
     * @param key
     * @param offset
     */
    public Boolean getBitMap(String key, long offset){
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 位图操作
     * @param key
     * @param bitFieldSubCommands
     */
    public List<Long> bitField(String key, BitFieldSubCommands bitFieldSubCommands){
        return redisTemplate.opsForValue().bitField(key, bitFieldSubCommands);
    }


    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return Gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return Gson.fromJson(json, clazz);
    }

    /**
     * lockKey 锁 key
     * value 身份标识（保证锁不会被其他人释放）
     * expireTime 锁的有效时间
     **/
    public Boolean lock(String key, String value, long expire) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value, expire, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("redis连接异常");
        }
    }

    /**
     * key 锁的key
     * value 身份标识
     * return 成功返回true 失败返回false
     **/
    public Boolean unlock(String key, String value) {
        // 获取当前锁的拥有者
        Object currentValue = redisTemplate.opsForValue().get(key);
        Boolean result = false;
        // 判断value是否为锁的拥有者
        if (StrUtil.isNotEmpty(String.valueOf(currentValue)) && Objects.equals(currentValue, value)) {
            //删除锁
            result = redisTemplate.opsForValue().getOperations().delete(key);
        }
        return result;
    }
}
