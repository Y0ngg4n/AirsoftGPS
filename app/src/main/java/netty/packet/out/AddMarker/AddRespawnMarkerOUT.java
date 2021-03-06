package netty.packet.out.AddMarker;

import com.google.gson.JsonObject;

import netty.packet.PacketOUT;

public class AddRespawnMarkerOUT implements PacketOUT {

    private double latitude, longitude;

    private String title, description, username;

    private boolean own;

    public AddRespawnMarkerOUT(double latitude, double longitude, String title, String description, String username, boolean own) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.username = username;
        this.own = own;
    }

    @Override
    public void write(JsonObject jsonObject) {
        jsonObject.addProperty("latitude", latitude);
        jsonObject.addProperty("longitude", longitude);
        jsonObject.addProperty("title", title);
        jsonObject.addProperty("description", description);
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("own", own);
    }

    @Override
    public int getId() {
        return 9;
    }


}
