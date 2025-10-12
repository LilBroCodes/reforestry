package org.lilbrocodes.reforestry.common.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import org.lilbrocodes.reforestry.common.tree.foliage.BlueSpruceFoliagePlacer;
import org.lilbrocodes.reforestry.Reforestry;

public class ModFoliagePlacers {
    public static final FoliagePlacerType<BlueSpruceFoliagePlacer> BLUE_SPRUCE_FOLIAGE_PLACER =
            register("blue_spruce_foliage_placer", new FoliagePlacerType<>(BlueSpruceFoliagePlacer.CODEC));

    private static <P extends FoliagePlacer> FoliagePlacerType<P> register(String id, FoliagePlacerType<P> type) {
        return Registry.register(Registries.FOLIAGE_PLACER_TYPE, new Identifier(Reforestry.MOD_ID, id), type);
    }

    public static void initialize() {

    }
}
