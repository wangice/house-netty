package com.ice.house.service.impl;

import com.ice.house.model.User;
import com.ice.house.service.IRedisService;

/**
 * @author:ice
 * @Date: 2018/9/2 0002
 */
public class UserRedisServiceImpl extends IRedisService<User> {

  private static final String REDIS_KEY = "USER_REDIS_KEY";

  @Override
  protected String getRedisKey() {
    return UserRedisServiceImpl.REDIS_KEY;
  }
}
