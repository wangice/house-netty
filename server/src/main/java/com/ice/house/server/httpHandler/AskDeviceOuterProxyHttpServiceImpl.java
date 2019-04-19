package com.ice.house.server.httpHandler;

import com.ice.house.Misc;
import com.ice.house.device.DeviceConnect;
import com.ice.house.device.DeviceConnectionFactory;
import com.ice.house.modbusmsg.http.HttpSendMsg;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author ice
 * @Date 2019/4/19 15:59
 */
@Service
public class AskDeviceOuterProxyHttpServiceImpl extends AbstractOuterProxyHttpService {

    private static final Logger logger = LoggerFactory.getLogger(AskDeviceOuterProxyHttpServiceImpl.class);

    @Override
    public void handlerHttpMsg(ChannelHandlerContext ctx, String body) {
        HttpSendMsg httpSendMsg = Misc.json2Obj(body, HttpSendMsg.class);
        if (httpSendMsg.clientId == null || httpSendMsg.msg == null) {
            logger.info("it is a unvalid msg");
            send(ctx, "it is a unvalid msg");
        }
        DeviceConnect deviceConnect = DeviceConnectionFactory.getInstance()
                .get(httpSendMsg.clientId);
        //发送信息.
        logger.info("send msg client:{},msg:{}", httpSendMsg.clientId, Misc.obj2json(httpSendMsg.msg));
    }
}
