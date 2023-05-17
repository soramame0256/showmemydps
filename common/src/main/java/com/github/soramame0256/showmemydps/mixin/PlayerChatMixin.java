package com.github.soramame0256.showmemydps.mixin;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundPlayerChatPacket.class)
public class PlayerChatMixin {
    @Shadow @Final private @Nullable Component unsignedContent;

    //@Inject(method = "handle(Lnet/minecraft/network/protocol/game/ClientGamePacketListener;)V", at = @At("HEAD"))
    private void inject(ClientGamePacketListener clientGamePacketListener, CallbackInfo ci){
        if(unsignedContent != null) ShowMeMyDPS.featureInstance.onChatReceive(unsignedContent);
    }
}
