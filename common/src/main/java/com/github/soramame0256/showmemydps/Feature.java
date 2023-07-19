package com.github.soramame0256.showmemydps;

import com.github.soramame0256.showmemydps.util.ChatSender;
import com.github.soramame0256.showmemydps.util.Removal;
import com.github.soramame0256.showmemydps.util.TitleInjector;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Feature {
    private static final Pattern damageReg = Pattern.compile("-(?<damage>\\d+) .");
    public static boolean hud = false;
    private static final String colorReg = "(§[0-9a-fik-or])";
    private final Map<String, Integer> damageList = new HashMap<>();
    private final Map<String, Instant> expire = new HashMap<>();
    public int hudX = 450;
    public int hudY = 460;
    private boolean gregInit = false;
    private Instant start = Instant.now();
    private static final int EXPIRE_WHEN = 5000;
    private static final int DPS_AVERAGE_EXPIRE_WHEN = 1000;
    private long damage = 0;
    private int hitCount = 0;
    private long dpsAvg = 0L;
    public boolean debugMode = false;
    private final List<Removal<Integer>> dpsAvgDamageList = new ArrayList<>();
    private Instant tickHandle = Instant.now();
    private static final Minecraft mc = Minecraft.getInstance();
    //Sometimes(or Always?) received messages are used as doubled. so it fixes them.
    private final List<Removal<String>> timeoutStrings = new ArrayList<>();
    public void tick(ClientLevel level){
        if(tickHandle.toEpochMilli()+50 <= Instant.now().toEpochMilli()) {
            tickHandle = Instant.now();
            checkExpires();
            if(damage != 0 && (isBossStartTitle(getCurrentTitle().replaceAll(colorReg,""))||isBossStartTitle(getCurrentSubTitle().replaceAll(colorReg,"")))) {
                reset();
            }
            if (level != null) {
                LocalPlayer pl = mc.player;
                if(pl ==null) return;
                if(gregInit) {
                    for (Entity en : level.getEntitiesOfClass(ArmorStand.class,new AABB(pl.getX()-200, pl.getY()-200,pl.getZ()-200,pl.getX()+200, pl.getY()+200,pl.getZ()+200), (ent) -> true)) {//ent.getDisplayName().getString().equals("§c§9§lThe §1§k12345§9§l Anomaly§r")
                        reset();
                        System.out.println(en.getDisplayName().getString());
                    };
                }
                for(Entity en : level.getEntitiesOfClass(ArmorStand.class,new AABB(pl.getX()-200, pl.getY()-200,pl.getZ()-200,pl.getX()+200, pl.getY()+200,pl.getZ()+200), (ent) -> ent.getDisplayName().getString().contains("-"))) {
                    String name = en.getDisplayName().getString();
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
                    if (c != 0) addDamage(en.getStringUUID(), c);
                }
            }
        }

    }
    public void onChatReceive(Component msg) {
        String s = msg.getString().replaceAll(colorReg, "");
        if(isTimeoutString(s)) return;
        timeoutStrings.add(new Removal<>(s, 50));
        //the nameless anomaly boss prepare room lore.
        if (s.startsWith("And what is responsible? What remains here should not- trapped far from whence it came. What feeds the paradox")) {
            reset();
            gregInit=true;
        } else if(s.equals("[1/1] The Mummyboard: OS Version M-37 is now online. Systems estimate a 96.286573628% chance of success.")) {
            reset();
        }else if(s.equals("[WAR] The battle has begun!")){
            reset();
        } else if(s.startsWith("You finished the Legendary Challenge")){
            this.showMsg(mc.player);
        } else if(s.startsWith("[!] The Raid Reward chest has been unlocked.")){
            this.showMsg(mc.player);
        } else if(s.equals("                        Territory Captured")){
            Thread t = new Thread(()->{
                try {
                    Thread.sleep(12);
                    this.showMsg(mc.player);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
            t.start();
        }
    }
    public void showMsg(LocalPlayer p){
        long totalDamage = this.getTotalDamage();
        int hitCount = this.getHitCount();
        if(hitCount == 0) hitCount++;
        Instant instant = this.getStartWhen();
        long in = Instant.now().getEpochSecond() - instant.getEpochSecond();
        ChatSender.sendMessage(p,"§bDPS: " + totalDamage / in);
        ChatSender.sendMessage(p,"§b→Time: " + in + " seconds");
        ChatSender.sendMessage(p,"§bAvg/hit: " + totalDamage/hitCount);
        ChatSender.sendMessage(p,"§bTotal: " + totalDamage);
    }
    public void render() {
        if(hud) {
            if(debugMode){
                try {
                    List<String> txt = new ArrayList<>();
                    for (Field field : this.getClass().getDeclaredFields()) {
                        boolean isAccessible = field.isAccessible();
                        field.setAccessible(true);
                        if (!Modifier.isFinal(field.getModifiers())) txt.add(field.getName() + ": " + field.get(this));
                        field.setAccessible(isAccessible);
                    }
                    int times = 0;
                    for (String tx : txt) {
                        mc.font.draw(new PoseStack(), tx, hudX, hudY + mc.font.lineHeight * times++, 0xffffff);
                    }
                }catch(IllegalAccessException e){
                    e.printStackTrace();
                }
            }else{
                mc.font.draw(new PoseStack(),"RealDPS: " + dpsAvg, hudX, hudY,0xffffff);
            }
        }
    }
    private boolean isBossStartTitle(String a){
        return a.equals("The defense system") || a.equals("The Light Beast") || a.equals("The Grootslang Wyrmlings");
    }
    private boolean isTimeoutString(String s){
        for(Removal<String> a: timeoutStrings){
            if(s.equals(a.b)) return true;
        }
        return false;
    }
    private void addDamage(String uuid, Integer da){
        int g = damageList.getOrDefault(uuid, 0);
        if(g < da){
            dpsAvgDamageList.add(new Removal<>(da-g,DPS_AVERAGE_EXPIRE_WHEN));
        }
        damageList.put(uuid, Math.max(g,da));
        expire.put(uuid, Instant.now());
    }
    private static String getCurrentTitle() {
        String a = null;
        if (TitleInjector.title != null) {
            a = TitleInjector.title.getString();
        }
        return a==null? "": a;
    }
    private static String getCurrentSubTitle(){
        String a = null;
        if (TitleInjector.subTitle != null) {
            a = TitleInjector.subTitle.getString();
        }
        return a==null? "": a;

    }
    private void checkExpires(){
        if(expire.size() == 0) return;
        long i = Instant.now().toEpochMilli();
        List<String> toRemove = new ArrayList<>();
        dpsAvgDamageList.removeAll(dpsAvgDamageList.stream().filter(Removal::checkExpire).toList());
        timeoutStrings.removeAll(timeoutStrings.stream().filter(Removal::checkExpire).toList());
        dpsAvg= 0;
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
        damage = 0;
        hitCount = 0;
        start = Instant.now();
        gregInit=false;
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
