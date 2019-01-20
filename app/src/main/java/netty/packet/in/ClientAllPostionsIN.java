package netty.packet.in;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import netty.packet.PacketIN;

public class ClientAllPostionsIN implements PacketIN {

    private JsonArray jsonArray;

    public ClientAllPostionsIN(JsonArray jsonArray){
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