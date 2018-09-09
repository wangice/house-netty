package com.ice.house.server;

import com.ice.house.Misc;
import com.ice.house.configuration.Config;
import com.ice.house.device.DeviceConnect;
import com.ice.house.device.DeviceConnectionFactory;
import com.ice.house.modbusmsg.http.HttpSendMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
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

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
    HttpMethod method = request.method();
    ByteBuf content = request.content();
    QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
    logger.debug("http request method:{},url:{}", method.name(), queryStringDecoder.path());
    if (method != HttpMethod.POST) {
      logger.info("request method is not valid");
      sendError(ctx, "request method is not valid");
      return;
    }
    byte[] req = new byte[content.readableBytes()];
    content.readBytes(req);
    if (queryStringDecoder.path() == Config.SENDMSG) {//向客户端发送消息
      HttpSendMsg httpSendMsg = Misc.json2Obj(new String(req), HttpSendMsg.class);
      if (httpSendMsg.clientId == null || httpSendMsg.msg == null) {
        logger.info("it is a unvalid msg");
        sendError(ctx, "it is a unvalid msg");
        return;
      }
      DeviceConnect deviceConnect = DeviceConnectionFactory.getInstance()
          .get(httpSendMsg.clientId);
      //发送信息.

      logger
          .info("send msg client:{},msg:{}", httpSendMsg.clientId, Misc.obj2json(httpSendMsg.msg));
    }
  }

  private void sendError(ChannelHandlerContext ctx, String msg) {
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
        HttpResponseStatus.OK, Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

  }
}
