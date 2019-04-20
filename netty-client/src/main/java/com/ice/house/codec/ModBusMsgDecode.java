package com.ice.house.codec;

import com.ice.house.modbusmsg.DeviceInfoReq;
import com.ice.house.modbusmsg.HeartBeatRsp;
import com.ice.house.msg.Modbus;
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
            logger.warn("传入数据为空");
            return null;
        }

        if (in.readableBytes() < ModbusHeader.MODBUS_HEADER_LEN) {
            logger.warn("dfd");
            return null;
        }
        in.markReaderIndex();
        ModbusHeader header = new ModbusHeader();
        header.tid = in.readShort();
        header.version = in.readByte();
        header.len = in.readInt();
        header.fcode = in.readByte();
        if (in.readableBytes() != header.len) {//标记的长度不符合
            logger.info("长度不符：{},header len:{}", in.readableBytes(), header.len);
            in.resetReaderIndex();
            return null;
        }
        //读取body
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        return this.decode(header.fcode, header, bytes);
    }

    /**
     * 将返回的值转化为不同类型.
     */
    private ModbusMsg decode(byte fcode, ModbusHeader header, byte[] bytes) {
        switch (fcode) {
            case Modbus.FC_HEARTBEAT://心跳
                return new HeartBeatRsp(header, bytes);
            case Modbus.FC_DEVICEINFO:
                System.out.println("解析");
                return new DeviceInfoReq(header, bytes);
            default:
                return null;
        }
    }
}
