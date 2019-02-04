package netty.packet.in;

import com.google.gson.JsonObject;

import netty.packet.PacketIN;

public class OrgaAuthIN implements PacketIN {

    private boolean successfull;

    public boolean isSuccessfull() {
        return successfull;
    }

    @Override
    public void read(JsonObject jsonObject) {
        successfull = jsonObject.get("successfull").getAsBoolean();
    }

    @Override
    public int getId() {
        return 6;
    }
}
