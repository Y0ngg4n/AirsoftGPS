package netty.packet.in;

import com.google.gson.JsonObject;

import netty.packet.PacketIN;

public class AddFlagMarkerIN implements PacketIN {

    private double latitude, longitude;

    private String title, description, creator;

    private boolean own;

    private int markerID;
    @Override
    public void read(JsonObject jsonObject) {
        latitude = jsonObject.get("latitude").getAsDouble();
        longitude = jsonObject.get("longitude").getAsDouble();
        title = jsonObject.get("title").getAsString();
        description = jsonObject.get("description").getAsString();
        creator = jsonObject.get("creator").getAsString();
        own = jsonObject.get("own").getAsBoolean();
        markerID = jsonObject.get("markerID").getAsInt();
    }

    @Override
    public int getId() {
        return 11;
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

    public String getCreator() {
        return creator;
    }

    public int getMarkerID() {
        return markerID;
    }

    public boolean isOwn() {
        return own;
    }
}
