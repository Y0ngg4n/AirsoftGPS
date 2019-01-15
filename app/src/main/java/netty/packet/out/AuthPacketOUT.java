package netty.packet.out;

import com.google.gson.JsonObject;

import netty.packet.PacketOUT;

public class AuthPacketOUT implements PacketOUT {

    private String username, password;

    public AuthPacketOUT(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void write(final JsonObject jsonObject) {
        jsonObject.addProperty("username", this.username);
        jsonObject.addProperty("password", this.password);
    }

    @Override
    public int getId() {
        return 1;
    }
}
