package org.lilbrocodes.reforestry.common.registry;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import org.lilbrocodes.reforestry.Reforestry;
import org.lilbrocodes.reforestry.common.tree.foliage.BlueSpruceFoliagePlacer;
import org.lilbrocodes.reforestry.common.tree.trunk.DividableTrunkPlacer;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> BLUE_SPRUCE_TREE =
            RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Reforestry.identify("blue_spruce_tree"));

    public static void bootstrapDirect(Registry<ConfiguredFeature<?, ?>> registry) {
        Registry.register(registry, BLUE_SPRUCE_TREE.getValue(), createBlueSpruceTree());
    }

    private static ConfiguredFeature<?, ?> createBlueSpruceTree() {
        return new ConfiguredFeature<>(
                Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(Blocks.SPRUCE_LOG),

                        new DividableTrunkPlacer(8, 2, 6, 2),

                        BlockStateProvider.of(ModBlocks.BLUE_SPRUCE_LEAVES.block),

                        new BlueSpruceFoliagePlacer(
                                UniformIntProvider.create(2, 3),
                                ConstantIntProvider.create(0),
                                UniformIntProvider.create(8, 14)
                        ),

                        new TwoLayersFeatureSize(2, 0, 3)
                )
                        .ignoreVines()
                        .build()
        );
    }
}
