package com.github.soramame0256.neoforge;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ShowMeMyDPS.MOD_ID)
public class ShowMeMyDPSNeoForge {
    public ShowMeMyDPSNeoForge() {
        // Submit our event bus to let architectury register our content on the right time
        NeoForge.EVENT_BUS.register(this);
        ShowMeMyDPS.init();
    }
    @SubscribeEvent
    public void commandRegistration(RegisterClientCommandsEvent e){
        ShowCommand.register(e.getDispatcher());
    }
    @SubscribeEvent
    public void onRender(RenderGuiLayerEvent.Post e){
        ShowMeMyDPS.featureInstance.render();
    }
}
