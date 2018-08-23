package com.ice.house.msg;

/**
 * @author:ice
 * @Date: 2018/8/12 0012
 */
public abstract class ModbusMsg {
    public ModbusHeader header;


    /**
     * 将内容解析
     */
    public abstract void decode(byte[] bytes);

    /**
     * 转化为字符串
     */
    public abstract byte[] bytes();
}
