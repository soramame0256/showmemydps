package com.github.soramame0256.showmemydps.quilt;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class ShowMeMyDPSQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        ShowMeMyDPS.init();
        CommandDispatcher<CommandSourceStack> s = new CommandDispatcher<>();
        ShowCommand.register(s);

    }
}
