package netty.client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.packet.PacketIN;
import netty.packet.in.LoginResponsePacketIN;
import pro.oblivioncoding.yonggan.airsoftgps.MainActivity;

public class NetworkHandler extends SimpleChannelInboundHandler<PacketIN> {

    public static boolean loggedIN = false;


    @SuppressLint("MissingPermission")
    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final PacketIN packet) {
        if (packet instanceof LoginResponsePacketIN) {
            final LoginResponsePacketIN responsePacket = ((LoginResponsePacketIN) packet);
            if (responsePacket.isSuccess()) {
                Log.i("NettyLoginSuccessfull", "Login into Database successfull");
                loggedIN = true;
                try {
                NettyClient.sendClientPositionOUTPackage(MainActivity.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(), MainActivity.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
                }catch (Exception e){
                    Log.i("NettyPositionError", "Couldn´t send first Position to the Server due to missing Location Permissions.");
                }

            } else {
                Log.i("NettyLoginError", "Login into Database not successfull");
                loggedIN = false;
            }
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        //    controller.logoutScene();
        //    cause.printStackTrace();
    }
}
