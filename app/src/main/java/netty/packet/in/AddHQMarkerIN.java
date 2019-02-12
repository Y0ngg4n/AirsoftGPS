package netty.packet.in;

import com.google.gson.JsonObject;

import netty.packet.PacketIN;

public class AddHQMarkerIN implements PacketIN {

    private double latitude, longitude;

    private String title, description, username;

    private boolean own;

    @Override
    public void read(JsonObject jsonObject) {
        latitude = jsonObject.get("latitude").getAsDouble();
        longitude = jsonObject.get("longitude").getAsDouble();
        title = jsonObject.get("title").getAsString();
        description = jsonObject.get("description").getAsString();
        username = jsonObject.get("username").getAsString();
        own = jsonObject.get("own").getAsBoolean();
    }

    @Override
    public int getId() {
        return 10;
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

    public boolean isOwn() {
        return own;
    }
}
