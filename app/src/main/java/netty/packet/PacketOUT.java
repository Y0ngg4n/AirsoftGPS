package netty.packet;

import com.google.gson.JsonObject;

public interface PacketOUT {

    void write(final JsonObject jsonObject);

    int getId();
}