package com.github.soramame0256.showmemydps.mixin;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class RendererMixin {
    @Inject(method = "render", at = @At(value ="TAIL"))
    private void inject(CallbackInfo ci){
        ShowMeMyDPS.featureInstance.render();
    }
}
