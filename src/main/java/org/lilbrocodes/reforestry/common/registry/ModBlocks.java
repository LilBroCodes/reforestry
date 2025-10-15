package org.lilbrocodes.reforestry.common.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.BlockSoundGroup;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredBlockRegistry;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredBlockRegistry.BlockWithItem;
import org.lilbrocodes.reforestry.Reforestry;
import org.lilbrocodes.reforestry.common.tree.generator.BlueSpruceSaplingGenerator;
import org.lilbrocodes.reforestry.common.tree.generator.EarthspineSaplingGenerator;
import org.lilbrocodes.reforestry.mixin.accessor.ComposterAccessor;
import org.lilbrocodes.reforestry.mixin.accessor.FireAccessor;

import static net.minecraft.block.Blocks.*;

public class ModBlocks {
    public static final int BLUE_SPRUCE_LEAVES_COLOR = 0xa5dced;
    public static final int EARTHSPINE_LEAVES_COLOR = 0x6faf95;

    private static final DeferredBlockRegistry BLOCKS =
            new DeferredBlockRegistry(Reforestry.MOD_ID, ModItemGroups.REFORESTRY_ITEMS_GROUP);

    public static final BlockWithItem<Block> BLUE_SPRUCE_SAPLING = BLOCKS.register(
            "blue_spruce_sapling",
            new SaplingBlock(new BlueSpruceSaplingGenerator(), AbstractBlock.Settings.copy(Blocks.SPRUCE_SAPLING))
    );

    public static final BlockWithItem<Block> POTTED_BLUE_SPRUCE_SAPLING = BLOCKS.register(
            "potted_blue_spruce_sapling",
            createFlowerPotBlock(BLUE_SPRUCE_SAPLING.block),
            false
    );

    public static final BlockWithItem<LeavesBlock> BLUE_SPRUCE_LEAVES = BLOCKS.register(
            "blue_spruce_leaves",
            createLeavesBlock(BlockSoundGroup.GRASS)
    );

    public static final BlockWithItem<Block> EARTHSPINE_SAPLING = BLOCKS.register(
            "earthspine_sapling",
            new SaplingBlock(new EarthspineSaplingGenerator(), AbstractBlock.Settings.copy(Blocks.SPRUCE_SAPLING))
    );

    public static final BlockWithItem<Block> POTTED_EARTHSPINE_SAPLING = BLOCKS.register(
            "potted_earthspine_sapling",
            createFlowerPotBlock(EARTHSPINE_SAPLING.block),
            false
    );

    public static final BlockWithItem<PillarBlock> EARTHSPINE_LOG = BLOCKS.register(
            "earthspine_log",
            createLogBlock(MapColor.OFF_WHITE, MapColor.LIGHT_BLUE_GRAY)
    );

    public static final BlockWithItem<PillarBlock> STRIPPED_EARTHSPINE_LOG = BLOCKS.register(
            "stripped_earthspine_log",
            createLogBlock(MapColor.OFF_WHITE, MapColor.OFF_WHITE)
    );

    public static final BlockWithItem<PillarBlock> EARTHSPINE_WOOD = BLOCKS.register(
            "earthspine_wood",
            new PillarBlock(AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE_GRAY).instrument(Instrument.BASS).strength(2.0F).sounds(BlockSoundGroup.WOOD).burnable())
    );

    public static final BlockWithItem<PillarBlock> STRIPPED_EARTHSPINE_WOOD = BLOCKS.register(
            "stripped_earthspine_wood",
            new PillarBlock(AbstractBlock.Settings.create().mapColor(MapColor.OFF_WHITE).instrument(Instrument.BASS).strength(2.0F).sounds(BlockSoundGroup.WOOD).burnable())
    );

    public static final BlockWithItem<LeavesBlock> EARTHSPINE_LEAVES = BLOCKS.register(
            "earthspine_leaves",
            createLeavesBlock(BlockSoundGroup.GRASS)
    );

    public static final BlockWithItem<StairsBlock> EARTHSPINE_STAIRS = BLOCKS.register(
            "earthspine_stairs",
            new StairsBlock(OAK_PLANKS.getDefaultState(), AbstractBlock.Settings.copy(OAK_PLANKS))
    );


    public static final BlockWithItem<DoorBlock> EARTHSPINE_DOOR = BLOCKS.register(
            "earthspine_door",
            new DoorBlock(
                    AbstractBlock.Settings.create()
                            .mapColor(OAK_PLANKS.getDefaultMapColor())
                            .instrument(Instrument.BASS)
                            .strength(3.0F)
                            .nonOpaque()
                            .burnable()
                            .pistonBehavior(PistonBehavior.DESTROY),
                    BlockSetType.OAK
            )
    );

    public static final BlockWithItem<Block> EARTHSPINE_PLANKS = BLOCKS.register(
            "earthspine_planks",
            new Block(AbstractBlock.Settings.create().mapColor(MapColor.EMERALD_GREEN).instrument(Instrument.BASS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD).burnable())
    );

    public static final BlockWithItem<TrapdoorBlock> EARTHSPINE_TRAPDOOR = BLOCKS.register(
            "earthspine_trapdoor",
            new TrapdoorBlock(
                    AbstractBlock.Settings.create().mapColor(MapColor.EMERALD_GREEN).instrument(Instrument.BASS).strength(3.0F).nonOpaque().allowsSpawning(Blocks::never).burnable(),
                    BlockSetType.SPRUCE
            )
    );

    // TODO: Add the other stuff made out of the wood type here

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
        BlockRenderLayerMap.INSTANCE.putBlock(EARTHSPINE_SAPLING.block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLUE_SPRUCE_LEAVES.block, RenderLayer.getCutoutMipped());

        BlockRenderLayerMap.INSTANCE.putBlock(POTTED_BLUE_SPRUCE_SAPLING.block, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(POTTED_EARTHSPINE_SAPLING.block, RenderLayer.getCutout());

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> BLUE_SPRUCE_LEAVES_COLOR, BLUE_SPRUCE_LEAVES.block);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> BLUE_SPRUCE_LEAVES_COLOR, BLUE_SPRUCE_LEAVES.item);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> EARTHSPINE_LEAVES_COLOR, EARTHSPINE_LEAVES.block);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> EARTHSPINE_LEAVES_COLOR, EARTHSPINE_LEAVES.item);
    }
}
