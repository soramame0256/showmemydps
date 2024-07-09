package com.github.soramame0256.showmemydps.fabric.mixin;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Nullable public ClientLevel level;

    @Shadow
    public static Minecraft getInstance() {
        return null;
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    public void runTickInject(boolean bl, CallbackInfo ci) {
        if(getInstance() != null) ShowMeMyDPS.getInstance().tick(level);
    }

}