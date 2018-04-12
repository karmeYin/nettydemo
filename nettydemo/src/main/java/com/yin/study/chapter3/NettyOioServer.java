package com.yin.study.chapter3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;


public class NettyOioServer {
    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); //1
            b.group(group) //2
                    .channel(OioServerSocketChannel.class)
            .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() { //3
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new ChannelInboundHandlerAdapter() { //4
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            ctx.write(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);//5
                                        }
                                    });
                        }
                    });
            ChannelFuture f = b.bind().sync(); //6
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync(); //7
        }
    }

/*#1 Create ServerBootstrap to allow bootstrap to server instance
        #2 Use OioEventLoopGroup Ito allow blocking mode (Old-IO)
        #3 Specify ChannelInitializer that will be called for each accepted connection
        #4 Add ChannelHandler to intercept events and allow to react on them
        #5 Write message to client and add ChannelFutureListener to close connection once message written
        #6 Bind server to accept connections
        #7 Release all resources
        */
}
