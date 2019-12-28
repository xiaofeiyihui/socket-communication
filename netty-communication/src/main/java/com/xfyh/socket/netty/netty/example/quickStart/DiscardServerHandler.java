package com.xfyh.socket.netty.netty.example.quickStart;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/23 19:08
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * This method is called with the received message, whenever new data is received from a client.
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Discard the received data silently.
        //((ByteBuf) msg).release();


     /*   ByteBuf in = (ByteBuf) msg;
        try {
            // (1)
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
                System.out.flush();
            }

        } finally {
            // (2)
            ReferenceCountUtil.release(msg);
        }*/

        ctx.write(msg);
        ctx.flush();
    }

    /**
     * The exceptionCaught() event handler method is called with a Throwable when an exception was raised by Netty
     * due to an I/O error or by a handler implementation due to the exception thrown while processing events.
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
