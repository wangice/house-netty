package com.ice.house.modbus;

import com.ice.house.msg.ModbusMsg;
import java.util.ArrayList;

/**
 * N2H连接上的被动(passive, libtsc收到MODBUS-TCP请求, 回送MODBUS-TCP响应)事务.
 *
 * @author:ice
 * @Date: 2018/8/14 22:01
 */
public class ModbusN2Hptrans {

  /**
   * 事务发起者.
   */
  public ModbusN2H n2h;
  /**
   * 事务上请求消息.
   */
  public ModbusMsg req;
  /**
   * 事务上响应消息.
   */
  public ArrayList<ModbusMsg> rsp = new ArrayList<>();

  public ModbusN2Hptrans(ModbusN2H n2h, ModbusMsg req) {
    this.n2h = n2h;
    this.req = req;
  }

  /**
   * 事务结束.
   */
  public final void end(ModbusMsg rsp) {
    if (rsp != null) {
      this.rsp.add(rsp);
      this.n2h.future(v -> {
        for (int i = 0; i < this.rsp.size(); ++i) {
//          this.n2h.
        }

      });
    } else {
      this.n2h.future(v -> System.out.println("事务结束"));
    }
  }
}
