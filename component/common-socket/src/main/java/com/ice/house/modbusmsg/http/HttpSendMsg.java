package com.ice.house.modbusmsg.http;

/**
 * @author:ice
 * @Date: 2018/9/2 0002
 */
public class HttpSendMsg {

  public String clientId;//发送客户端的消息
  public MsgBody msg;//消息的内容

  public static class MsgBody {

    public long sendTime;//发送消息的时间
    public String title;//标题
    public String content;//内容
    public String icon;//图标
  }
}
