package netty.packet.out;

import com.google.gson.JsonObject;

import netty.packet.PacketOUT;

public class ClientPositionOUT implements PacketOUT {

    private double latitude, longitude;
    private String username;

    public ClientPositionOUT(double latitude, double longitude, String username) {
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void write(JsonObject jsonObject) {
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("latitude", latitude);
        jsonObject.addProperty("longitude", longitude);
    }

    @Override
    public int getId() {
        return 2;
    }
}
