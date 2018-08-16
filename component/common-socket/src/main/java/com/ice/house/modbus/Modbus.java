package com.ice.house.modbus;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:ice
 * @Date: 2018/8/16 0016
 */
public class Modbus {

    public static final byte FC_HEARTBEAT = (byte) 0x01; /* 服务器向客户端发起的心跳. */

    public static List<Byte> serverFcode = new ArrayList<>();

    static {
        serverFcode.add(FC_HEARTBEAT);
    }


}
