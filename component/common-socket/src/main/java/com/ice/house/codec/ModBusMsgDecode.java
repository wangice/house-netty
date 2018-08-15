package com.ice.house.codec;

import com.ice.house.Misc;
import com.ice.house.msg.ModbusHeader;
import com.ice.house.msg.ModbusMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author:ice
 * @Date: 2018/8/12 0012
 */
public class ModBusMsgDecode extends LengthFieldBasedFrameDecoder {

    private static final Logger logger = LoggerFactory.getLogger(ModBusMsgDecode.class);

    public ModBusMsgDecode(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in == null) {
            return null;
        }

        if (in.readableBytes() < ModbusHeader.MODBUS_HEADER_LEN) {
            return null;
        }
        in.markReaderIndex();
        ModbusHeader header = new ModbusHeader();
        header.tid = in.readShort();
        header.version = in.readByte();
        header.len = in.readInt();
        header.fcode = in.readByte();
        if (in.readableBytes() != header.len) {//标记的长度不符合
            in.resetReaderIndex();
            return null;
        }
        //读取body
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        return new ModbusMsg(header, bytes);
    }
}
