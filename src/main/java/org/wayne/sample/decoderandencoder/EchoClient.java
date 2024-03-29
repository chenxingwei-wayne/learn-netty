package org.wayne.sample.decoderandencoder;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {
    public void connect(int port, String host) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();

            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            // DelimiterBasedFrameDecoder 与 StringDecoder需要搭配使用。
                            // DelimiterBasedFrameDecoder：自定义分隔符
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            //设置为字符串形式的解码:将传递的buf改为String
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            // 发起异步链接操作
            ChannelFuture f = b.connect(host, port).sync();
            // 等待客户端连接关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            group.shutdownGracefully();

        }
    }

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new EchoClient().connect(port, "127.0.0.1");
    }
}

