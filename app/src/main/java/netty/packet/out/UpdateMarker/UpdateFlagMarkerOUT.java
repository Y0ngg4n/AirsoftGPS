package netty.packet.out.UpdateMarker;

import com.google.gson.JsonObject;

import netty.packet.PacketOUT;

public class UpdateFlagMarkerOUT implements PacketOUT {

    private int flagID;
    private boolean isOwned;

    public UpdateFlagMarkerOUT(int flagID, boolean isOwned) {
        this.flagID = flagID;
        this.isOwned = isOwned;
    }

    @Override
    public void write(JsonObject jsonObject) {
        jsonObject.addProperty("flagID", flagID);
        jsonObject.addProperty("own", isOwned);
    }

    @Override
    public int getId() {
        return 17;
    }
}
