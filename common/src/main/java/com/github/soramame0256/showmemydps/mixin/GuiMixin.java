package com.github.soramame0256.showmemydps.mixin;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import com.github.soramame0256.showmemydps.util.TitleInjector;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.objectweb.asm.Opcodes.PUTFIELD;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "setTitle", at = @At(value = "HEAD"))
    private void setTitleInject(Component component, CallbackInfo ci){
        TitleInjector.title = component;
    }
    @Inject(method = "setSubtitle", at = @At(value = "HEAD"))
    private void setSubTitleInject(Component component, CallbackInfo ci){
        TitleInjector.subTitle = component;
    }
    @Inject(method = "clearTitles", at = @At(value = "HEAD"))
    private void clearInject(CallbackInfo ci){
        TitleInjector.title = null;
        TitleInjector.subTitle = null;
    }
    @Inject(method = "render", at = @At(value ="TAIL"))
    private void inject(CallbackInfo ci){
        ShowMeMyDPS.getInstance().render();
    }
}
