package org.lilbrocodes.reforestry.common.data.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import org.lilbrocodes.reforestry.common.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ReforestryBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ReforestryBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.SAPLINGS)
                .add(ModBlocks.BLUE_SPRUCE_SAPLING.block);

        getOrCreateTagBuilder(BlockTags.FLOWER_POTS)
                .add(ModBlocks.BLUE_SPRUCE_SAPLING.block);
    }
}
