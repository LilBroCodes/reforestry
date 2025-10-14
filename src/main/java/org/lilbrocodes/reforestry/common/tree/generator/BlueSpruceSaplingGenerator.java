package org.lilbrocodes.reforestry.common.tree.generator;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import org.lilbrocodes.reforestry.common.registry.ModFeatures;

public class BlueSpruceSaplingGenerator extends SaplingGenerator {
    @Override
    protected @Nullable RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return ModFeatures.Configured.BLUE_SPRUCE;
    }
}
