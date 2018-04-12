package com.yin.study.chapter3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class NettyNioServer {
    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); //1
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() { //3
                           @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new ChannelInboundHandlerAdapter() {
                                   @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                       // 连接后，写消息到客户端，写完后便关闭连接
                                        ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
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
}/*
1 Create ServerBootstrap to allow bootstrap to server
#2 Use NioEventLoopGroup forI nonblocking mode
#3 Specify ChannelInitializer called for each accepted connection
#4 Add ChannelHandler to intercept events and allow to react on them
#5 Write message to client and add ChannelFutureListener to close connection once message written
#6 Bind server to accept connections
#7 Release all resources
*/
