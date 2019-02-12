package netty.client;

import android.util.Log;

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
import netty.packet.out.AddMarker.AddFlagMarkerOUT;
import netty.packet.out.AddMarker.AddHQMarkerOUT;
import netty.packet.out.AddMarker.AddMissionMarkerOUT;
import netty.packet.out.AddMarker.AddRespawnMarkerOUT;
import netty.packet.out.AddMarker.AddTacticalMarkerOUT;
import netty.packet.out.AuthPacketOUT;
import netty.packet.out.ClientPositionOUT;
import netty.packet.out.ClientStatusUpdateOUT;
import netty.packet.out.RefreshPacketOUT;
import netty.packet.out.RemoveMarker.RemoveTacticalMarkerOUT;
import pro.oblivioncoding.yonggan.airsoftgps.LoginActivity;

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
                    Log.i("NettyConnectionSuccess", "Successfully connected to the Server with the Channel-ID: " + channel.id());

                } else {
                    group.shutdownGracefully();
                }
            });

        } catch (final Exception ex) {

            group.shutdownGracefully();

            //noinspection ConstantConditions
            if (ex instanceof ConnectException) {
                Log.i("NettyConnectionError", "Can´t connect to the Server: " + ex.getMessage());
                LoginActivity.loginConsumer.accept(false);
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

    public static void sendClientStatusPositionOUTPackage(boolean alive, boolean underfire, boolean mission, boolean support) {
        if (channel != null) {
            if (channel.isWritable()) {
                channel.writeAndFlush(new ClientStatusUpdateOUT(username, alive, underfire, mission, support));
            }
        }
    }

    public static void sendAddTacticalMarkerOUTPackage(double latitude, double longitude, String teamname, String title, String description) {
        if (channel != null) {
            if (channel.isWritable()) {
                channel.writeAndFlush(new AddTacticalMarkerOUT(latitude, longitude, teamname, title, description, username));
            }
        }
    }
    public static void sendAddMissionMarkerOUTPackage(double latitude, double longitude, String title, String description) {
        if (channel != null) {
            if (channel.isWritable()) {
                channel.writeAndFlush(new AddMissionMarkerOUT(latitude, longitude, title, description, username));
            }
        }
    }
    public static void sendAddRespawnMarkerOUTPackage(double latitude, double longitude, String title, String description) {
        if (channel != null) {
            if (channel.isWritable()) {
                channel.writeAndFlush(new AddRespawnMarkerOUT(latitude, longitude, title, description, username));
            }
        }
    }
    public static void sendAddHQMarkerOUTPackage(double latitude, double longitude, String title, String description) {
        if (channel != null) {
            if (channel.isWritable()) {
                channel.writeAndFlush(new AddHQMarkerOUT(latitude, longitude, title, description, username));
            }
        }
    }
    public static void sendAddFlagMarkerOUTPackage(double latitude, double longitude, String title, String description) {
        if (channel != null) {
            if (channel.isWritable()) {
                channel.writeAndFlush(new AddFlagMarkerOUT(latitude, longitude, title, description, username));
            }
        }
    }

    public static void sendRemoveTacticalMarkerOUTPackage(int markerID){
        if(channel != null){
            if(channel.isWritable()){
                RemoveTacticalMarkerOUT removeTacticalMarkerOUT = new RemoveTacticalMarkerOUT(markerID, username);
                channel.writeAndFlush(removeTacticalMarkerOUT);
            }
        }
    }

    public static void sendRefreshPacketOUT(){
        if(channel != null){
            if(channel.isWritable()){
                channel.writeAndFlush(new RefreshPacketOUT());
            }
        }
    }

    public static String getUsername() {
        return username;
    }
}
