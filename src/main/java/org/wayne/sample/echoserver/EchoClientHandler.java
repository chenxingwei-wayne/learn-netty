package org.wayne.sample.echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public EchoClientHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client received: " + byteBuf.toString(CharsetUtil.UTF_8));
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 将数据写入到与ChannelHandler相关联的ChannelHandlerContext对象中，消息将从ChannelPipeline的下一个ChannelHandler开始流动。
        // 如果是直接把数据写道channel里面，将会导致消息从ChannelPipeline的尾端开始流动。
        ctx.writeAndFlush(Unpooled.copiedBuffer("NettyRocks !", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        throwable.printStackTrace();
        context.close();
    }
}
