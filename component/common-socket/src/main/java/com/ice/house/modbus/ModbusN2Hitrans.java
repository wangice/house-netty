package com.ice.house.modbus;

import com.ice.house.msg.ModbusMsg;

import java.util.function.Consumer;

/**
 * N2H连接上的主动(initiative, libtsc发送MODBUS-TCP请求, 接收MODBUS-TCP响应)事务.
 *
 * @author:ice
 * @Date: 2018/8/14 21:32
 */
public class ModbusN2Hitrans {

  public ModbusN2H n2h; //事物发起者

  public short tid;//事务ID

  public ModbusMsg modbusMsg;//事务上的消息

  public int retry;//如果超时，重传次数

  public int rc; //已重传次数

  public boolean tm;//是否超时

  public long gts;//事务发起时间

  public long lpts;//事务上最近一次收到包的时间戳.

  public Consumer<ModbusN2Hitrans> rspCb;//响应回调

  public Consumer<ModbusN2Hitrans> tmCb;//超时回调

  public ModbusN2Hitrans(ModbusN2H n2h, short tid, Consumer<ModbusN2Hitrans> rspCb, /* 响应回调. */
      Consumer<ModbusN2Hitrans> tmCb /* 超时回调. */, int retry) {
    this.n2h = n2h;
    this.tid = tid;
    this.rspCb = rspCb;
    this.tmCb = tmCb;
    this.gts = System.currentTimeMillis();
    this.lpts = this.gts;
    this.tm = false;
    this.retry = retry;
    this.rc = 0;
  }
}
