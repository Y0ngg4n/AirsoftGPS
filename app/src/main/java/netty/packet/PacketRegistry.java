package netty.packet;

import netty.packet.in.ClientAllPostionsIN;
import netty.packet.in.LoginResponsePacketIN;

public enum PacketRegistry {

    LoginResponseIN(LoginResponsePacketIN.class),
    ClientAllPostionsIN(ClientAllPostionsIN.class);

    private Class<? extends PacketIN> clazz;

    PacketRegistry(Class<? extends PacketIN> clazz) {
        this.clazz = clazz;
    }

    public static Class<? extends PacketIN> getPacket(final int id) {
        for (final PacketRegistry value : values()) {
            try {
                if (value.getClazz().newInstance().getId() == id) {
                    return value.getClazz();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Class<? extends PacketIN> getClazz() {
        return clazz;
    }
}
