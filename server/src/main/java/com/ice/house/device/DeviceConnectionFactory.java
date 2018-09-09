package com.ice.house.device;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:ice
 * @Date: 2018/9/9 0009
 */
public class DeviceConnectionFactory {


  private ConcurrentHashMap<String /* 设备ID. */, DeviceConnect> connects = new ConcurrentHashMap<>();

  private DeviceConnectionFactory() {
  }

  public static final DeviceConnectionFactory getInstance() {
    return SingletonHolder.install;
  }

  public DeviceConnect get(String deviceId) {
    return connects.get(deviceId);
  }

  public void put(String deviceId, DeviceConnect deviceConnect) {
    connects.put(deviceId, deviceConnect);
  }

  public void remove(String deviceId) {
    connects.remove(deviceId);
  }

  /**
   * 懒加载实现线程安全.
   */
  private static class SingletonHolder {

    public static final DeviceConnectionFactory install = new DeviceConnectionFactory();
  }
}
