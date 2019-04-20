package com.ice.house.server.httpHandler;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ice
 * @Date 2019/4/19 15:42
 */
@Service
public class OuterProxyHttpServiceFactory {

    private Map<String, OuterProxyHttpService> map = new HashMap<>();

    public OuterProxyHttpServiceFactory(List<OuterProxyHttpService> list) {
        for (OuterProxyHttpService service : list) {
            map.put(service.getClass().getSimpleName(), service);
        }
    }

    public void callProxyHttpService(String type, ChannelHandlerContext ctx, String body) {
        map.get(type).handlerHttpMsg(ctx, body);
    }

}
