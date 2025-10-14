package org.lilbrocodes.reforestry.common.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

public class ModWorldGeneration {
    public static void initialize() {
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BiomeTags.IS_TAIGA),
                GenerationStep.Feature.SURFACE_STRUCTURES,
                ModFeatures.Placed.BLUE_SPRUCE_CHECKED
        );
    }
}
