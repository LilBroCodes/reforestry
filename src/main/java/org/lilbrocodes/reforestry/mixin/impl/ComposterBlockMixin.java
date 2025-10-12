package org.lilbrocodes.reforestry.mixin.impl;

import net.minecraft.block.Block;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import org.lilbrocodes.reforestry.common.utils.CompostableItem;
import org.lilbrocodes.reforestry.mixin.accessor.ComposterAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ComposterBlock.class)
public abstract class ComposterBlockMixin implements ComposterAccessor {
    @Shadow
    private static void registerCompostableItem(float levelIncreaseChance, ItemConvertible item) {
    }

    @Unique private static final List<CompostableItem> COMPOSABLES = new ArrayList<>();

    @Override
    public void reForestry$addCompostable(Item item, float chance) {
        COMPOSABLES.add(new CompostableItem(item, chance));
    }

    @Inject(method = "registerDefaultCompostableItems", at = @At("TAIL"))
    private static void reForestry$registerCompostables(CallbackInfo ci) {
        COMPOSABLES.forEach(composable -> {
            registerCompostableItem(composable.chance(), composable.item());
        });
    }
}
