package com.ice.house.server.httpHandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 处理公共部分内容
 *
 * @author ice
 * @Date 2019/4/19 15:40
 */
@Component
public abstract class AbstractOuterProxyHttpService implements OuterProxyHttpService {
    public void send(ChannelHandlerContext ctx, String msg) {
        send(ctx, msg, HttpResponseStatus.OK);
    }

    public void sendAndClose(ChannelHandlerContext ctx, String msg, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                status, Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        response.headers().set(HttpHeaderNames.ACCEPT_CHARSET, "UTF-8");
        response.headers().add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);//关闭连接
    }

    public void send(ChannelHandlerContext ctx, String msg, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                status, Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        response.headers().set(HttpHeaderNames.ACCEPT_CHARSET, "UTF-8");
        response.headers().add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        ctx.writeAndFlush(response);
    }
}
