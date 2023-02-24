package com.github.soramame0256.showmemydps.quilt;

import com.github.soramame0256.showmemydps.ShowMeMyDPS;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class ShowMeMyDPSQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        ShowMeMyDPS.init();

    }
}
