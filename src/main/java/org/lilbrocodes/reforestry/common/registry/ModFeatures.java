package org.lilbrocodes.reforestry.common.registry;

import net.minecraft.block.Blocks;
import net.minecraft.block.WoodType;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.DarkOakFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import org.lilbrocodes.reforestry.Reforestry;
import org.lilbrocodes.reforestry.common.tree.foliage.BlueSpruceFoliagePlacer;

import java.util.OptionalInt;

import static org.lilbrocodes.reforestry.common.registry.ModFeatures.Configured.BLUE_SPRUCE;

public class ModFeatures {
    public static class Configured {
        public static final RegistryKey<ConfiguredFeature<?, ?>> BLUE_SPRUCE =
                RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Reforestry.identify("blue_spruce"));

        public static final RegistryKey<ConfiguredFeature<?, ?>> EARTHSPINE =
                RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Reforestry.identify("earthspine"));

        public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> registerable) {
            ConfiguredFeatures.register(
                    registerable,
                    BLUE_SPRUCE,
                    Feature.TREE,
                    new TreeFeatureConfig.Builder(
                            BlockStateProvider.of(Blocks.SPRUCE_LOG),

                            new StraightTrunkPlacer(8, 2, 6),

                            BlockStateProvider.of(ModBlocks.BLUE_SPRUCE_LEAVES.block),

                            new BlueSpruceFoliagePlacer(
                                    UniformIntProvider.create(2, 3),
                                    ConstantIntProvider.create(0),
                                    UniformIntProvider.create(8, 14)
                            ),

                            new TwoLayersFeatureSize(2, 0, 3)
                    ).ignoreVines().build()
            );

            ConfiguredFeatures.register(
                    registerable,
                    EARTHSPINE,
                    Feature.TREE,
                    new TreeFeatureConfig.Builder(
                            BlockStateProvider.of(ModBlocks.EARTHSPINE_LOG.block),
                            new DarkOakTrunkPlacer(6, 2, 1),
                            BlockStateProvider.of(ModBlocks.EARTHSPINE_LEAVES.block),
                            new DarkOakFoliagePlacer(ConstantIntProvider.create(0), ConstantIntProvider.create(0)),
                            new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
                    ).ignoreVines().build()
            );
        }
    }

    public static class Placed {
        public static final RegistryKey<PlacedFeature> BLUE_SPRUCE_CHECKED =
                RegistryKey.of(RegistryKeys.PLACED_FEATURE, Reforestry.identify("blue_spruce_checked"));

        public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
            RegistryEntryLookup<ConfiguredFeature<?, ?>> registryEntryLookup = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
            RegistryEntry<ConfiguredFeature<?, ?>> blueSpruceEntry = registryEntryLookup.getOrThrow(BLUE_SPRUCE);
            PlacedFeatures.register(featureRegisterable, BLUE_SPRUCE_CHECKED, blueSpruceEntry, PlacedFeatures.wouldSurvive(ModBlocks.BLUE_SPRUCE_SAPLING.block));
        }
    }

    public static void initialize() {

    }
}
