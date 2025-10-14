package org.lilbrocodes.reforestry.common.tree.trunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.lilbrocodes.reforestry.common.registry.ModBlocks;
import org.lilbrocodes.reforestry.common.registry.ModTrunkPlacers;

import java.util.List;
import java.util.function.BiConsumer;

public class EarthspineTrunkPlacer extends TrunkPlacer {
    public static final Codec<EarthspineTrunkPlacer> CODEC = RecordCodecBuilder.create(
            instance -> fillTrunkPlacerFields(instance).apply(instance, EarthspineTrunkPlacer::new)
    );

    public EarthspineTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return ModTrunkPlacers.EARTHSPINE_PLACER;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(
            TestableWorld world,
            BiConsumer<BlockPos, BlockState> replacer,
            Random random,
            int height,
            BlockPos startPos,
            TreeFeatureConfig config
    ) {
        int trunkBaseRadius = 3;
        double trunkTaperAmount = 0.3;
        double trunkStep = 0.4;
        double trunkTiltMin = 10;
        double trunkTiltMax = 20;
        int trunkHeightVariance = 3;

        int branchCountMin = 3;
        int branchCountMax = 6;
        double branchLengthMin = 5;
        double branchLengthMax = 8;
        double branchRadiusMin = 0.5;
        double branchRadiusMax = 0.6;
        double branchTiltMin = 0;
        double branchTiltMax = 0;
        double branchVerticalAttachMin = 0.7;
        double branchVerticalAttachMax = 1.0;

        boolean addTrunkTopFoliage = true;


        int baseRadius = trunkBaseRadius + random.nextInt(1);
        int totalHeight = this.getHeight(random) + random.nextInt(trunkHeightVariance + 1);

        double tiltDegrees = trunkTiltMin + random.nextDouble() * (trunkTiltMax - trunkTiltMin);
        double tiltRadians = Math.toRadians(tiltDegrees);
        double yaw = random.nextDouble() * Math.PI * 2;

        double dx = Math.cos(yaw) * Math.sin(tiltRadians);
        double dz = Math.sin(yaw) * Math.sin(tiltRadians);
        double dy = Math.cos(tiltRadians);

        BlockPos topPos = startPos;

        for (double t = 0; t < totalHeight; t += trunkStep) {
            double cx = startPos.getX() + dx * t;
            double cy = startPos.getY() + dy * t;
            double cz = startPos.getZ() + dz * t;
            BlockPos center = new BlockPos((int)Math.round(cx), (int)Math.round(cy), (int)Math.round(cz));

            double progress = t / totalHeight;
            double radius = baseRadius * (1.0 - trunkTaperAmount * progress);

            boolean isTopLayer = t + trunkStep >= totalHeight;
            fillCircleSmooth(world, replacer, random, center, radius, config, isTopLayer);
            topPos = center;
        }

        int branchCount = branchCountMin + random.nextInt(branchCountMax - branchCountMin + 1);
        List<FoliagePlacer.TreeNode> foliage = new java.util.ArrayList<>();

        if (addTrunkTopFoliage)
            foliage.add(new FoliagePlacer.TreeNode(topPos.up(), 0, true));

        for (int i = 0; i < branchCount; i++) {
            double branchYaw = random.nextDouble() * Math.PI * 2;
            double branchTilt = Math.toRadians(branchTiltMin + random.nextDouble() * (branchTiltMax - branchTiltMin));
            double bdx = Math.cos(branchYaw) * Math.cos(branchTilt);
            double bdy = Math.sin(branchTilt);
            double bdz = Math.sin(branchYaw) * Math.cos(branchTilt);

            double branchLength = branchLengthMin + random.nextDouble() * (branchLengthMax - branchLengthMin);
            double branchRadius = branchRadiusMin + random.nextDouble() * (branchRadiusMax - branchRadiusMin);

            double attachFactor = branchVerticalAttachMin + random.nextDouble() * (branchVerticalAttachMax - branchVerticalAttachMin);
            double attachHeight = totalHeight * attachFactor;
            BlockPos branchStart = startPos.add(
                    (int)Math.round(dx * attachHeight),
                    (int)Math.round(dy * attachHeight),
                    (int)Math.round(dz * attachHeight)
            );

            Direction.Axis branchAxis = getDominantAxis(bdx, bdy, bdz);

            BlockPos endPos = branchStart;
            double segmentStep = 0.4;

            for (double t = 0; t < branchLength; t += segmentStep) {
                double bx = branchStart.getX() + bdx * t;
                double by = branchStart.getY() + bdy * t;
                double bz = branchStart.getZ() + bdz * t;
                BlockPos center = new BlockPos((int)Math.round(bx), (int)Math.round(by), (int)Math.round(bz));

                BlockState log = config.trunkProvider.get(random, center);
                if (log.contains(Properties.AXIS)) {
                    log = log.with(Properties.AXIS, branchAxis);
                }
                replacer.accept(center, log);

                endPos = center;
            }

            foliage.add(new FoliagePlacer.TreeNode(endPos.up(), 0, false));
            if (branchLength > 6 && random.nextBoolean()) {
                BlockPos mid = branchStart.add(
                        (int)Math.round(bdx * branchLength * 0.5),
                        (int)Math.round(bdy * branchLength * 0.5),
                        (int)Math.round(bdz * branchLength * 0.5)
                );
                foliage.add(new FoliagePlacer.TreeNode(mid, 0, false));
            }
        }

        return foliage;
    }

    private static Direction.Axis getDominantAxis(double x, double y, double z) {
        double ax = Math.abs(x);
        double ay = Math.abs(y);
        double az = Math.abs(z);
        if (ay > ax && ay > az) return Direction.Axis.Y;
        if (ax > az) return Direction.Axis.X;
        return Direction.Axis.Z;
    }

    private void fillCircleSmooth(
            TestableWorld world,
            BiConsumer<BlockPos, BlockState> replacer,
            Random random,
            BlockPos center,
            double radius,
            TreeFeatureConfig config,
            boolean topLayer
    ) {
        int ceil = (int)Math.ceil(radius);
        for (int x = -ceil; x <= ceil; x++) {
            for (int z = -ceil; z <= ceil; z++) {
                double dist = Math.sqrt(x * x + z * z);
                if (dist <= radius) {
                    BlockPos pos = center.add(x, 0, z);
                    placeLog(replacer, pos, topLayer ? ModBlocks.EARTHSPINE_WOOD.block.getDefaultState() : config.trunkProvider.get(random, pos));
                }
            }
        }
    }

    private void placeLog(
            BiConsumer<BlockPos, BlockState> replacer,
            BlockPos pos,
            BlockState state
    ) {
        replacer.accept(pos, state);
    }
}
