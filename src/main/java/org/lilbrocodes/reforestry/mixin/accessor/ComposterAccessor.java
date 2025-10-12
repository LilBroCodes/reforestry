package org.lilbrocodes.reforestry.mixin.accessor;

import net.minecraft.item.Item;

public interface ComposterAccessor {
    void reForestry$addCompostable(Item item, float chance);
}
