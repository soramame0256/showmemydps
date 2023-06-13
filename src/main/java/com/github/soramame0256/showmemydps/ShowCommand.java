package com.github.soramame0256.showmemydps;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.time.Instant;

import static com.github.soramame0256.showmemydps.EventListener.debugMode;

public class ShowCommand extends CommandBase {
    public static int x = 450;
    public static int y = 460;
    public static boolean hud = false;
    @Override
    public String getName() {
        return "showdps";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/showdps";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("reset")){
                showMsg(sender);
                ShowMeMyDps.EVENT_LISTENER.reset();
            }else if(args[0].equalsIgnoreCase("debug")){
                sender.sendMessage(new TextComponentString("debugMode: " + (debugMode = !debugMode)));
            }
            if(args.length >= 2 && args[0].equalsIgnoreCase("hud")){
                if(args.length == 2 && args[1].equalsIgnoreCase("toggle")){
                    sender.sendMessage(new TextComponentString("HUD " + ((hud = !hud) ? "Enabled!" : "Disabled!")));
                } else if(args.length == 3) {
                    x = Integer.parseInt(args[1]);
                    y = Integer.parseInt(args[2]);
                    sender.sendMessage(new TextComponentString("HUD Location Changed!"));
                }
            }
        }else{
            showMsg(sender);
        }
    }
    public static void showMsg(ICommandSender sender){
        long totalDamage = ShowMeMyDps.EVENT_LISTENER.getTotalDamage();
        int hitCount = ShowMeMyDps.EVENT_LISTENER.getHitCount();
        if(hitCount == 0) hitCount++;
        Instant instant = ShowMeMyDps.EVENT_LISTENER.getStartWhen();
        long in = Instant.now().getEpochSecond() - instant.getEpochSecond();
        sender.sendMessage(new TextComponentString("§bDPS: " + totalDamage/in));
        sender.sendMessage(new TextComponentString("§b→Time: " + in + " seconds"));
        sender.sendMessage(new TextComponentString("§bAvg/hit: " + totalDamage/hitCount));
        sender.sendMessage(new TextComponentString("§bTotal: " + totalDamage));
    }
    public static void showMsg(ICommandSender sender,long invincibleTime){
        long totalDamage = ShowMeMyDps.EVENT_LISTENER.getTotalDamage();
        int hitCount = ShowMeMyDps.EVENT_LISTENER.getHitCount();
        if(hitCount == 0) hitCount++;
        Instant instant = ShowMeMyDps.EVENT_LISTENER.getStartWhen();
        long in = Instant.now().getEpochSecond() - instant.getEpochSecond();
        sender.sendMessage(new TextComponentString("§bDPS: " + totalDamage/(in-invincibleTime/20L)));
        sender.sendMessage(new TextComponentString("§b→Time: " + (in-invincibleTime/20L) + " seconds"));
        sender.sendMessage(new TextComponentString("§b→Actual: " + in + " seconds"));
        sender.sendMessage(new TextComponentString("§bAvg/hit: " + totalDamage/hitCount));
        sender.sendMessage(new TextComponentString("§bTotal: " + totalDamage));
    }
}
