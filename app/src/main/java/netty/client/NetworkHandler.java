package netty.client;

import android.annotation.SuppressLint;
import android.location.LocationManager;
import android.util.Log;

import com.google.gson.JsonElement;

import java.sql.Timestamp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.packet.PacketIN;
import netty.packet.in.AddFlagMarkerIN;
import netty.packet.in.AddHQMarkerIN;
import netty.packet.in.AddMissionMarkerIN;
import netty.packet.in.AddRespawnMarkerIN;
import netty.packet.in.AddTacticalMarkerIN;
import netty.packet.in.LoginResponsePacketIN;
import netty.packet.in.ClientAllPositionsIN;
import netty.packet.in.OrgaAuthIN;
import pro.oblivioncoding.yonggan.airsoftgps.LoginActivity;
import pro.oblivioncoding.yonggan.airsoftgps.MainActivity;

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
                    NettyClient.sendClientStatusPositionOUTPackage(MainActivity.alive, MainActivity.underFire, MainActivity.mission, MainActivity.support);
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
                Log.i("NettyAllPosition", jsonElement.getAsJsonObject().get("username").getAsString());

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
                if (MainActivity.showAreaPolygons) {
                    MainActivity.mapFragment.removeAreaPolygons();
                    MainActivity.mapFragment.setAreaPolygons();
                }

                if (MainActivity.showAreaCircles) {
                    MainActivity.mapFragment.removeAreaCircles();
                    MainActivity.mapFragment.setAreaCircles();
                }
            }
        } else if (packet instanceof OrgaAuthIN) {
            final OrgaAuthIN orgaAuthIN = (OrgaAuthIN) packet;
            if (orgaAuthIN.isSuccessful()) {
                MainActivity.enableOrga();
                Log.i("Orga", "Incoming OrgaAuth Packet");
                MainActivity.tacticalMarker = orgaAuthIN.isTacticalMarker();
                MainActivity.missionMarker = orgaAuthIN.isMissionMarker();
                MainActivity.hqMarker = orgaAuthIN.isHqMarker();
                MainActivity.respawnMarker = orgaAuthIN.isRespawnMarker();
                MainActivity.flagMarker = orgaAuthIN.isFlagMarker();
            }
        } else if (packet instanceof AddTacticalMarkerIN) {
            AddTacticalMarkerIN addTacticalMarkerIN = (AddTacticalMarkerIN) packet;
            Log.i("Pins", String.valueOf(addTacticalMarkerIN.getId()));
                MainActivity.getMapFragment().addTacticalMarker(
                        addTacticalMarkerIN.getLatitude(),
                        addTacticalMarkerIN.getLongitude(),
                        addTacticalMarkerIN.getMarkerID(),
                        addTacticalMarkerIN.getTitle(),
                        addTacticalMarkerIN.getDescription(),
                        addTacticalMarkerIN.getTeamname(),
                        addTacticalMarkerIN.getCreator());
        } else if (packet instanceof AddMissionMarkerIN) {
            Log.i("Pins", "AddMissionMarkerIN");
            AddMissionMarkerIN addMissionMarkerIN = (AddMissionMarkerIN) packet;
            MainActivity.getMapFragment().addMissionMarker(addMissionMarkerIN.getLatitude(),addMissionMarkerIN.getLongitude(), addMissionMarkerIN.getMarkerID(), addMissionMarkerIN.getTitle(), addMissionMarkerIN.getDescription(), addMissionMarkerIN.getCreator());
        } else if (packet instanceof AddRespawnMarkerIN) {
            Log.i("Pins", "AddRespawnMarkerIN");
            AddRespawnMarkerIN addRespawnMarkerIN = (AddRespawnMarkerIN) packet;
            MainActivity.getMapFragment().addRespawnMarker(addRespawnMarkerIN.getLatitude(), addRespawnMarkerIN.getLongitude(),addRespawnMarkerIN.getMarkerID(), addRespawnMarkerIN.getTitle(), addRespawnMarkerIN.getDescription(), addRespawnMarkerIN.getCreator(), addRespawnMarkerIN.isOwn());
        } else if (packet instanceof AddHQMarkerIN) {
            Log.i("Pins", "AddHQMarkerIN");
            AddHQMarkerIN addHQMarkerIN = (AddHQMarkerIN) packet;
            MainActivity.getMapFragment().addHQMarker(addHQMarkerIN.getLatitude(), addHQMarkerIN.getLongitude(), addHQMarkerIN.getMarkerID(), addHQMarkerIN.getTitle(), addHQMarkerIN.getDescription(), addHQMarkerIN.getCreator(), addHQMarkerIN.isOwn());
        } else if (packet instanceof AddFlagMarkerIN) {
            Log.i("Pins", "AddFlagMarkerIN");
            AddFlagMarkerIN addFlagMarkerIN = (AddFlagMarkerIN) packet;
            MainActivity.getMapFragment().addFlagMarker(addFlagMarkerIN.getLatitude(), addFlagMarkerIN.getLongitude(), addFlagMarkerIN.getMarkerID(), addFlagMarkerIN.getTitle(),addFlagMarkerIN.getDescription(), addFlagMarkerIN.getCreator(), addFlagMarkerIN.isOwn());
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        Log.i("NetworkHandlerError", cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        MainActivity.connectToServer(LoginActivity.username, LoginActivity.password, LoginActivity.HOST, LoginActivity.PORT);
    }
}
