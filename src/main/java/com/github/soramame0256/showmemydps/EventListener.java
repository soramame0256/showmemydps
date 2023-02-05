package com.github.soramame0256.showmemydps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventListener {
    private static final Pattern damageReg = Pattern.compile("-(?<damage>\\d+) .");
    private static final String colorReg = "(§[0-9a-fik-or])";
    private final Map<String, Integer> damageList = new HashMap<>();
    private final Map<String, Instant> expire = new HashMap<>();
    private Instant start = Instant.now();
    private static final int EXPIRE_WHEN = 5000;
    private long damage = 0;
    private int hitCount = 0;
    @SubscribeEvent
    public void onEntityRender(RenderLivingEvent.Pre<EntityArmorStand> e){

    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e){
        checkExpires();
        if(getCurrentTitle().replaceAll(colorReg,"").equals("The defense system") && getTotalDamage() != 0) reset();
        if(Minecraft.getMinecraft().world == null) return;
        for(Entity en : Minecraft.getMinecraft().world.getEntities(EntityArmorStand.class, (ent) -> ent.getDisplayName().getUnformattedText().contains("-"))) {
            String name = en.getDisplayName().getUnformattedText();
            name = name.replaceAll(colorReg, "");
            int c = 0;
            Matcher m = damageReg.matcher(name);
            if (m.find()) {
                for (String s : name.split(" ")) {
                    if (s.startsWith("-")) {
                        try {
                            c += Integer.parseInt(s.replace("-", ""));
                        } catch (NumberFormatException ex) {
                            break;
                        }
                    }
                }
            }
            if (c != 0) addDamage(en.getUniqueID().toString(), c);
        }
    }
    private void addDamage(String uuid, Integer da){
        damageList.put(uuid, Math.max(damageList.getOrDefault(uuid, 0),da));
        expire.put(uuid, Instant.now());
    }
    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent e){
        String s = e.getMessage().getUnformattedText().replaceAll(colorReg,"");
        //the nameless anomaly boss prepare room lore.
        if(s.contains("And what is responsible? What remains here should not- trapped far from whence it came. What feeds the paradox")){
            reset();
        }
        if(s.contains("[!] The Raid Reward chest has been unlocked.")){
            ShowCommand.showMsg(Minecraft.getMinecraft().player);
        }


    }
    private static String getCurrentTitle(){
        String a;
        if (Loader.instance().getMCVersionString().equals("Minecraft 1.12.2")){
            a = ObfuscationReflectionHelper.getPrivateValue(GuiIngame.class, Minecraft.getMinecraft().ingameGUI, "field_175201_x");
        }else {
            a= ObfuscationReflectionHelper.getPrivateValue(GuiIngame.class, Minecraft.getMinecraft().ingameGUI, "field_175201_x", "");
        }
        return a==null? "": a;
    }
    private void checkExpires(){
        if(expire.size() == 0) return;
        long i = Instant.now().toEpochMilli();
        List<String> toRemove = new ArrayList<>();
        for(Map.Entry<String, Instant> ep : expire.entrySet()){
            if(ep.getValue().toEpochMilli() + EXPIRE_WHEN <= i){
                damage += damageList.get(ep.getKey());
                hitCount++;
                toRemove.add(ep.getKey());
            }
        }
        for(String r : toRemove){
            expire.remove(r);
            damageList.remove(r);
        }
    }
    private void makeAllExpire(){
        if(expire.size() == 0) return;
        List<String> toRemove = new ArrayList<>(damageList.keySet());
        for(String r : toRemove){
            damage += damageList.get(r);
            hitCount++;
            expire.remove(r);
            damageList.remove(r);
        }
    }
    public void reset(){
        expire.clear();
        damageList.clear();
        damage = 0;
        hitCount = 0;
        start = Instant.now();
    }
    public long getTotalDamage(){
        makeAllExpire();
        return damage;

    }
    public Instant getStartWhen(){
        return start;
    }
    public int getHitCount(){
        makeAllExpire();
        return hitCount;
    }
}
