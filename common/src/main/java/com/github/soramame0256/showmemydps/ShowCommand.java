package com.github.soramame0256.showmemydps;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;

import static com.github.soramame0256.showmemydps.util.ChatSender.sendMessage;
import static net.minecraft.commands.Commands.literal;

public class ShowCommand {
    public static final Minecraft mc = Minecraft.getInstance();
    public static final Feature fi = ShowMeMyDPS.featureInstance;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> e = literal("showdps")
                .executes(context -> {
                    fi.showMsg(mc.player);
                    return 1;
                });
        e.then(literal("reset")
                .executes(context -> {
                    fi.showMsg(mc.player);
                    fi.reset();
                    return 1;
                })).build();
        e.then(literal("hud")
                .then(literal("toggle")
                .executes(context -> {
                    if (mc.player == null) return 0;
                    Feature.hud = !Feature.hud;
                    sendMessage(mc.player, "HUD " + ((Feature.hud) ? "Enabled!" : "Disabled!"));
                    return 1;
                }).build()));
        e.then(literal("hud").then(RequiredArgumentBuilder.<CommandSourceStack, Integer>argument("x", IntegerArgumentType.integer(0, mc.getWindow().getScreenWidth()))
                .then(RequiredArgumentBuilder.<CommandSourceStack, Integer>argument("y", IntegerArgumentType.integer(0, mc.getWindow().getScreenHeight()))
                        .executes(context -> {
                            if (mc.player == null) return 0;
                            fi.hudX = context.getArgument("x", Integer.class);
                            fi.hudY = context.getArgument("y", Integer.class);
                            sendMessage(mc.player, "HUD Location Changed!");
                            return 1;
                        })
                ).build()
        ));
        dispatcher.register(e);
    }
}
