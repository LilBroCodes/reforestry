package org.lilbrocodes.reforestry.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.lilbrocodes.reforestry.common.tree.trunk.EarthspineTrunkPlacer;

public class ModTrunkPlacers {
    public static final TrunkPlacerType<EarthspineTrunkPlacer> EARTHSPINE_PLACER = register(
            "earthspine_trunk_placer", EarthspineTrunkPlacer.CODEC
    );

    private static <P extends TrunkPlacer> TrunkPlacerType<P> register(String id, Codec<P> codec) {
        return Registry.register(Registries.TRUNK_PLACER_TYPE, id, new TrunkPlacerType<>(codec));
    }

    public static void initialize() {

    }
}
