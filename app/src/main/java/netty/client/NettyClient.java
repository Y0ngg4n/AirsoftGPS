package netty.client;

import android.util.Log;

import java.math.BigDecimal;
import java.net.ConnectException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import netty.packet.PacketDecoder;
import netty.packet.PacketEncoder;
import netty.packet.out.AuthPacketOUT;
import netty.packet.out.ClientPositionOUT;

public class NettyClient {

    private static String username;

    private static Channel channel;

    public NettyClient(final String username, final String password, final String host, final int port) {
        NettyClient.username = username;
        final EventLoopGroup group = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        final Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(final SocketChannel ch) {
                        ch.pipeline().
                                addLast(new PacketDecoder()).
                                addLast(new LengthFieldPrepender(4, true)).
                                addLast(new PacketEncoder()).
                                addLast(new NetworkHandler());
                    }
                });

        try {

            final ChannelFuture channelFuture = bootstrap.connect(host, port).syncUninterruptibly();


            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    channel = channelFuture.channel();
                    channel.writeAndFlush(new AuthPacketOUT(username, password));
                    Log.i("NettyConnectionSuccess", "Succesfully connected to the Server");
                } else {
                    group.shutdownGracefully();
                }
            });


        } catch (final Exception ex) {

            group.shutdownGracefully();

            //noinspection ConstantConditions
            if (ex instanceof ConnectException) {
                Log.i("NettyConnectionError", "Fehler beim Verbinden");
            } else {
                //TODO Implement Log Files
                ex.printStackTrace();
            }
        }
    }

    public static void sendClientPositionOUTPackage(double latitude, double longitude) {
        if (channel != null) {
            if (channel.isWritable()) {
                channel.writeAndFlush(new ClientPositionOUT(latitude, longitude, username));
            }
        }
    }

    public static String getUsername() {
        return username;
    }
}
