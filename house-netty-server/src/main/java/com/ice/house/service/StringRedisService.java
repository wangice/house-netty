package com.ice.house.service;

public interface StringRedisService {

  /**
   * 设置过期时间.
   */
  boolean expire(String key, long time/*秒.*/);

  /**
   * 获取key的值
   */
  String get(String key);

  /**
   * 设置key的值
   */
  void set(String key, String value);

  /**
   * 设置key的值，并设置过期时间
   */
  void set(String key, String value, long expire);


}
