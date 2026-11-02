package com.yyh.performance;

import com.mojang.logging.LogUtils;
import com.yyh.performance.config.PerformanceConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Mod entry point. Client-only setup lives in {@link com.yyh.performance.client.PerformanceClient};
 * mixins are declared in {@code performance.mixins.json} under the {@code com.yyh.performance.mixin} package.
 */
@Mod(Performance.MOD_ID)
public final class Performance {
    public static final String MOD_ID = "performance";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Performance(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();

        context.registerConfig(ModConfig.Type.CLIENT, PerformanceConfig.CLIENT_SPEC);
        context.registerConfig(ModConfig.Type.COMMON, PerformanceConfig.COMMON_SPEC);

        modBus.addListener(Performance::onCommonSetup);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("{} common setup complete", MOD_ID);
    }
}
