package netty.packet;

import com.google.gson.JsonObject;

public interface PacketIN {

    void read(final JsonObject jsonObject);

    int getId();
}
