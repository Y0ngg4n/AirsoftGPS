package netty.packet.in;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import netty.packet.PacketIN;

public class AddTacticalMarkerIN implements PacketIN {

    private JsonArray jsonArray;

    @Override
    public void read(JsonObject jsonObject) {
        jsonArray = jsonObject.get("tacticalMarkers").getAsJsonArray();
    }

    @Override
    public int getId() {
        return 7;
    }

    public JsonArray getJsonArray() {
        return jsonArray;
    }
}
