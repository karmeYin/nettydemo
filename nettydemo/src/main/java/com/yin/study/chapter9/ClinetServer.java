package com.yin.study.chapter9;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class ClinetServer{



        public static void main(String[] args) {
            final AttributeKey<Integer> id = AttributeKey.valueOf("ID");
            NioEventLoopGroup nioEventLoopGroup=new NioEventLoopGroup();
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class).handler(new SimpleChannelInboundHandler<ByteBuf>(){
            @Override
            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                Integer idValue = ctx.channel().attr(id).get();
            }
            @Override
            protected void channelRead0(
                    ChannelHandlerContext channelHandlerContext,
                    ByteBuf byteBuf) throws Exception {
                System.out.println("Reveived data");
                byteBuf.clear();
            }
        });
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        bootstrap.attr(id, 123456);
        ChannelFuture future = bootstrap.connect(
        new InetSocketAddress("www.manning.com", 80));
        future.syncUninterruptibly();
        }

}
