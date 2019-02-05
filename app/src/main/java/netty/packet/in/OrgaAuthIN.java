package netty.packet.in;

import com.google.gson.JsonObject;

import netty.packet.PacketIN;

public class OrgaAuthIN implements PacketIN {

    private boolean successful, tacticalMarker, missionMarker, hqMarker, respawnMarker, flagMarker;

    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public void read(JsonObject jsonObject) {
        successful = jsonObject.get("successful").getAsBoolean();
        tacticalMarker = jsonObject.get("tacticalMarker").getAsBoolean();
        missionMarker = jsonObject.get("missionMarker").getAsBoolean();
        hqMarker = jsonObject.get("hqMarker").getAsBoolean();
        respawnMarker= jsonObject.get("respawnMarker").getAsBoolean();
        flagMarker= jsonObject.get("flagMarker").getAsBoolean();
    }

    @Override
    public int getId() {
        return 6;
    }

    public boolean isFlagMarker() {
        return flagMarker;
    }

    public boolean isHqMarker() {
        return hqMarker;
    }

    public boolean isMissionMarker() {
        return missionMarker;
    }

    public boolean isRespawnMarker() {
        return respawnMarker;
    }

    public boolean isTacticalMarker() {
        return tacticalMarker;
    }
}
