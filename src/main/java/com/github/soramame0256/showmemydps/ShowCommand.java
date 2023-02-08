package com.github.soramame0256.showmemydps;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.time.Instant;

public class ShowCommand extends CommandBase {
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
}
