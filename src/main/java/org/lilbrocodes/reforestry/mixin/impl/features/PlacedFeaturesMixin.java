package org.lilbrocodes.reforestry.mixin.impl.features;

import net.minecraft.registry.Registerable;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import org.lilbrocodes.reforestry.common.registry.ModFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlacedFeatures.class)
public class PlacedFeaturesMixin {
    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void reForestry$addFeatures(Registerable<PlacedFeature> featureRegisterable, CallbackInfo ci) {
        ModFeatures.Placed.bootstrap(featureRegisterable);
    }
}
