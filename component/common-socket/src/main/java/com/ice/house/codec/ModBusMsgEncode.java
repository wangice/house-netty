package com.ice.house.codec;

import com.ice.house.msg.ModbusMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author:ice
 * @Date: 2018/8/11 15:07
 */
public class ModBusMsgEncode extends MessageToByteEncoder<ModbusMsg> {

    private static final Logger logger = LoggerFactory.getLogger(ModBusMsgEncode.class);

    private int ofst = 0;

    @Override
    protected void encode(ChannelHandlerContext ctx, ModbusMsg modbusMsg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(modbusMsg.bytes());
    }
}
