package com.ice.house.server;

import com.ice.house.enums.OuterProxyEnums;
import com.ice.house.server.httpHandler.OuterProxyHttpServiceFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

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
        if (method == HttpMethod.GET) {
            logger.info("GET 请求");
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
            Map<String, List<String>> parameters = queryStringDecoder.parameters();
            logger.info("请求路径：{}", queryStringDecoder.path());
            logger.info("请求参数：{}", parameters);

            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                    HttpResponseStatus.OK, Unpooled.copiedBuffer("服务器已接受请求", CharsetUtil.UTF_8));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            response.headers().set(HttpHeaderNames.ACCEPT_CHARSET, "UTF-8");
            response.headers().add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);//关闭连接
        } else if (method == HttpMethod.POST) {
            logger.info("POST 请求");
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
            ByteBuf content = request.content();
            byte[] req = new byte[content.readableBytes()];
            content.readBytes(req);
            outerProxyHttpServiceFactory.callProxyHttpService(OuterProxyEnums.getClassName(method.name(), queryStringDecoder.path()), ctx, new String(req));
        } else {
            logger.info(" 不支持的请求");
            ctx.channel().writeAndFlush("不支持的请求");
            return;
        }
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
