package com.github.soramame0256.showmemydps;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = ShowMeMyDps.MOD_ID,
        name = ShowMeMyDps.MOD_NAME,
        version = ShowMeMyDps.VERSION
)
public class ShowMeMyDps {

    public static final String MOD_ID = "showmemydps";
    public static final String MOD_NAME = "ShowMeMyDPS";
    public static final String VERSION = "1.5-SNAPSHOT";
    public static EventListener EVENT_LISTENER;
    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static ShowMeMyDps INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {

    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        EVENT_LISTENER = new EventListener();
        MinecraftForge.EVENT_BUS.register(EVENT_LISTENER);
        ClientCommandHandler.instance.registerCommand(new ShowCommand());
    }
}
