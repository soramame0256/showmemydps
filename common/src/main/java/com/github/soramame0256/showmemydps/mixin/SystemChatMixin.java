package com.github.soramame0256.showmemydps.mixin;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class SystemChatMixin {

    @Inject(method = "handleSystemChat", at = @At(value = "INVOKE",target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V",shift = At.Shift.AFTER))
    private void inject(ClientboundSystemChatPacket clientboundSystemChatPacket, CallbackInfo ci){
        if(!clientboundSystemChatPacket.overlay()) ShowMeMyDPS.featureInstance.onChatReceive(clientboundSystemChatPacket.content());
    }
}
