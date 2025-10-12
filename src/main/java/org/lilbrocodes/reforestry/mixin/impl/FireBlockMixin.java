package org.lilbrocodes.reforestry.mixin.impl;

import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;
import org.lilbrocodes.reforestry.mixin.accessor.FireAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin implements FireAccessor {
    @Shadow protected abstract void registerFlammableBlock(Block block, int burnChance, int spreadChance);

    @Override
    public void reForestry$addFlammable(Block block, int burnChance, int spreadChance) {
        registerFlammableBlock(block, burnChance, spreadChance);
    }
}
