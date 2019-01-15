package netty.packet.in;

import com.google.gson.JsonObject;

import netty.packet.PacketIN;

public class LoginResponsePacketIN implements PacketIN {

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    @Override
    public void read(final JsonObject jsonObject) {
        this.success = jsonObject.get("success").getAsBoolean();
    }

    @Override
    public int getId() {
        //TODO ID`s musst du selber planen
        return 1;
    }
}
