package netty.packet.in;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import netty.packet.PacketIN;

public class ClientAllPositionsIN implements PacketIN {

    private JsonArray jsonArray;

    public JsonArray getJsonArray() {
        return jsonArray;
    }

    public ClientAllPositionsIN(){}

    public ClientAllPositionsIN(final JsonArray jsonArray){
        this.jsonArray = jsonArray;
    }

    @Override
    public void read(JsonObject jsonObject) {
        jsonObject.get("array").getAsJsonArray();
    }

    @Override
    public int getId() {
        return 3;
    }
}