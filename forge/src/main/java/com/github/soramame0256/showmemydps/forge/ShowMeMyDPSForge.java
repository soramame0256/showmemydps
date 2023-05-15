package com.github.soramame0256.showmemydps.forge;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(ShowMeMyDPS.MOD_ID)
public class ShowMeMyDPSForge {
    public ShowMeMyDPSForge() {
        // Submit our event bus to let architectury register our content on the right time
        MinecraftForge.EVENT_BUS.register(this);
        ShowMeMyDPS.init();
    }
    @SubscribeEvent
    public void commandRegistration(RegisterClientCommandsEvent e){
        ShowCommand.register(e.getDispatcher());
    }
    @SubscribeEvent
    public void onRender(RenderGuiOverlayEvent.Post e){
        ShowMeMyDPS.featureInstance.render();
    }
}
