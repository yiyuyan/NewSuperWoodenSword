package net.minecraft.sws.mixin.accessors;

import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(priority = 2147483647,value = ServerGamePacketListenerImpl.class)
public interface ServerCommonPacketListenerImplAccessor {
    @Accessor("connection")
    Connection getConnection();
}
