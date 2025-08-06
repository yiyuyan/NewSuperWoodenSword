package cn.ksmcbrigade.sws.utils;

import cn.ksmcbrigade.sws.mixin.accessors.ConnectionAccessor;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;

public class KIckUtilsZ {
    public static void disconnect(Connection connection,MinecraftServer server,Component pDisconnectionDetails) {
        connection.send(new ClientboundDisconnectPacket(pDisconnectionDetails), PacketSendListener.thenRun(() -> {
            connection.disconnect(pDisconnectionDetails);
        }));
        connection.setReadOnly();
        Objects.requireNonNull(connection);
        server.executeBlocking(connection::handleDisconnection);
        ((ConnectionAccessor) connection).getChannel().close().awaitUninterruptibly();
    }

    /*public static void disconnect(Connection connection,MinecraftServer server,Component pReason) {
        disconnect(connection,server,new DisconnectionDetails(pReason));
    }*/
}
