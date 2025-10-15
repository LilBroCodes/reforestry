package org.lilbrocodes.reforestry.common.data.assets;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.client.*;
import net.minecraft.data.DataOutput;
import net.minecraft.util.Identifier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.lilbrocodes.reforestry.common.registry.ModBlocks;

public class ReforestryModelProvider implements DataProvider {

    private final DataOutput.PathResolver blockstatesPathResolver;
    private final DataOutput.PathResolver modelsPathResolver;

    public ReforestryModelProvider(FabricDataOutput output) {
        this.blockstatesPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "blockstates");
        this.modelsPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "models");
    }

    public static void generateBlocks(BlockStateModelGenerator generator) {
        generator.registerFlowerPotPlant(ModBlocks.BLUE_SPRUCE_SAPLING.block, ModBlocks.POTTED_BLUE_SPRUCE_SAPLING.block, BlockStateModelGenerator.TintType.NOT_TINTED);
        generator.registerFlowerPotPlant(ModBlocks.EARTHSPINE_SAPLING.block, ModBlocks.POTTED_EARTHSPINE_SAPLING.block, BlockStateModelGenerator.TintType.NOT_TINTED);
        generator.registerSingleton(ModBlocks.BLUE_SPRUCE_LEAVES.block, TexturedModel.LEAVES);
        generator.registerSingleton(ModBlocks.EARTHSPINE_LEAVES.block, TexturedModel.LEAVES);

        generator.registerLog(ModBlocks.EARTHSPINE_LOG.block).log(ModBlocks.EARTHSPINE_LOG.block).wood(ModBlocks.EARTHSPINE_WOOD.block);
        generator.registerLog(ModBlocks.STRIPPED_EARTHSPINE_LOG.block).log(ModBlocks.STRIPPED_EARTHSPINE_LOG.block).wood(ModBlocks.STRIPPED_EARTHSPINE_WOOD.block);

        generator.registerOrientableTrapdoor(ModBlocks.EARTHSPINE_TRAPDOOR.block);
        generator.registerDoor(ModBlocks.EARTHSPINE_DOOR.block);
        generator.registerSingleton(ModBlocks.EARTHSPINE_PLANKS.block, TexturedModel.CUBE_ALL);
    }

    public static void generateItems(ItemModelGenerator generator) {

    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<Block, BlockStateSupplier> blockStates = new HashMap<>();
        Consumer<BlockStateSupplier> blockConsumer = blockStateSupplier -> {
            Block block = blockStateSupplier.getBlock();
            if (blockStates.put(block, blockStateSupplier) != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        };

        Map<Identifier, Supplier<com.google.gson.JsonElement>> models = new HashMap<>();
        BiConsumer<Identifier, Supplier<com.google.gson.JsonElement>> modelConsumer = (id, supplier) -> {
            if (models.put(id, supplier) != null) {
                throw new IllegalStateException("Duplicate model definition for " + id);
            }
        };

        Set<Item> itemModels = new HashSet<>();
        Consumer<Item> itemConsumer = itemModels::add;

        generateBlocks(new BlockStateModelGenerator(blockConsumer, modelConsumer, itemConsumer));
        generateItems(new ItemModelGenerator(modelConsumer));

        return CompletableFuture.allOf(
                writeJsons(writer, blockStates, b -> blockstatesPathResolver.resolveJson(b.getRegistryEntry().registryKey().getValue())),
                writeJsons(writer, models, modelsPathResolver::resolveJson)
        );
    }

    private <T> CompletableFuture<?> writeJsons(DataWriter writer, Map<T, ? extends Supplier<com.google.gson.JsonElement>> map, Function<T, Path> pathGetter) {
        return CompletableFuture.allOf(map.entrySet().stream()
                .map(entry -> DataProvider.writeToPath(writer, entry.getValue().get(), pathGetter.apply(entry.getKey())))
                .toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Reforestry Model Provider";
    }
}
