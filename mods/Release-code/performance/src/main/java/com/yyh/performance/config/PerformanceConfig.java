package com.yyh.performance.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Forge config specs. CLIENT holds local rendering/input options; COMMON holds options
 * loaded on both sides. Read values only after the config has loaded, never in static initializers.
 */
public final class PerformanceConfig {
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = client.getLeft();
        CLIENT_SPEC = client.getRight();

        Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = common.getLeft();
        COMMON_SPEC = common.getRight();
    }

    private PerformanceConfig() {
    }

    public static final class Client {
        public final ForgeConfigSpec.BooleanValue enableClientOptimizations;

        private Client(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            enableClientOptimizations = builder
                .comment("Master switch for client-side optimizations.")
                .define("enableClientOptimizations", true);
            builder.pop();
        }
    }

    public static final class Common {
        public final ForgeConfigSpec.BooleanValue enableCommonOptimizations;

        private Common(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            enableCommonOptimizations = builder
                .comment("Master switch for optimizations that run on both sides.")
                .define("enableCommonOptimizations", true);
            builder.pop();
        }
    }
}
