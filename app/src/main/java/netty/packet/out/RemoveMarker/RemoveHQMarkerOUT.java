package netty.packet.out.RemoveMarker;

import com.google.gson.JsonObject;

import netty.packet.PacketOUT;

public class RemoveHQMarkerOUT implements PacketOUT {

    private int markerID;
    private String username;

    public RemoveHQMarkerOUT(int markerID, String username) {
        this.markerID = markerID;
        this.username = username;
    }

    @Override
    public void write(JsonObject jsonObject) {
        jsonObject.addProperty("markerID", markerID);
        jsonObject.addProperty("username", username);
    }

    @Override
    public int getId() {
        return 15;
    }


}
