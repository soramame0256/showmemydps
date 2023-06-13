package com.github.soramame0256.showmemydps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.soramame0256.showmemydps.ShowCommand.hud;

public class EventListener {
    private static final Pattern damageReg = Pattern.compile("-(?<damage>\\d+) .");
    private static final String colorReg = "(§[0-9a-fik-or])";
    private final Map<String, Integer> damageList = new HashMap<>();
    private final Map<String, Instant> expire = new HashMap<>();
    private Instant start = Instant.now();
    private static final int EXPIRE_WHEN = 5000;
    private static final int DPS_AVERAGE_EXPIRE_WHEN = 1000;
    private long damage = 0L;
    private int hitCount = 0;
    private long dpsAvg = 0L;
    private final List<Removal<Integer>> dpsAvgDamageList = new ArrayList<>();
    private Instant tickHandle = Instant.now();
    private boolean isInGreg = false;
    private boolean gregInit = false;
    private long invincibleTime = 0L; //it must be used only in tna (tick)
    public static boolean debugMode = false;
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent e) throws IllegalAccessException {
        if(hud && e.getType() == RenderGameOverlayEvent.ElementType.ALL) render();
    }
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e){
        if(Minecraft.getMinecraft().world != null&& gregInit){
            for(Entity en : Minecraft.getMinecraft().world.getEntities(EntityArmorStand.class, (ent) -> ent.getDisplayName().getFormattedText().equals("§c§9§lThe §1§k12345§9§l Anomaly§r"))) {
                reset();
                isInGreg = true;
            }
            if(!gregInit) return;
        }
        if(tickHandle.toEpochMilli()+50 > Instant.now().toEpochMilli()) return;
        tickHandle = Instant.now();
        checkExpires();
        if(damage != 0 && (isBossStartTitle(getCurrentTitle().replaceAll(colorReg,""))||isBossStartTitle(getCurrentSubTitle().replaceAll(colorReg,"")))) {
            reset();
        }
        if(Minecraft.getMinecraft().world == null) return;
        for(Entity en : Minecraft.getMinecraft().world.getEntities(EntityArmorStand.class, (ent) -> (ent.getDisplayName().getUnformattedText().contains("-")||ent.getDisplayName().getFormattedText().equals("§e§4Sightseeing Interceptor§6 [Lv. 30]§r")))) {
            if(isInGreg && en.getDisplayName().getFormattedText().equals("§e§4Sightseeing Interceptor§6 [Lv. 30]§r")){
                invincibleTime++;
                continue;
            }
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
    private void render() throws IllegalAccessException {
        if(debugMode) {
            List<String> txt = new ArrayList<>();
            for (Field field : this.getClass().getDeclaredFields()) {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                if(!Modifier.isFinal(field.getModifiers())) txt.add(field.getName()+": "+field.get(this));
                field.setAccessible(isAccessible);
            }
            int times = 0;
            for(String tx : txt){
                Minecraft.getMinecraft().fontRenderer.drawString(tx, ShowCommand.x, ShowCommand.y+Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT*times++,0xffffff);
            }
        }else Minecraft.getMinecraft().fontRenderer.drawString("RealDPS: " + dpsAvg, ShowCommand.x, ShowCommand.y,0xffffff);
    }
    private boolean isBossStartTitle(String a){
        return a.equals("The defense system") || a.equals("The Light Beast") || a.equals("The Grootslang Wyrmlings");
    }
    private void addDamage(String uuid, Integer da){
        int g = damageList.getOrDefault(uuid, 0);
        if(g < da){
            dpsAvgDamageList.add(new Removal<>(da-g,DPS_AVERAGE_EXPIRE_WHEN));
        }
        damageList.put(uuid, Math.max(g,da));
        expire.put(uuid, Instant.now());
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatReceive(ClientChatReceivedEvent e) {
        String s = e.getMessage().getUnformattedText().replaceAll(colorReg, "");
        //the nameless anomaly boss prepare room lore.
        if (s.startsWith("And what is responsible? What remains here should not- trapped far from whence it came. What feeds the paradox")) {
            reset();
            gregInit = true;
        } else if(s.equals("[1/1] The Mummyboard: OS Version M-37 is now online. Systems estimate a 96.286573628% chance of success.")) {
            reset();
        }else if(s.equals("[WAR] The battle has begun!")){
            reset();
        } else if(s.startsWith("You finished the Legendary Challenge")){
            ShowCommand.showMsg(Minecraft.getMinecraft().player);
        } else if(s.contains("[!] The Raid Reward chest has been unlocked.")){
            if(isInGreg) {
                ShowCommand.showMsg(Minecraft.getMinecraft().player, invincibleTime);
                isInGreg=false;
                invincibleTime=0L;
            }
            else ShowCommand.showMsg(Minecraft.getMinecraft().player);
        } else if(s.equals("                        Territory Captured")){
            Thread t = new Thread(()->{
                try {
                    Thread.sleep(12);
                    ShowCommand.showMsg(Minecraft.getMinecraft().player);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
            t.start();
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
    private static String getCurrentSubTitle(){
        String a;
        if (Loader.instance().getMCVersionString().equals("Minecraft 1.12.2")){
            a = ObfuscationReflectionHelper.getPrivateValue(GuiIngame.class, Minecraft.getMinecraft().ingameGUI, "field_175200_y");
        }else{
            a = ObfuscationReflectionHelper.getPrivateValue(GuiIngame.class, Minecraft.getMinecraft().ingameGUI, "field_175201_x","");
        }
        return a==null? "": a;

    }
    private void checkExpires(){
        if(expire.size() == 0) return;
        long i = Instant.now().toEpochMilli();
        List<String> toRemove = new ArrayList<>();
        dpsAvgDamageList.removeAll(dpsAvgDamageList.stream().filter(Removal::checkExpire).collect(Collectors.toList()));
        dpsAvg=0L;
        dpsAvgDamageList.forEach(a -> dpsAvg += a.b);
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
        damage = 0L;
        hitCount = 0;
        start = Instant.now();
        isInGreg = false;
        gregInit = false;
        invincibleTime = 0L;
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
    private static class Removal<T> {
        public Instant a = Instant.now();
        public T b;
        private final int expire;
        public Removal(T b, int expire){
            this.b = b;
            this.expire = expire;
        }
        public boolean checkExpire(){
            return a.toEpochMilli()+expire<=Instant.now().toEpochMilli();
        }
    }
}
