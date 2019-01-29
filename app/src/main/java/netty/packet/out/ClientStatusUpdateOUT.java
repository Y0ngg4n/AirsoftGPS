package netty.packet.out;

import com.google.gson.JsonObject;

import netty.packet.PacketOUT;

public class ClientStatusUpdateOUT implements PacketOUT {

    private String username;
    private boolean alive, underfire, mission, support;

    public ClientStatusUpdateOUT(String username, boolean alive, boolean underfire, boolean mission, boolean support ) {
        this.username = username;
        this.alive = alive;
        this.underfire = underfire;
        this.mission = mission;
        this.support = support;
    }

    @Override
    public void write(JsonObject jsonObject) {
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("alive", alive);
        jsonObject.addProperty("underfire", underfire);
        jsonObject.addProperty("mission", mission);
        jsonObject.addProperty("support", support);
    }

    @Override
    public int getId() {
        return 3;
    }
}
