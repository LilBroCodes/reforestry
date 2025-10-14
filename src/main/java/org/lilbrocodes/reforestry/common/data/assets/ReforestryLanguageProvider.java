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
        builder.add(ModBlocks.EARTHSPINE_SAPLING.block, "Earthspine Sapling");
        builder.add(ModBlocks.EARTHSPINE_LOG.block, "Earthspine Log");
        builder.add(ModBlocks.EARTHSPINE_WOOD.block, "Earthspine Wood");
        builder.add(ModBlocks.STRIPPED_EARTHSPINE_LOG.block, "Stripped Earthspine Log");
        builder.add(ModBlocks.STRIPPED_EARTHSPINE_WOOD.block, "Stripped Earthspine Wood");
        builder.add(ModBlocks.EARTHSPINE_LEAVES.block, "Earthspine Leaves");
        builder.add(ModBlocks.BLUE_SPRUCE_LEAVES.block, "Blue Spruce Leaves");

        builder.add(ModItemGroups.REFORESTRY_ITEMS_GROUP, "Re::Forestry");
    }
}
