package org.lilbrocodes.reforestry.common.registry;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import com.codex.composer.api.v1.registry.lazy.DeferredItemGroupRegistry;
import org.lilbrocodes.reforestry.Reforestry;

public class ModItemGroups {
    private static final DeferredItemGroupRegistry GROUPS = new DeferredItemGroupRegistry(Reforestry.MOD_ID);

    public static final RegistryKey<ItemGroup> REFORESTRY_ITEMS_GROUP = GROUPS.registerItemGroup(
            "reforestry",
            () -> new ItemStack(ModBlocks.BLUE_SPRUCE_SAPLING.block)
    );

    public static void initialize() {

    }
}
