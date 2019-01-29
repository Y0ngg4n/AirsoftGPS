package netty.client;

import android.annotation.SuppressLint;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.Timestamp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.packet.PacketIN;
import netty.packet.in.LoginResponsePacketIN;
import netty.packet.in.ClientAllPositionsIN;
import pro.oblivioncoding.yonggan.airsoftgps.LoginActivity;
import pro.oblivioncoding.yonggan.airsoftgps.MainActivity;
import pro.oblivioncoding.yonggan.airsoftgps.MapFragment;

public class NetworkHandler extends SimpleChannelInboundHandler<PacketIN> {

    public static boolean loggedIN = false;


    @SuppressLint("MissingPermission")
    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final PacketIN packet) {
        Log.i("PacketIncome", "Packet incoming: " + packet.getId());
        if (packet instanceof LoginResponsePacketIN) {
            final LoginResponsePacketIN responsePacket = ((LoginResponsePacketIN) packet);
            if (responsePacket.isSuccess()) {
                Log.i("NettyLoginSuccessfull", "Login into Database successfull");
                loggedIN = true;
                try {
                    NettyClient.sendClientPositionOUTPackage(MainActivity.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(), MainActivity.getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
                } catch (Exception e) {
                    Log.i("NettyPositionError", "Couldn´t send first Position to the Server due to missing Location Permissions.");
                }

            } else {
                Log.i("NettyLoginError", "Login into Database not successfull");
                loggedIN = false;

            }
            LoginActivity.loginConsumer.accept(loggedIN);
        } else if (packet instanceof ClientAllPositionsIN) {
            Log.i("NettyAllPosition", "All Position incoming");
            final ClientAllPositionsIN clientAllPositionsIN = ((ClientAllPositionsIN) packet);
            //TODO: Handle Data and send them
            Log.i("NettyAllPosition", String.valueOf(clientAllPositionsIN.getJsonArray()));

            for (JsonElement jsonElement : clientAllPositionsIN.getJsonArray()) {
                if (jsonElement.getAsJsonObject().get("username").getAsString().equals(NettyClient.getUsername())) {
                    Log.i("NettyAllPosition","Same username");
                    MainActivity.setStatus(jsonElement.getAsJsonObject().get("alive").getAsBoolean(), jsonElement.getAsJsonObject().get("underfire").getAsBoolean(), jsonElement.getAsJsonObject().get("mission").getAsBoolean(),jsonElement.getAsJsonObject().get("support").getAsBoolean());
                }
                MainActivity.getMapFragment().createMarker(
                        jsonElement.getAsJsonObject().get("latitude").getAsDouble(),
                        jsonElement.getAsJsonObject().get("longitude").getAsDouble(),
                        jsonElement.getAsJsonObject().get("userID").getAsInt(),
                        jsonElement.getAsJsonObject().get("username").getAsString(),
                        Timestamp.valueOf(jsonElement.getAsJsonObject().get("timestamp").getAsString()),
                        jsonElement.getAsJsonObject().get("teamname").getAsString() + " (" + jsonElement.getAsJsonObject().get("teamid").getAsString() + ")",
                        jsonElement.getAsJsonObject().get("alive").getAsBoolean(),
                        jsonElement.getAsJsonObject().get("underfire").getAsBoolean(),
                        jsonElement.getAsJsonObject().get("mission").getAsBoolean(),
                        jsonElement.getAsJsonObject().get("support").getAsBoolean()
                );
            }
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        Log.i("NetworkHandlerError", cause.getMessage());
        cause.printStackTrace();
    }
}
