package com.ice.house.server;

import com.ice.house.enums.OuterProxyEnums;
import com.ice.house.server.httpHandler.OuterProxyHttpServiceFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * @author:ice
 * @Date: 2018/9/1 0001
 */
@Sharable
@Component
@Qualifier("httpHandler")
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    private volatile int readIdleTimes;

    @Autowired
    private OuterProxyHttpServiceFactory outerProxyHttpServiceFactory;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        HttpMethod method = request.method();
        ByteBuf content = request.content();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        logger.debug("http request method:{},url:{}", method.name(), queryStringDecoder.path());
        byte[] req = new byte[content.readableBytes()];
        content.readBytes(req);

        outerProxyHttpServiceFactory.callProxyHttpService(OuterProxyEnums.getClassName(queryStringDecoder.path()), ctx, new String(req));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("连接：{}", ctx.channel().remoteAddress());
        ctx.writeAndFlush("客户端" + InetAddress.getLocalHost().getHostName() + "成功与服务端建立连接！ ");
        super.channelActive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;

        String eventType = null;
        switch (event.state()) {
            case READER_IDLE:
                eventType = "读空闲";
                readIdleTimes++; // 读空闲的计数加1
                break;
            case WRITER_IDLE:
                eventType = "写空闲";
                // 不处理
                break;
            case ALL_IDLE:
                eventType = "读写空闲";
                // 不处理
                break;
        }
        logger.debug(ctx.channel().remoteAddress() + "超时事件：" + eventType);
        if (readIdleTimes > 3) {
            logger.info(" [server]读空闲超过3次，关闭连接");
            ctx.channel().writeAndFlush("you are out");
            ctx.channel().close();
        }
    }

    /**
     * 清理空闲次数
     */
    public boolean clearReadIdleTimes() {
        readIdleTimes = 0;
        return true;
    }
}
