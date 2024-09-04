package baguchan.noncave.data;

import baguchan.noncave.NonCave;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.NOISE, (context) -> {
            })
            .add(Registries.DENSITY_FUNCTION, ModDensityFunctions::bootstrap)
            .add(Registries.NOISE_SETTINGS, ModDimensionSettings::bootstrapNoise);


    public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft", NonCave.MODID));
    }
}