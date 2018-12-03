package cn.woshicheng.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis通用处理类
 */
@Component
public class RedisService {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    /**
     * 构造方法
     */
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate getRedisTemplate(){
        return redisTemplate;
    }

    /**
     * 构造方法
     */
    public RedisService() {
        super();
    }

    /**
     * 存储string类型并且需要设置失效时间
     */
    public void expire(String key, String value, long expireTime) {
        this.setString(key, value);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 设置失效时间
     */
    public void expire(String key, long expireTime) {
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 存储string类型
     */
    public void setString(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            logger.error("redis set放大异常,异常原因{}", e);
        }
    }

    /**
     * 获取结果(String 类型)
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Long getIncrement(String key) {
        return redisTemplate.boundValueOps(key).increment(0);
    }

    /**
     * 自减1
     * @param key
     * @return
     */
    public Long decrement(String key){
        return redisTemplate.boundValueOps(key).increment(-1L);
    }

    /**
     * 取set结合
     */
    public Set<String> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 添加set集合
     */
    public void addForSet(String key, String... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    /**
     *  pop for set
     * */
    public String popForSet(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * set集合中个数
     */
    public Long sizeForSet(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * del
     */
    public void del(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 添加 哈希表元素
     */
    public void addForHash(String key, Map<String, String> stringStringMap) {
        redisTemplate.opsForHash().putAll(key, stringStringMap);
    }

    /**
     * 根据redis的key和map的key获取值
     * @param key
     * @param hashKey
     * @return
     */
    public Object get(String key,String hashKey){
        return redisTemplate.opsForHash().get(key,hashKey);
    }

    /**
     * 删除 哈希表元素
     */
    public void delForHash(String key, String ... objects){
        redisTemplate.opsForHash().delete(key, objects);
    }

    /**
     * 自增长
     */
    public Long increment(String key) {
        return redisTemplate.boundValueOps(key).increment(1);
    }



    /**
     * 自增长 设置失效时间
     */
    public Long increment(String key, long expireTime) {
        Long redisValue = this.increment(key);
        if (redisValue == 1) {
            this.expire(key, expireTime);
        }
        return redisValue;
    }

    /**
     * 获取list集合中范围数据
     */
    public List<String> lRange(String key, long startElement, long endElement) {
        return redisTemplate.boundListOps(key).range(startElement, endElement);
    }

    /**
     * 添加指定key集合中添加元素 先进后出原则 fengjian
     */
    public Long lPush(String key, String params) {
        Long lPushResult = 0L;
        try {
            lPushResult = redisTemplate.boundListOps(key).leftPush(params);
        } catch (Exception e) {
            logger.error("redis leftPush异常,异常原因{}", e);
        }
        return lPushResult;
    }

    /**
     * 删除list中指定元素
     */
    public Long lrem(String key, long count, String params) {
        return redisTemplate.opsForList().remove(key, count, params);

    }

    public boolean setnx(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }
}
