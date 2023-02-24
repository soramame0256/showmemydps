package com.github.soramame0256.showmemydps.mixin;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatListener.class)
public class ChatMixin {
    @Inject(method = "handleDisguisedChatMessage", at = @At(value = "HEAD"))
    private void inject(Component component, ChatType.Bound bound, CallbackInfo ci){
        ShowMeMyDPS.featureInstance.onChatReceive(component);
    }
}
