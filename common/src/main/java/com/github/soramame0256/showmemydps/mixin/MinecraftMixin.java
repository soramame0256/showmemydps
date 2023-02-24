package com.github.soramame0256.showmemydps.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.soramame0256.showmemydps.ShowMeMyDPS.featureInstance;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Nullable public ClientLevel level;

    @Inject(method = "runTick", at = @At("HEAD"))
    public void runTickInject(boolean bl, CallbackInfo ci) {
        if(featureInstance != null) featureInstance.tick(level);
    }

}