package com.ice.house.msg;

/**
 * @author:ice
 * @Date: 2018/8/12 0012
 */
public abstract class ModbusMsg {
    public ModbusHeader header;

    public abstract byte[] bytes();
    
}
