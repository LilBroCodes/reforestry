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
import org.lilbrocodes.reforestry.mixin.accessor.TreeNodeDataAccessor;

import java.util.ArrayList;
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
        int trunkHeightVariance = 2;

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
        int totalHeight = this.getHeight(random) + 2 + random.nextInt(trunkHeightVariance + 1);

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
            fillCircleSmooth(replacer, random, center, radius, config);
            topPos = center;
        }

        int branchCount = branchCountMin + random.nextInt(branchCountMax - branchCountMin + 1);
        List<FoliagePlacer.TreeNode> foliage = new java.util.ArrayList<>();

        List<Double> usedAngles = new ArrayList<>();

        for (int i = 0; i < branchCount; i++) {
            double branchYaw = pickSpacedAngle(random, usedAngles, Math.toRadians(40)); // 40° min separation
            usedAngles.add(branchYaw);

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

            List<BlockPos> positions = new ArrayList<>();
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
                positions.add(center);

                endPos = center;
            }

            FoliagePlacer.TreeNode node = new FoliagePlacer.TreeNode(endPos.up(), 0, false);
            if (!(((Object) node) instanceof TreeNodeDataAccessor accessor)) return foliage;
            positions.forEach(accessor::reForestry$addPosition);

            foliage.add(node);
        }

        return foliage;
    }

    private static double pickSpacedAngle(Random random, List<Double> usedAngles, double minSeparation) {
        if (usedAngles.isEmpty()) {
            return random.nextDouble() * Math.PI * 2;
        }

        int samples = 16;
        double bestAngle = 0;
        double bestMinDist = -1;

        for (int i = 0; i < samples; i++) {
            double candidate = random.nextDouble() * Math.PI * 2;

            double minDist = usedAngles.stream()
                    .mapToDouble(a -> angularDistance(a, candidate))
                    .min().orElse(Math.PI);

            if (minDist > bestMinDist) {
                bestMinDist = minDist;
                bestAngle = candidate;
            }
        }

        double jitter = (random.nextDouble() - 0.5) * minSeparation * 0.3;
        return (bestAngle + jitter + Math.PI * 2) % (Math.PI * 2);
    }

    private static double angularDistance(double a, double b) {
        double diff = Math.abs(a - b) % (Math.PI * 2);
        return diff > Math.PI ? (Math.PI * 2 - diff) : diff;
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
            BiConsumer<BlockPos, BlockState> replacer,
            Random random,
            BlockPos center,
            double radius,
            TreeFeatureConfig config
    ) {
        int ceil = (int)Math.ceil(radius);
        for (int x = -ceil; x <= ceil; x++) {
            for (int z = -ceil; z <= ceil; z++) {
                double dist = Math.sqrt(x * x + z * z);
                if (dist <= radius) {
                    BlockPos pos = center.add(x, 0, z);
                    replacer.accept(pos, config.trunkProvider.get(random, pos));
                }
            }
        }
    }
}
