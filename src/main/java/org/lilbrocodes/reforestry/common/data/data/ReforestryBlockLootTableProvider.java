package org.lilbrocodes.reforestry.common.data.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import org.lilbrocodes.reforestry.common.registry.ModBlocks;


public class ReforestryBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ReforestryBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.BLUE_SPRUCE_SAPLING.block);
        addDrop(ModBlocks.EARTHSPINE_SAPLING.block);
        addDrop(ModBlocks.EARTHSPINE_LOG.block);
        addDrop(ModBlocks.STRIPPED_EARTHSPINE_LOG.block);
        addDrop(ModBlocks.EARTHSPINE_WOOD.block);
        addDrop(ModBlocks.STRIPPED_EARTHSPINE_WOOD.block);
        addDrop(ModBlocks.BLUE_SPRUCE_LEAVES.block, block -> leavesDrops(block, ModBlocks.BLUE_SPRUCE_SAPLING.block, SAPLING_DROP_CHANCE));
        addDrop(ModBlocks.EARTHSPINE_LEAVES.block, block -> leavesDrops(block, ModBlocks.EARTHSPINE_SAPLING.block, SAPLING_DROP_CHANCE));
    }
}
