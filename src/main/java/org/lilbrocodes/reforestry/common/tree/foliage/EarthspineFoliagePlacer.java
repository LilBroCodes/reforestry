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
import org.lilbrocodes.reforestry.mixin.accessor.TreeNodeDataAccessor;

import java.util.EnumSet;
import java.util.List;

public class EarthspineFoliagePlacer extends FoliagePlacer {
    public static final Codec<EarthspineFoliagePlacer> CODEC = RecordCodecBuilder.create(
            instance -> fillFoliagePlacerFields(instance)
                    .apply(instance, EarthspineFoliagePlacer::new)
    );

    public enum ShapeType {
        SPHERE, CYLINDER, CONE, CUBE
    }

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
        if (treeNode.isGiantTrunk() || !(((Object) treeNode) instanceof TreeNodeDataAccessor accessor))
            return;

        int horizontalRadius = 3;
        int verticalRadius = 3;
        float density = 0.5f;
        ShapeType shape = ShapeType.SPHERE;
        float falloff = 0.5f;
        EnumSet<Direction> excludedSides = EnumSet.noneOf(Direction.class);

        List<BlockPos> basePositions = accessor.reForestry$getPositions();

        for (BlockPos basePos : basePositions) {
            for (int dx = -horizontalRadius; dx <= horizontalRadius; dx++) {
                for (int dy = -verticalRadius; dy <= verticalRadius; dy++) {
                    for (int dz = -horizontalRadius; dz <= horizontalRadius; dz++) {

                        BlockPos targetPos = basePos.add(dx, dy, dz);

                        if (!isInsideShape(dx, dy, dz, shape, horizontalRadius, verticalRadius))
                            continue;

                        if (Math.abs(dx) + Math.abs(dz) == 1 && dy == 0) {
                            Direction dir = getDirectionFromOffset(dx, dz);
                            if (dir != null && excludedSides.contains(dir))
                                continue;
                        }

                        float dist = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
                        float chance = density * (1.0f - dist / (horizontalRadius + verticalRadius) * falloff);

                        if (random.nextFloat() < chance)
                            placeLeaf(random, placer, world, targetPos, config);
                    }
                }
            }
        }
    }

    private static Direction getDirectionFromOffset(int dx, int dz) {
        if (dx == 1 && dz == 0) return Direction.EAST;
        if (dx == -1 && dz == 0) return Direction.WEST;
        if (dz == 1 && dx == 0) return Direction.SOUTH;
        if (dz == -1 && dx == 0) return Direction.NORTH;
        return null;
    }

    private boolean isInsideShape(int dx, int dy, int dz, ShapeType shape, double horizontalRadius, double verticalRadius) {
        return switch (shape) {
            case CUBE -> true;
            case CYLINDER -> (dx * dx + dz * dz) <= horizontalRadius * horizontalRadius;
            case CONE -> {
                double heightFactor = 1.0 - ((double) dy / verticalRadius);
                yield heightFactor > 0 && (dx * dx + dz * dz) <= (horizontalRadius * horizontalRadius * heightFactor * heightFactor);
            }
            case SPHERE -> (dx * dx + dy * dy + dz * dz) <= (horizontalRadius * horizontalRadius);
        };
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
