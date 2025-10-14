package org.lilbrocodes.reforestry.mixin.impl.features;

import net.minecraft.registry.Registerable;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import org.lilbrocodes.reforestry.common.registry.ModFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConfiguredFeatures.class)
public class ConfiguredFeaturesMixin {
    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void reForestry$addFeatures(Registerable<ConfiguredFeature<?, ?>> featureRegisterable, CallbackInfo ci) {
        ModFeatures.Configured.bootstrap(featureRegisterable);
    }
}
