package com.ice.house.server.httpHandler;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author ice
 * @Date 2019/4/19 15:38
 */
public interface OuterProxyHttpService {
    /**
     * 处理业务逻辑
     */
    void handlerHttpMsg(ChannelHandlerContext ctx, String body);
}
