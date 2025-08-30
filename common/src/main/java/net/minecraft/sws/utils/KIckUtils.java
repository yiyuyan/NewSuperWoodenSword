package net.minecraft.sws.utils;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

public class KIckUtils {
    public static void disconnect(Connection connection,MinecraftServer server,Component pDisconnectionDetails) {
        /*connection.send(new ClientboundDisconnectPacket(pDisconnectionDetails), PacketSendListener.thenRun(() -> {
            connection.disconnect(pDisconnectionDetails);
        }));
        connection.setReadOnly();
        Objects.requireNonNull(connection);
        server.executeBlocking(connection::handleDisconnection);
        ((ConnectionAccessor) connection).getChannel().close().awaitUninterruptibly();*/
    }

    /*public static void disconnect(Connection connection,MinecraftServer server,Component pReason) {
        disconnect(connection,server,new DisconnectionDetails(pReason));
    }*/
}
