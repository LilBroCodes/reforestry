package org.lilbrocodes.reforestry.mixin.impl;

import net.minecraft.data.client.BlockStateModelGenerator;
import org.lilbrocodes.reforestry.common.data.assets.ReforestryModelProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockStateModelGenerator.class)
public class BlockStateModelGeneratorMixin {
    @Inject(method = "register", at = @At("TAIL"))
    public void reForestry$generateCustom(CallbackInfo ci) {
        ReforestryModelProvider.generateBlocks((BlockStateModelGenerator) (Object) this);
    }
}
