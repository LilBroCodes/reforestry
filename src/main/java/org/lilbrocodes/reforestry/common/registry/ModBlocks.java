package org.lilbrocodes.reforestry.common.registry;

import com.codex.composer.api.v1.item.settings.ComposerItemSettings;
import com.codex.composer.api.v1.registry.lazy.DeferredItemRegistry;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import com.codex.composer.api.v1.registry.lazy.DeferredBlockRegistry;
import com.codex.composer.api.v1.registry.lazy.DeferredBlockRegistry.BlockProvider;
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
            new DeferredBlockRegistry(Reforestry.MOD_ID);
    private static final DeferredItemRegistry ITEMS =
            new DeferredItemRegistry(Reforestry.MOD_ID, ModItemGroups.REFORESTRY_ITEMS_GROUP);

    public static class BlockWithItem<T extends Block> {
        BlockWithItem(T block,Item item){
            this.block=block;
            this.item=item;
        }

        public T block;
        public Item item;
    }

    public static <T extends Block> BlockWithItem<T> registerBlockWithItem(String name,T block){
        Block block1 = BLOCKS.register(name, block);
        return new BlockWithItem(
                block1,
                ITEMS.register(block1,name,new ComposerItemSettings())
        );
    }

    public static <T extends Block> BlockWithItem<T> registerBlockWithItem(String name,T block,boolean addToGroup){
        Block block1 = BLOCKS.register(name, block);
        return new BlockWithItem(
                block1,
                ITEMS.register(block1,name,new ComposerItemSettings(),addToGroup)
        );
    }

    public static final BlockWithItem<Block> BLUE_SPRUCE_SAPLING = registerBlockWithItem(
            "blue_spruce_sapling",
            new SaplingBlock(new BlueSpruceSaplingGenerator(), AbstractBlock.Settings.copy(Blocks.SPRUCE_SAPLING))
    );

    public static final BlockWithItem<Block> POTTED_BLUE_SPRUCE_SAPLING = registerBlockWithItem(
            "potted_blue_spruce_sapling",
            createFlowerPotBlock(BLUE_SPRUCE_SAPLING.block),
            false
    );

    public static final BlockWithItem<LeavesBlock> BLUE_SPRUCE_LEAVES = registerBlockWithItem(
            "blue_spruce_leaves",
            createLeavesBlock(BlockSoundGroup.GRASS)
    );

    public static final BlockWithItem<Block> EARTHSPINE_SAPLING = registerBlockWithItem(
            "earthspine_sapling",
            new SaplingBlock(new EarthspineSaplingGenerator(), AbstractBlock.Settings.copy(Blocks.SPRUCE_SAPLING))
    );

    public static final BlockWithItem<Block> POTTED_EARTHSPINE_SAPLING = registerBlockWithItem(
            "potted_earthspine_sapling",
            createFlowerPotBlock(EARTHSPINE_SAPLING.block),
            false
    );

    public static final BlockWithItem<PillarBlock> EARTHSPINE_LOG = registerBlockWithItem(
            "earthspine_log",
            createLogBlock(MapColor.OFF_WHITE, MapColor.LIGHT_BLUE_GRAY)
    );

    public static final BlockWithItem<PillarBlock> STRIPPED_EARTHSPINE_LOG = registerBlockWithItem(
            "stripped_earthspine_log",
            createLogBlock(MapColor.OFF_WHITE, MapColor.OFF_WHITE)
    );

    public static final BlockWithItem<PillarBlock> EARTHSPINE_WOOD = registerBlockWithItem(
            "earthspine_wood",
            new PillarBlock(AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE_GRAY).instrument(Instrument.BASS).strength(2.0F).sounds(BlockSoundGroup.WOOD).burnable())
    );

    public static final BlockWithItem<PillarBlock> STRIPPED_EARTHSPINE_WOOD = registerBlockWithItem(
            "stripped_earthspine_wood",
            new PillarBlock(AbstractBlock.Settings.create().mapColor(MapColor.OFF_WHITE).instrument(Instrument.BASS).strength(2.0F).sounds(BlockSoundGroup.WOOD).burnable())
    );

    public static final BlockWithItem<LeavesBlock> EARTHSPINE_LEAVES = registerBlockWithItem(
            "earthspine_leaves",
            createLeavesBlock(BlockSoundGroup.GRASS)
    );

    public static final BlockWithItem<StairsBlock> EARTHSPINE_STAIRS = registerBlockWithItem(
            "earthspine_stairs",
            new StairsBlock(WARPED_PLANKS.getDefaultState(), AbstractBlock.Settings.copy(WARPED_PLANKS))
    );


    public static final BlockWithItem<DoorBlock> EARTHSPINE_DOOR = registerBlockWithItem(
            "earthspine_door",
            new DoorBlock(
                    AbstractBlock.Settings.create()
                            .mapColor(WARPED_PLANKS.getDefaultMapColor())
                            .instrument(Instrument.BASS)
                            .strength(3.0F)
                            .nonOpaque()
                            .burnable()
                            .pistonBehavior(PistonBehavior.DESTROY),
                    BlockSetType.OAK
            )
    );

    public static final BlockWithItem<Block> EARTHSPINE_PLANKS = registerBlockWithItem(
            "earthspine_planks",
            new Block(AbstractBlock.Settings.create().mapColor(MapColor.EMERALD_GREEN).instrument(Instrument.BASS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD).burnable())
    );

    public static final BlockWithItem<TrapdoorBlock> EARTHSPINE_TRAPDOOR = registerBlockWithItem(
            "earthspine_trapdoor",
            new TrapdoorBlock(
                    AbstractBlock.Settings.create().mapColor(MapColor.EMERALD_GREEN).instrument(Instrument.BASS).strength(3.0F).nonOpaque().allowsSpawning(Blocks::never).burnable(),
                    BlockSetType.SPRUCE
            )
    );

    // TODO: Add the other stuff made out of the wood type here

    public static void initialize() {
        ITEMS.finalizeRegistration();

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
