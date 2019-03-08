package netty.client;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
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
import netty.packet.out.RemoveMarker.RemoveFlagMarkerOUT;
import netty.packet.out.RemoveMarker.RemoveHQMarkerOUT;
import netty.packet.out.RemoveMarker.RemoveMissionMarkerOUT;
import netty.packet.out.RemoveMarker.RemoveRespawnMarkerOUT;
import netty.packet.out.RemoveMarker.RemoveTacticalMarkerOUT;
import netty.packet.out.UpdateMarker.UpdateFlagMarkerOUT;
import pro.oblivioncoding.yonggan.airsoftgps.LoginActivity;
import pro.oblivioncoding.yonggan.airsoftgps.MainActivity;

public class NettyClient {

    private static String username;

    private static Channel channel;

    public NettyClient(final String username, final String password, final String host, final int port) {
        NettyClient.username = username;
        final EventLoopGroup group = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        SslContext handler = null;
//        try {
//            handler = getHandler("");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        }
//
//        SslHandler sslHandler = handler.newHandler(channel.alloc());


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
            channel = channelFuture.syncUninterruptibly().channel();
            channel.writeAndFlush(new AuthPacketOUT(username, password));
            Log.i("NettyConnectionSuccess", "Successfully connected to the Server with the Channel-ID: " + channel.id());

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

    private InputStream getSSLCertificate() {
        try {
            URL url = new URL("https://" + LoginActivity.HOST + "/certificates/publickey.php");
            return url.openStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
//            String certificate = "";
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                certificate += line;
//            }
//            bufferedReader.close();
//            Log.i("SSLCert", certificate);
//            return certificate;
        } catch (IOException e) {
            Log.i("SSLCertificate", "Can´t get SSL Certificate");
            Log.i("SSLCertificate", e.getMessage());
        }
        return null;
    }

    public SslContext getHandler(String certificateString) throws IOException, CertificateException, CertificateException {

//        final X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(getSSLCertificate());

        return SslContextBuilder.forClient().trustManager(getSSLCertificate()).build();
//        return SslContextBuilder.forClient(privateKey, certificate).build();
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

    public static void sendAddRespawnMarkerOUTPackage(double latitude, double longitude, String title, String description, boolean own) {
        if (channel != null) {
            if (channel.isWritable()) {
                channel.writeAndFlush(new AddRespawnMarkerOUT(latitude, longitude, title, description, username, own));
            }
        }
    }

    public static void sendAddHQMarkerOUTPackage(double latitude, double longitude, String title, String description, boolean own) {
        if (channel != null) {
            if (channel.isWritable()) {
                channel.writeAndFlush(new AddHQMarkerOUT(latitude, longitude, title, description, username, own));
            }
        }
    }

    public static void sendAddFlagMarkerOUTPackage(double latitude, double longitude, String title, String description, boolean own) {
        if (channel != null) {
            if (channel.isWritable()) {
                channel.writeAndFlush(new AddFlagMarkerOUT(latitude, longitude, title, description, username, own));
            }
        }
    }

    public static void sendRemoveTacticalMarkerOUTPackage(int markerID) {
        if (channel != null) {
            if (channel.isWritable()) {
                RemoveTacticalMarkerOUT removeTacticalMarkerOUT = new RemoveTacticalMarkerOUT(markerID, username);
                channel.writeAndFlush(removeTacticalMarkerOUT);
            }
        }
    }

    public static void sendRemoveMissionMarkerOUTPackage(int markerID) {
        if (channel != null) {
            if (channel.isWritable()) {
                RemoveMissionMarkerOUT removeMissionMarkerOUT = new RemoveMissionMarkerOUT(markerID, username);
                channel.writeAndFlush(removeMissionMarkerOUT);
            }
        }
    }

    public static void sendRemoveRespawnMarkerOUTPackage(int markerID) {
        if (channel != null) {
            if (channel.isWritable()) {
                RemoveRespawnMarkerOUT removeRespawnMarkerOUT = new RemoveRespawnMarkerOUT(markerID, username);
                channel.writeAndFlush(removeRespawnMarkerOUT);
            }
        }
    }

    public static void sendRemoveHQMarkerOUTPackage(int markerID) {
        if (channel != null) {
            if (channel.isWritable()) {
                RemoveHQMarkerOUT removeHQMarkerOUT = new RemoveHQMarkerOUT(markerID, username);
                channel.writeAndFlush(removeHQMarkerOUT);
            }
        }
    }

    public static void sendRemoveFlagMarkerOUTPackage(int markerID) {
        if (channel != null) {
            if (channel.isWritable()) {
                RemoveFlagMarkerOUT removeFlagMarkerOUT = new RemoveFlagMarkerOUT(markerID, username);
                channel.writeAndFlush(removeFlagMarkerOUT);
            }
        }
    }

    public static void sendUpdateFlagMarkerPackageOUT(int flagID, boolean isOwned){
        if(channel != null){
            if (channel.isWritable()){
                UpdateFlagMarkerOUT updateFlagMarkerOUT = new UpdateFlagMarkerOUT(flagID, isOwned);
                channel.writeAndFlush(updateFlagMarkerOUT);
            }
        }
    }

    public static void sendRefreshPacketOUT() {
        if (channel != null) {
            if (channel.isWritable()) {
                channel.writeAndFlush(new RefreshPacketOUT());
            }
        }
    }

    public static String getUsername() {
        return username;
    }
}
