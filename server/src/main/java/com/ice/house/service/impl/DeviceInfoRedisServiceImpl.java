package com.ice.house.service.impl;

import com.ice.house.model.DeviceInfo;
import com.ice.house.service.IRedisService;

/**
 * @author:ice
 * @Date: 2018/9/9 0009
 */
public class DeviceInfoRedisServiceImpl extends IRedisService<DeviceInfo> {

  private static final String REDIS_KEY = "DEVICE_REDIS_KEY";

  @Override
  protected String getRedisKey() {
    return REDIS_KEY;
  }
}
