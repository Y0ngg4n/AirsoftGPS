package netty.packet.in;

import com.google.gson.JsonObject;

import netty.packet.PacketIN;

public class AddTacticalMarkerIN implements PacketIN {

    private double latitude, longitude;

    private String teamname,title, description, username;

    @Override
    public void read(JsonObject jsonObject) {
        latitude = jsonObject.get("latitude").getAsDouble();
        longitude = jsonObject.get("longitude").getAsDouble();
        title = jsonObject.get("title").getAsString();
        teamname = jsonObject.get("teamname").getAsString();
        description = jsonObject.get("description").getAsString();
        username = jsonObject.get("username").getAsString();
    }

    @Override
    public int getId() {
        return 7;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }
}
