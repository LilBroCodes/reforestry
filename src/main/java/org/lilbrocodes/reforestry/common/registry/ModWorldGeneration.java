package org.lilbrocodes.reforestry.common.registry;

import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.registry.RegistryKeys;

public class ModWorldGeneration {

    public static void initialize() {
        DynamicRegistrySetupCallback.EVENT.register((registries) -> {
            registries.getOptional(RegistryKeys.CONFIGURED_FEATURE).ifPresent(ModConfiguredFeatures::bootstrapDirect);
        });
    }
}
