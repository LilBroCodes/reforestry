package org.lilbrocodes.reforestry.common.tree.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import org.lilbrocodes.reforestry.common.registry.ModFoliagePlacers;

public class EarthspineFoliagePlacer extends FoliagePlacer {
    public static final Codec<EarthspineFoliagePlacer> CODEC = RecordCodecBuilder.create(
            instance -> fillFoliagePlacerFields(instance)
                    .apply(instance, EarthspineFoliagePlacer::new)
    );

    public EarthspineFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return ModFoliagePlacers.EARTHSPINE_FOLIAGE_PLACER;
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
        if (treeNode.isGiantTrunk()) return;

        BlockPos center = treeNode.getCenter();
        int foliageRadius = 2 + random.nextInt(2);
        int foliageHeightRange = 2 + random.nextInt(2);

        for (int dx = -foliageRadius; dx <= foliageRadius; dx++) {
            for (int dy = -foliageHeightRange; dy <= foliageHeightRange; dy++) {
                for (int dz = -foliageRadius; dz <= foliageRadius; dz++) {
                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (dist <= foliageRadius + 0.5) {
                        BlockPos leafPos = center.add(dx, dy, dz);
                        if (world.testBlockState(leafPos, s ->
                                s.isAir() ||
                                        s.isOf(Blocks.VINE) ||
                                        s.isOf(Blocks.SNOW) ||
                                        s.isOf(config.foliageProvider.get(random, leafPos).getBlock()))) {

                            BlockState leaf = config.foliageProvider.get(random, leafPos);
                            placer.placeBlock(leafPos, leaf);
                        }
                    }
                }
            }
        }
    }

    static void placeLeaf(Random random, BlockPlacer placer, TestableWorld world, BlockPos pos, TreeFeatureConfig config) {
        if (world.testBlockState(pos, state ->
                state.isAir() || state.isOf(config.foliageProvider.get(random, pos).getBlock())
                        || state.isOf(Blocks.VINE)
                        || state.isOf(Blocks.SNOW))) {
            placer.placeBlock(pos, config.foliageProvider.get(random, pos));placer.placeBlock(pos, config.foliageProvider.get(random, pos));
        }
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return 0;
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return false;
    }
}
