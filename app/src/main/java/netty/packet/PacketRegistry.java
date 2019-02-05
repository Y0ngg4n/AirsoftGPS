package netty.packet;
import android.util.Log;

import netty.packet.in.AddFlagMarkerIN;
import netty.packet.in.AddHQMarkerIN;
import netty.packet.in.AddMissionMarkerIN;
import netty.packet.in.AddRespawnMarkerIN;
import netty.packet.in.AddTacticalMarkerIN;
import netty.packet.in.ClientAllPositionsIN;
import netty.packet.in.LoginResponsePacketIN;
import netty.packet.in.OrgaAuthIN;

public enum PacketRegistry {
    LoginResponsePacketIN(LoginResponsePacketIN.class),
    ClientAllPositionsIN(ClientAllPositionsIN.class),
    OrgaAuthIN(OrgaAuthIN.class),
    AddFlagMarkerIN(AddFlagMarkerIN.class),
    AddHQMarkerIN(AddHQMarkerIN.class),
    AddMissionMarkerIN(AddMissionMarkerIN.class),
    AddRespawnMarkerIN(AddRespawnMarkerIN.class),
    AddTacticalMarkerIN(AddTacticalMarkerIN.class);

    private Class<? extends PacketIN> clazz;

    PacketRegistry(Class<? extends PacketIN> clazz) {
        this.clazz = clazz;
    }

    public static Class<? extends PacketIN> getPacket(int id) {
        for (PacketRegistry value : values()) {
            try {
                if (value.getClazz().newInstance().getId() == id) {
                    return value.getClazz();
                }
            } catch (Exception e) {
                Log.i("PacketRegistry", e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    public Class<? extends PacketIN> getClazz() {
        return clazz;
    }
}
