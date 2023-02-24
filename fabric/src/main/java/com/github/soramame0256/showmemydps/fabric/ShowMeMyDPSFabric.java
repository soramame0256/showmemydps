package com.github.soramame0256.showmemydps.fabric;

import com.github.soramame0256.showmemydps.ShowCommand;
import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ShowMeMyDPSFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ShowMeMyDPS.init();
        CommandRegistrationCallback.EVENT.register((a,b,c) -> ShowCommand.register(a));
    }
}
