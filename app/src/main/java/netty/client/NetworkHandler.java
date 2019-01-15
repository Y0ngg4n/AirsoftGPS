package netty.client;

import android.util.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.packet.PacketIN;
import netty.packet.in.LoginResponsePacketIN;

public class NetworkHandler extends SimpleChannelInboundHandler<PacketIN> {

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final PacketIN packet) {
        if(packet instanceof LoginResponsePacketIN) {
            final LoginResponsePacketIN responsePacket = ((LoginResponsePacketIN) packet);
            if (responsePacket.isSuccess()) {
                Log.i("NettyLoginSuccessfull", "Login into Database successfull");
            } else {
                Log.i("NettyLoginError", "Login into Database not successfull");
            }
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        //    controller.logoutScene();
        //    cause.printStackTrace();
    }
}
