package org.lilbrocodes.reforestry.common.tree.trunk;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.lilbrocodes.reforestry.common.registry.ModTrunkPlacerTypes;

import java.util.List;
import java.util.function.BiConsumer;

public class DividableTrunkPlacer extends TrunkPlacer {
    private final int divisor;

    public static final Codec<DividableTrunkPlacer> CODEC = RecordCodecBuilder.create(
            instance -> fillTrunkPlacerFields(instance)
                    .and(Codec.INT.fieldOf("divisor").forGetter(placer -> placer.divisor))
                    .apply(instance, DividableTrunkPlacer::new)
    );

    public DividableTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight, int divisor) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.divisor = divisor;
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return ModTrunkPlacerTypes.DIVIDABLE_TRUNK_PLACER;
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
        setToDirt(world, replacer, random, startPos.down(), config);

        int finalHeight = height;
        if (divisor > 1) {
            finalHeight = height - (height % divisor);
        }

        for (int i = 0; i < finalHeight; i++) {
            this.getAndSetState(world, replacer, random, startPos.up(i), config);
        }

        return ImmutableList.of(new FoliagePlacer.TreeNode(startPos.up(finalHeight), 0, false));
    }
}
