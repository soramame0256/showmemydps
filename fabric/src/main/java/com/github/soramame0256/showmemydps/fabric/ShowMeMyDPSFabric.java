package com.github.soramame0256.showmemydps.fabric;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class ShowMeMyDPSFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        ShowMeMyDPS.init();
        ClientCommandRegistrationCallback.EVENT.register((a,b) -> ShowCommand.register(a));
    }
    @Override
    public void onInitializeClient() {
    }
}
