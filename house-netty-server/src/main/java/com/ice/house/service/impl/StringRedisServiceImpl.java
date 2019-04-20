package com.ice.house.service.impl;

import com.ice.house.service.StringRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author:ice
 * @Date: 2018/9/2 0002
 */
@Service
public class StringRedisServiceImpl implements StringRedisService {

  @Autowired
  protected RedisTemplate<String, Object> redisTemplate;
  @Resource
  private ValueOperations<String, String> valueOperations;

  @Override
  public boolean expire(String key, long time/*秒.*/) {
    if (time < 1) {
      return false;
    }
    redisTemplate.expire(key, time, TimeUnit.SECONDS);
    return true;
  }

  @Override
  public String get(String key) {
    if (key == null) {
      return null;
    }
    return valueOperations.get(key);
  }

  @Override
  public void set(String key, String value) {
    valueOperations.set(key, value);
  }

  @Override
  public void set(String key, String value, long expire) {
    if (expire > 0) {
      valueOperations.set(key, value, expire, TimeUnit.SECONDS);
    } else {
      set(key, value);
    }
  }
}
