package org.lilbrocodes.reforestry.mixin.impl;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import org.lilbrocodes.reforestry.mixin.accessor.TreeNodeDataAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(FoliagePlacer.TreeNode.class)
public class TreeNodeMixin implements TreeNodeDataAccessor {
    @Unique private final List<BlockPos> positions = new ArrayList<>();

    @Override
    public List<BlockPos> reForestry$getPositions() {
        return positions;
    }

    @Override
    public void reForestry$addPosition(BlockPos pos) {
        positions.add(pos);
    }
}
