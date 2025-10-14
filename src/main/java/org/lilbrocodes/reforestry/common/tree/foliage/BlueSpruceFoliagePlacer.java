package org.lilbrocodes.reforestry.common.tree.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import org.lilbrocodes.reforestry.common.registry.ModFoliagePlacers;

public class BlueSpruceFoliagePlacer extends FoliagePlacer {
    public static final Codec<BlueSpruceFoliagePlacer> CODEC = RecordCodecBuilder.create(
            instance -> fillFoliagePlacerFields(instance)
                    .and(IntProvider.createValidatingCodec(8, 14)
                            .fieldOf("height")
                            .forGetter(placer -> placer.height))
                    .apply(instance, BlueSpruceFoliagePlacer::new)
    );

    private final IntProvider height;

    public BlueSpruceFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return ModFoliagePlacers.BLUE_SPRUCE_FOLIAGE_PLACER;
    }

    @Override
    protected void generate(
            TestableWorld world,
            BlockPlacer placer,
            Random random,
            TreeFeatureConfig config,
            int trunkHeight,
            TreeNode treeNode,
            int foliageHeight,
            int radius,
            int offset
    ) {
        BlockPos center = treeNode.getCenter().down();

        placeLeaf(random, placer, world, center.east(), config);
        placeLeaf(random, placer, world, center.west(), config);
        placeLeaf(random, placer, world, center.north(), config);
        placeLeaf(random, placer, world, center.south(), config);
        placeLeaf(random, placer, world, center.up(), config);

        int[] layerRadii;

        if (trunkHeight < 10) {
            // 8, 9
            layerRadii = new int[]{0, 2, 1, 3, 2, 4};
        } else if (trunkHeight < 12) {
            // 10, 11
            layerRadii = new int[]{0, 2, 1, 3, 2, 4, 3, 4};
        } else if (trunkHeight < 14) {
            // 12, 13
            layerRadii = new int[]{0, 2, 1, 3, 2, 4, 3, 5, 4, 5};
        } else {
            // 14
            layerRadii = new int[]{0, 2, 1, 3, 2, 4, 3, 4, 3, 5, 4, 5};
        }

        int yOffset = 1;

        for (int i = 0; i < layerRadii.length; i++) {
            int r = layerRadii[i];
            int y = -yOffset - i;

            if (trunkHeight + y <= 1) continue;
            if (r == 0) continue;

            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    int dist = Math.abs(dx) + Math.abs(dz);
                    if (dist > r + 1 || (Math.abs(dx) == r && Math.abs(dz) == r)) continue; // chopped corners

                    BlockPos leafPos = center.add(dx, y, dz);
                    placeLeaf(random, placer, world, leafPos, config);
                }
            }
        }
    }

    private void placeLeaf(Random random, BlockPlacer placer, TestableWorld world, BlockPos pos, TreeFeatureConfig config) {
        if (world.testBlockState(pos, state ->
                state.isAir() || state.isOf(config.foliageProvider.get(random, pos).getBlock())
                        || state.isOf(Blocks.VINE)
                        || state.isOf(Blocks.SNOW))) {
            placer.placeBlock(pos, config.foliageProvider.get(random, pos));placer.placeBlock(pos, config.foliageProvider.get(random, pos));
        }
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        int h = this.height.get(random);
        return (h % 2 == 0) ? h : h + 1;
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return dx * dx + dz * dz > (radius + 0.5) * (radius + 0.5);
    }
}
