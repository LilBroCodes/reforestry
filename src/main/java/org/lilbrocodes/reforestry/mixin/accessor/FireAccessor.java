package org.lilbrocodes.reforestry.mixin.accessor;

import net.minecraft.block.Block;

public interface FireAccessor {
    void reForestry$addFlammable(Block block, int burnChance, int spreadChance);
}
