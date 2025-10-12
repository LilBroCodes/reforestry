package org.lilbrocodes.reforestry.common.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.BlockSoundGroup;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredBlockRegistry;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredBlockRegistry.BlockWithItem;
import org.lilbrocodes.reforestry.Reforestry;
import org.lilbrocodes.reforestry.common.tree.generator.BlueSpruceSaplingGenerator;
import org.lilbrocodes.reforestry.mixin.accessor.ComposterAccessor;
import org.lilbrocodes.reforestry.mixin.accessor.FireAccessor;

import static net.minecraft.block.Blocks.*;

public class ModBlocks {
    public static final int BLUE_SPRUCE_LEAVES_COLOR = 0xa5dced;

    private static final DeferredBlockRegistry BLOCKS =
            new DeferredBlockRegistry(Reforestry.MOD_ID, ModItemGroups.REFORESTRY_ITEMS_GROUP);

    public static final BlockWithItem<Block> BLUE_SPRUCE_SAPLING =
            BLOCKS.register("blue_spruce_sapling",
                    new SaplingBlock(new BlueSpruceSaplingGenerator(),
                            AbstractBlock.Settings.copy(Blocks.SPRUCE_SAPLING)),
                    true
            );

    public static final BlockWithItem<LeavesBlock> BLUE_SPRUCE_LEAVES =
            BLOCKS.register(
                    "blue_spruce_leaves",
                    createLeavesBlock(BlockSoundGroup.GRASS),
                    true
            );

    public static final BlockWithItem<Block> POTTED_BLUE_SPRUCE_SAPLING =
            BLOCKS.register(
                    "potted_blue_spruce_sapling",
                    createFlowerPotBlock(BLUE_SPRUCE_SAPLING.block),
                    false
            );

    public static void initialize() {
        BLOCKS.finalizeRegistration();

        if (FIRE instanceof FireAccessor fire) {
            fire.reForestry$addFlammable(BLUE_SPRUCE_LEAVES.block, 30, 60);
        }

        if (COMPOSTER instanceof ComposterAccessor composter) {
            composter.reForestry$addCompostable(BLUE_SPRUCE_LEAVES.item, 0.3F);
        }
    }

    public static void initializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(BLUE_SPRUCE_SAPLING.block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLUE_SPRUCE_LEAVES.block, RenderLayer.getCutoutMipped());

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> BLUE_SPRUCE_LEAVES_COLOR, BLUE_SPRUCE_LEAVES.block);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> BLUE_SPRUCE_LEAVES_COLOR, BLUE_SPRUCE_LEAVES.item);
    }
}
