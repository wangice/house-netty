package com.ice.house.server;

import com.ice.house.ModbusMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author:ice
 * @Date: 2018/8/11 15:07
 */
public class ModBusMsgEncode extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(ModBusMsgEncode.class);

    private int ofst = 0;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {


    }
}
