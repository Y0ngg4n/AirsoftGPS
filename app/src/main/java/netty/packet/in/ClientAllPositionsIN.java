package netty.packet.in;

import com.google.gson.JsonObject;

import netty.packet.PacketIN;

public class ClientAllPositionsIN implements PacketIN {

    private JsonObject jsonObject;

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    @Override
    public void read(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public int getId() {
        return 3;
    }
}