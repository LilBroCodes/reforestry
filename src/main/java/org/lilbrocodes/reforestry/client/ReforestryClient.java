package org.lilbrocodes.reforestry.client;

import net.fabricmc.api.ClientModInitializer;
import org.lilbrocodes.reforestry.common.registry.ModBlocks;

public class ReforestryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModBlocks.initializeClient();
    }
}
