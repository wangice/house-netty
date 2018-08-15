package com.ice.house.server;

import com.ice.house.ModbusHeader;
import com.ice.house.ModbusMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author:ice
 * @Date: 2018/8/12 0012
 */
public class ModBusMsgDecode extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(ModBusMsgDecode.class);


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < ModbusHeader.MODBUS_HEADER_LEN) {
            return;
        }
        int beginReader;  //记录包头开始的index
        while (true) {
            beginReader = byteBuf.readerIndex();
            byteBuf.markReaderIndex();//标记包开头的index;
        }
    }
}
