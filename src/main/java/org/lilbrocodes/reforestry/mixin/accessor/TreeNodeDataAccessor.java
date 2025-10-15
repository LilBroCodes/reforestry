package org.lilbrocodes.reforestry.mixin.accessor;

import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface TreeNodeDataAccessor {
    List<BlockPos> reForestry$getPositions();
    void reForestry$addPosition(BlockPos pos);
}
