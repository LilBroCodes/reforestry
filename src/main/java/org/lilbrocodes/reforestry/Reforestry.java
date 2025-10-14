package org.lilbrocodes.reforestry;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.lilbrocodes.reforestry.common.registry.*;

public class Reforestry implements ModInitializer {
    public static final String MOD_ID = "reforestry";

    @Override
    public void onInitialize() {
        ModBlocks.initialize();
        ModFeatures.initialize();
        ModItemGroups.initialize();
        ModFoliagePlacers.initialize();
        ModWorldGeneration.initialize();
    }

    public static Identifier identify(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
