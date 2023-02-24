package com.github.soramame0256.showmemydps.mixin;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundSystemChatPacket.class)
public class SystemChatMixin {
    @Shadow @Final private Component content;

    @Shadow @Final private boolean overlay;

    @Inject(method = "handle(Lnet/minecraft/network/protocol/game/ClientGamePacketListener;)V", at = @At(value = "HEAD"))
    private void inject(ClientGamePacketListener clientGamePacketListener, CallbackInfo ci){
        if(!overlay) ShowMeMyDPS.featureInstance.onChatReceive(content);
    }
}
