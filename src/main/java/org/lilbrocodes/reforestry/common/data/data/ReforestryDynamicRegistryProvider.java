package org.lilbrocodes.reforestry.common.data.data;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import org.lilbrocodes.reforestry.Reforestry;
import org.slf4j.Logger;

public class ReforestryDynamicRegistryProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final DataOutput output;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture;

    public ReforestryDynamicRegistryProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        this.output = output;
        this.registryLookupFuture = registryLookupFuture;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return registryLookupFuture.thenCompose(lookup -> {
            DynamicOps<JsonElement> ops = RegistryOps.of(JsonOps.INSTANCE, lookup);
            return CompletableFuture.allOf(
                    writeRegistryEntries(writer, lookup, ops, RegistryLoader.DYNAMIC_REGISTRIES.get(4)).orElse(null),
                    writeRegistryEntries(writer, lookup, ops, RegistryLoader.DYNAMIC_REGISTRIES.get(5)).orElse(null)
            );
        });
    }

    private <T> Optional<CompletableFuture<?>> writeRegistryEntries(
            DataWriter writer, RegistryWrapper.WrapperLookup lookup, DynamicOps<JsonElement> ops, RegistryLoader.Entry<T> registry
    ) {
        RegistryKey<? extends Registry<T>> registryKey = registry.key();
        return lookup.getOptionalWrapper(registryKey).map(wrapper -> {
            DataOutput.PathResolver resolver =
                    output.getResolver(DataOutput.OutputType.DATA_PACK, registryKey.getValue().getPath());

            return CompletableFuture.allOf(
                    wrapper.streamEntries()
                            .filter(entry -> entry.registryKey().getValue().getNamespace().equals(Reforestry.MOD_ID))
                            .map(entry -> writeToPath(
                                    resolver.resolveJson(entry.registryKey().getValue()),
                                    writer,
                                    ops,
                                    registry.elementCodec(),
                                    entry.value()
                            ))
                            .toArray(CompletableFuture[]::new)
            );
        });
    }

    private static <E> CompletableFuture<?> writeToPath(
            Path path, DataWriter writer, DynamicOps<JsonElement> json, Encoder<E> encoder, E value
    ) {
        Optional<JsonElement> optional =
                encoder.encodeStart(json, value)
                        .resultOrPartial(err -> LOGGER.error("Failed to encode {}: {}", path, err));
        return optional.isPresent() ? DataProvider.writeToPath(writer, optional.get(), path)
                : CompletableFuture.completedFuture(null);
    }

    @Override
    public String getName() {
        return "Dynamic registries (" + Reforestry.MOD_ID + ")";
    }
}
