package cn.ksmcbrigade.sws;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.sws.handlers.ClientEventsHandler;

public class SWSFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> ClientEventsHandler.clientTick());
    }
}
