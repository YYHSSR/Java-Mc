package com.yyh.performance.client;

import com.yyh.performance.Performance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-only setup. This class is loaded only on the physical client; common code must never reference it.
 */
@Mod.EventBusSubscriber(modid = Performance.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PerformanceClient {
    private PerformanceClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        Performance.LOGGER.info("{} client setup complete", Performance.MOD_ID);
    }
}
