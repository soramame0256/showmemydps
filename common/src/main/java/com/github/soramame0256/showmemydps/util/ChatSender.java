package com.github.soramame0256.showmemydps.util;

import com.mojang.brigadier.LiteralMessage;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;

public class ChatSender {
    public static Component getComponentFromString(String s) {
        return ComponentUtils.fromMessage(new LiteralMessage(s));
    }
    public static void sendMessage(LocalPlayer p, Component c){
        p.displayClientMessage(c,false);
    }
    public static void sendMessage(LocalPlayer p, String s){
        p.displayClientMessage(getComponentFromString(s),false);
    }
}
