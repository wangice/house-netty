package com.ice.house.server;

import com.ice.house.codec.ModBusMsgDecode;
import com.ice.house.codec.ModBusMsgEncode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author:ice
 * @Date: 2018/8/11 14:53
 */
@Component
@Qualifier("springProtocolInitializer")
public class StringProtocolInitalizer extends ChannelInitializer<SocketChannel> {

    private static final int MAX_FRAME_LENGTH = 1024 * 1024;  //最大长度
    private static final int LENGTH_FIELD_LENGTH = 4;  //长度字段所占的字节数
    private static final int LENGTH_FIELD_OFFSET = 3;  //长度偏移

    @Autowired
    @Qualifier("tcpSocketAddress")
    private InetSocketAddress tcpPort;

    @Autowired
    @Qualifier("httpSocketAddress")
    private InetSocketAddress httpPort;

    @Autowired
    private ServerHandler serverHandler;

    @Autowired
    private HttpHandler httpHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (ch.localAddress().getPort() == tcpPort.getPort()) {
            pipeline.addLast("decoder",
                    new ModBusMsgDecode(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH));
            pipeline.addLast("handler", serverHandler);
            pipeline.addLast("encoder", new ModBusMsgEncode());
        }
        if (ch.localAddress().getPort() == httpPort.getPort()) {
            pipeline.addLast("http-decoder", new HttpRequestDecoder());
            pipeline.addLast("http-aggregator",
                    new HttpObjectAggregator(65536));//将多个消息转换为单一的FullHttpRequest或FullHttpResponse对象
            pipeline.addLast("http-encoder", new HttpResponseEncoder());
            pipeline.addLast("http-chunked",
                    new ChunkedWriteHandler());//ChunkedWriteHandler的主要作用是支持异步发送大的码流,但不占用过多的内存,防止JAVA内存溢出
            pipeline.addLast(new IdleStateHandler(20, 20, 20, TimeUnit.SECONDS));//添加心跳检测，检测的方式读空闲2秒或者是写空闲2秒，或者都空闲2秒时发送心跳
            pipeline.addLast("httpServerHandler", httpHandler);
        }
    }
}
