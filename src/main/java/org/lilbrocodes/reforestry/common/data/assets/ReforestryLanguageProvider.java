package org.lilbrocodes.reforestry.common.data.assets;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import org.lilbrocodes.reforestry.common.registry.ModBlocks;
import org.lilbrocodes.reforestry.common.registry.ModItemGroups;

public class ReforestryLanguageProvider extends FabricLanguageProvider {
    public ReforestryLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        builder.add(ModBlocks.BLUE_SPRUCE_SAPLING.block, "Blue Spruce Sapling");
        builder.add(ModBlocks.BLUE_SPRUCE_LEAVES.block, "Blue Spruce Leaves");

        builder.add(ModItemGroups.REFORESTRY_ITEMS_GROUP, "Re::Forestry");
    }
}
