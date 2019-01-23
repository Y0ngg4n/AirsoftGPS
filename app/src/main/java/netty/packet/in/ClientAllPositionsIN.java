package netty.packet.in;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import netty.packet.PacketIN;

public class ClientAllPositionsIN implements PacketIN {

    private JsonArray jsonArray;

    public JsonArray getJsonArray() {
        return jsonArray;
    }

    @Override
    public void read(JsonObject jsonObject) {
        this.jsonArray = jsonObject.get("positions").getAsJsonArray();
    }

    @Override
    public int getId() {
        return 3;
    }
}