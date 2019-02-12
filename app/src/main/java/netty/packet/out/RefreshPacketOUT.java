package netty.packet.out;

import com.google.gson.JsonObject;

import netty.packet.PacketOUT;

public class RefreshPacketOUT implements PacketOUT {

    public RefreshPacketOUT() {
        //Has to have empty Constructor
    }

    @Override
    public void write(JsonObject jsonObject) {
    }

    @Override
    public int getId() {
        return 50;
    }
}
