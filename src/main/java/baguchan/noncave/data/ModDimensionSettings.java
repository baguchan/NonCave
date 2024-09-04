package baguchan.noncave.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;

import java.util.List;

public class ModDimensionSettings {
    protected static final NoiseSettings OVERWORLD_NOISE_SETTINGS = NoiseSettings.create(-64, 384, 1, 2);
    public static final NoiseSettings NETHER_NOISE_SETTINGS = NoiseSettings.create(0, 128, 1, 2);

    public static final ResourceKey<NoiseGeneratorSettings> OVERWORLD = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.withDefaultNamespace("overworld"));
    public static final ResourceKey<NoiseGeneratorSettings> NETHER = ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.withDefaultNamespace("nether"));

    public static NoiseGeneratorSettings moddedNoise(BootstrapContext<NoiseGeneratorSettings> p_256365_) {
        return new NoiseGeneratorSettings(OVERWORLD_NOISE_SETTINGS, Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), ModNoiseRouterData.moddedCave(p_256365_.lookup(Registries.DENSITY_FUNCTION), p_256365_.lookup(Registries.NOISE)), ModSurfaceRuleData.overworld(), (new OverworldBiomeBuilder()).spawnTarget(), 63, false, true, true, false);
    }

    public static void bootstrapNoise(BootstrapContext<NoiseGeneratorSettings> p_256365_) {
        p_256365_.register(OVERWORLD, moddedNoise(p_256365_));
        p_256365_.register(NETHER, nether(p_256365_));
    }

    public static NoiseGeneratorSettings nether(BootstrapContext<?> p_256180_) {
        return new NoiseGeneratorSettings(NETHER_NOISE_SETTINGS, Blocks.NETHERRACK.defaultBlockState(), Blocks.LAVA.defaultBlockState(), ModNoiseRouterData.nether(p_256180_.lookup(Registries.DENSITY_FUNCTION), p_256180_.lookup(Registries.NOISE)), SurfaceRuleData.nether(), List.of(), 32, false, true, false, true);
    }


}