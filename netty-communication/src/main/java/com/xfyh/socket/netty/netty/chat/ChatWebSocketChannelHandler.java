package com.xfyh.socket.netty.netty.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;

/**
 * @Description: 接收、处理、响应客户端websocket请求的核心业务处理类
 * @Auther: xfyh
 * @Date: 2019/12/28 21:15
 */
public class ChatWebSocketChannelHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;
    private static final String WEB_SOCKET_URL="ws://127.0.0.1:8888/websocket";

    /**
     * 服务端处理客户端websocket请求的核心方法
     * Is called for each message of type
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 处理客户端向服务端发起http握手请求的业务
        if(msg instanceof FullHttpMessage) {
            handleHttpRequest(ctx,(FullHttpRequest)msg);
        }else if(msg instanceof WebSocketFrame) {
            //处理websocket连接业务
            handleWebSocketFrame(ctx,(WebSocketFrame)msg);
        }

    }

    /**
     * 客户端与服务端创建连接时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.add(ctx.channel());
        System.out.println("客户端与服务端建立连接");
    }

    /**
     * 客户端与服务端断开连接时调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.remove(ctx.channel());
        System.out.println("客户端与服务端断开连接");
    }

    /**
     * 服务端接受客户端发送过来的数据结束之后调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 出现异常时调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }



    /*********************************自定义的私有方法*************************/

    /**
     * 处理客户端与服务端之间的websocket业务
     * @param ctx
     * @param frame
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //是否是关闭websocket指令
        if(frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
        }
        //是否为ping消息
        if(frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //是否为二进制消息，暂时不支持，抛异常
        if(!(frame instanceof TextWebSocketFrame)) {
            System.out.println("目前我们不支持二进制消息");
            new RuntimeException("["+this.getClass().getName()+"]不支持消息");
        }

        //返回应答消息
        //获取客户端向服务端发送的消息
       String request = ((TextWebSocketFrame)frame).text();
        System.out.println("服务端接收到消息==>"+request);
        TextWebSocketFrame res = new TextWebSocketFrame(new Date().toString()+"-"+ctx.channel().id()+"===>"+request);
       //群发消息
        NettyConfig.group.writeAndFlush(res);
    }


    /**
     * 处理客户端向服务器发起http握手请求的业务
     * @param ctx
     * @param request
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        //不是websocket的握手请求
        if(!request.decoderResult().isSuccess()||!"websocket".equals(request.headers().get("Upgrade"))) {
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory(WEB_SOCKET_URL, null, false);
        handshaker = factory.newHandshaker(request);
        if(handshaker==null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else {
            handshaker.handshake(ctx.channel(), request);
        }

    }

    /**
     * 服务端向客户端响应消息
     * @param ctx
     * @param request
     * @param response
     */
    private void sendHttpResponse(ChannelHandlerContext ctx,FullHttpRequest request,DefaultFullHttpResponse response) {
        if(response.status().code()!=200) {
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
        }
        //服务端向客户端发送数据
        ChannelFuture future = ctx.channel().writeAndFlush(response);
        if(response.status().code()!=200) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }




}
