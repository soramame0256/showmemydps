package com.github.soramame0256.showmemydps.fabric.mixin;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Gui.class)
public class GuiMixinFabric {
    @Inject(method = "render", at = @At(value ="TAIL"))
    private void inject(CallbackInfo ci){
        ShowMeMyDPS.featureInstance.render();
    }

}
