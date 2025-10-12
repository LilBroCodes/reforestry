package org.lilbrocodes.reforestry.common.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.lilbrocodes.reforestry.common.data.assets.ReforestryLanguageProvider;
import org.lilbrocodes.reforestry.common.data.assets.ReforestryModelProvider;
import org.lilbrocodes.reforestry.common.data.data.ReforestryBlockLootTableGenerator;
import org.lilbrocodes.reforestry.common.data.data.ReforestryBlockTagProvider;

public class ReforestryDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();

        pack.addProvider(ReforestryBlockTagProvider::new);
        pack.addProvider(ReforestryLanguageProvider::new);
        pack.addProvider(ReforestryBlockLootTableGenerator::new);
        pack.addProvider(ReforestryModelProvider::new);
    }
}
