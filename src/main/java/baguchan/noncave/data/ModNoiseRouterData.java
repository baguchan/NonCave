package baguchan.noncave.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class ModNoiseRouterData {
    public static final ResourceKey<DensityFunction> FACTOR = createKey("overworld/factor");
    public static final ResourceKey<DensityFunction> DEPTH = createKey("overworld/depth");
    public static final ResourceKey<DensityFunction> SLOPED_CHEESE = createKey("overworld/sloped_cheese");

    public static final ResourceKey<DensityFunction> CONTINENTS = createKey("overworld/continents");
    public static final ResourceKey<DensityFunction> EROSION = createKey("overworld/erosion");
    private static final ResourceKey<DensityFunction> Y = createKey("y");
    private static final ResourceKey<DensityFunction> SHIFT_X = createKey("shift_x");
    private static final ResourceKey<DensityFunction> SHIFT_Z = createKey("shift_z");
    public static final ResourceKey<DensityFunction> RIDGES = createKey("overworld/ridges");
    private static final ResourceKey<DensityFunction> SPAGHETTI_ROUGHNESS_FUNCTION = createKey("overworld/caves/spaghetti_roughness_function");
    private static final ResourceKey<DensityFunction> ENTRANCES = createKey("overworld/caves/entrances");
    private static final ResourceKey<DensityFunction> NOODLE = createKey("overworld/caves/noodle");
    private static final ResourceKey<DensityFunction> PILLARS = createKey("overworld/caves/pillars");
    private static final ResourceKey<DensityFunction> SPAGHETTI_2D_THICKNESS_MODULATOR = createKey("overworld/caves/spaghetti_2d_thickness_modulator");
    private static final ResourceKey<DensityFunction> SPAGHETTI_2D = createKey("overworld/caves/spaghetti_2d");
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_NETHER = createKey("nether/base_3d_noise");

    private static ResourceKey<DensityFunction> createKey(String p_209537_) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(p_209537_));
    }

    private static ResourceKey<DensityFunction> createKey(String modid, String p_209537_) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(modid, p_209537_));
    }

    public static DensityFunction underground(HolderGetter<DensityFunction> p_256548_, HolderGetter<NormalNoise.NoiseParameters> p_256236_, DensityFunction p_256658_, float offset) {
        DensityFunction densityfunction = getFunction(p_256548_, SPAGHETTI_2D);
        DensityFunction densityfunction1 = getFunction(p_256548_, SPAGHETTI_ROUGHNESS_FUNCTION);
        DensityFunction densityfunction2 = DensityFunctions.noise(p_256236_.getOrThrow(Noises.CAVE_LAYER), 8.0);
        DensityFunction densityfunction3 = DensityFunctions.mul(DensityFunctions.constant(4.0), densityfunction2.square());
        DensityFunction densityfunction4 = DensityFunctions.noise(p_256236_.getOrThrow(Noises.CAVE_CHEESE), 0.6666666666666666);
        DensityFunction densityfunction5 = DensityFunctions.add(DensityFunctions.add(DensityFunctions.constant(0.27), densityfunction4).clamp(-1.0, 1.0), DensityFunctions.add(DensityFunctions.constant(1.5), DensityFunctions.mul(DensityFunctions.constant(-0.64), p_256658_)).clamp(-0.5, 0.5));
        DensityFunction densityfunction6 = DensityFunctions.add(densityfunction3, densityfunction5);
        DensityFunction densityfunction7 = DensityFunctions.min(DensityFunctions.min(densityfunction6, getFunction(p_256548_, ENTRANCES)), DensityFunctions.add(densityfunction, densityfunction1));
        DensityFunction densityfunction8 = getFunction(p_256548_, PILLARS);
        DensityFunction densityfunction9 = DensityFunctions.rangeChoice(densityfunction8, -1000000.0, offset, DensityFunctions.constant(-1000000.0), densityfunction8);
        return DensityFunctions.max(densityfunction7, densityfunction9);
    }

    private static DensityFunction postProcess(DensityFunction p_224493_) {
        DensityFunction densityfunction = DensityFunctions.blendDensity(p_224493_);
        return DensityFunctions.mul(DensityFunctions.interpolated(densityfunction), DensityFunctions.constant(0.64D)).squeeze();
    }

    public static NoiseRouter moddedCave(HolderGetter<DensityFunction> p_255681_, HolderGetter<NormalNoise.NoiseParameters> p_256005_) {
        DensityFunction densityfunction = DensityFunctions.noise(p_256005_.getOrThrow(Noises.AQUIFER_BARRIER), 0.5D);
        DensityFunction densityfunction1 = DensityFunctions.noise(p_256005_.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67D);
        DensityFunction densityfunction2 = DensityFunctions.noise(p_256005_.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143D);
        DensityFunction densityfunction3 = DensityFunctions.noise(p_256005_.getOrThrow(Noises.AQUIFER_LAVA));
        DensityFunction densityfunction4 = getFunction(p_255681_, SHIFT_X);
        DensityFunction densityfunction5 = getFunction(p_255681_, SHIFT_Z);
        DensityFunction densityfunction6 = DensityFunctions.shiftedNoise2d(densityfunction4, densityfunction5, 0.25D, p_256005_.getOrThrow(Noises.TEMPERATURE));
        DensityFunction densityfunction7 = DensityFunctions.shiftedNoise2d(densityfunction4, densityfunction5, 0.25D, p_256005_.getOrThrow(Noises.VEGETATION));
        DensityFunction densityfunction8 = getFunction(p_255681_, FACTOR);
        DensityFunction densityfunction9 = getFunction(p_255681_, DEPTH);
        DensityFunction densityfunction10 = noiseGradientDensity(DensityFunctions.cache2d(densityfunction8), densityfunction9);
        DensityFunction densityfunction11 = getFunction(p_255681_, SLOPED_CHEESE);
        DensityFunction densityfunction12 = DensityFunctions.min(densityfunction11, DensityFunctions.mul(DensityFunctions.constant(5.0D), getFunction(p_255681_, ENTRANCES)));


        DensityFunction cave = getFunction(p_255681_, ModDensityFunctions.UNDERGROUND);

        DensityFunction densityfunction13 = DensityFunctions.rangeChoice(densityfunction11, -1000000.0D, 1.5625D, densityfunction12, DensityFunctions.min(slideCaveUpper(cave), slideCave(cave)));

        DensityFunction densityfunction14 = DensityFunctions.min(postProcess(slideOverworld(densityfunction13)), getFunction(p_255681_, NOODLE));
        DensityFunction densityfunction15 = getFunction(p_255681_, Y);
        DensityFunction densityfunction16 = DensityFunctions.zero();
        float f = 4.0F;
        DensityFunction densityfunction19 = DensityFunctions.zero();
        DensityFunction densityfunction20 = DensityFunctions.zero();
        return new NoiseRouter(densityfunction, densityfunction1, densityfunction2, densityfunction3, densityfunction6, densityfunction7, getFunction(p_255681_, CONTINENTS), getFunction(p_255681_, EROSION), densityfunction9, getFunction(p_255681_, RIDGES), slideOverworld(DensityFunctions.add(densityfunction10, DensityFunctions.constant(-0.703125D)).clamp(-64.0D, 64.0D)), densityfunction14, densityfunction16, densityfunction19, densityfunction20);
    }

    private static NoiseRouter netherCaves(HolderGetter<DensityFunction> p_255724_, HolderGetter<NormalNoise.NoiseParameters> p_255986_) {
        DensityFunction aqua_barrier = DensityFunctions.noise(p_255986_.getOrThrow(Noises.AQUIFER_BARRIER), 0.5D);
        DensityFunction aqua_floodness = DensityFunctions.noise(p_255986_.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67D);
        DensityFunction aqua_spread = DensityFunctions.noise(p_255986_.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143D);
        DensityFunction lava = DensityFunctions.noise(p_255986_.getOrThrow(Noises.AQUIFER_LAVA));
        DensityFunction aqua_floodness2 = DensityFunctions.rangeChoice(aqua_floodness, -1, -0.8, DensityFunctions.constant(1), DensityFunctions.zero());
        DensityFunction aqua_spread2 = DensityFunctions.rangeChoice(aqua_spread, -1, -0.8, DensityFunctions.constant(1), DensityFunctions.zero());

        DensityFunction densityfunction = getFunction(p_255724_, SHIFT_X);
        DensityFunction densityfunction1 = getFunction(p_255724_, SHIFT_Z);
        DensityFunction densityfunction2 = DensityFunctions.shiftedNoise2d(densityfunction, densityfunction1, 0.25D, p_255986_.getOrThrow(Noises.TEMPERATURE));
        DensityFunction densityfunction3 = DensityFunctions.shiftedNoise2d(densityfunction, densityfunction1, 0.25D, p_255986_.getOrThrow(Noises.VEGETATION));
        DensityFunction cave = getFunction(p_255724_, ModDensityFunctions.UNDERGROUND_NETHER);

        DensityFunction densityfunction8 = getFunction(p_255724_, FACTOR);
        DensityFunction densityfunction9 = getFunction(p_255724_, DEPTH);
        DensityFunction densityfunction10 = noiseGradientDensity(DensityFunctions.cache2d(densityfunction8), densityfunction9);


        DensityFunction densityfunction11 = getFunction(p_255724_, ModDensityFunctions.SLOPED_CHEESE_NETHER);
        DensityFunction densityfunction12 = DensityFunctions.min(densityfunction11, DensityFunctions.mul(DensityFunctions.constant(5.0D), getFunction(p_255724_, ENTRANCES)));

        DensityFunction densityfunction13 = DensityFunctions.rangeChoice(densityfunction11, -1000000.0D, 1.5625D, densityfunction12, cave);
        DensityFunction densityfunction14 = postProcess(slideNetherLike(p_255724_, densityfunction13, 0, 100));

        return new NoiseRouter(aqua_barrier, aqua_floodness2, aqua_spread2, lava, densityfunction2, densityfunction3, getFunction(p_255724_, CONTINENTS), getFunction(p_255724_, EROSION), densityfunction9, getFunction(p_255724_, RIDGES), slideNetherLike(p_255724_, DensityFunctions.add(densityfunction10, DensityFunctions.constant(-0.703125D)).clamp(0.0D, 64.0D), 0, 100), densityfunction14, DensityFunctions.zero(), DensityFunctions.zero(), DensityFunctions.zero());
    }

    protected static NoiseRouter nether(HolderGetter<DensityFunction> p_256256_, HolderGetter<NormalNoise.NoiseParameters> p_256169_) {
        return netherCaves(p_256256_, p_256169_);
    }

    private static DensityFunction slideNetherLike(HolderGetter<DensityFunction> p_256084_, DensityFunction densityFunction, int p_255802_, int p_255834_) {
        return slide(densityFunction, p_255802_, p_255834_, 24, 0, 0.9375D, -8, 24, 2.5D);
    }

    private static DensityFunction slideOverworld(DensityFunction p_224491_) {
        return slide(p_224491_, -64, 384, 32, 16, -0.078125D, 0, 32, 0.5D);
    }

    private static DensityFunction slideCave(DensityFunction p_224491_) {
        return slide(p_224491_, -64, 64, 24, 0, 0.9375, -8, 24, 2.5);
    }

    private static DensityFunction slideCaveUpper(DensityFunction p_224491_) {
        return slide(p_224491_, 0, 54, 24, 0, 0.9375, -8, 16, 2.5);
    }

    private static DensityFunction noiseGradientDensity(DensityFunction p_212272_, DensityFunction p_212273_) {
        DensityFunction densityfunction = DensityFunctions.mul(p_212273_, p_212272_);
        return DensityFunctions.mul(DensityFunctions.constant(4.0D), densityfunction.quarterNegative());
    }

    private static DensityFunction yLimitedInterpolatable(DensityFunction p_209472_, DensityFunction p_209473_, int p_209474_, int p_209475_, int p_209476_) {
        return DensityFunctions.interpolated(DensityFunctions.rangeChoice(p_209472_, (double) p_209474_, (double) (p_209475_ + 1), p_209473_, DensityFunctions.constant((double) p_209476_)));
    }

    private static DensityFunction slide(DensityFunction density, int minY, int maxY, int fromYTop, int toYTop, double offset1, int fromYBottom, int toYBottom, double offset2) {
        DensityFunction topSlide = DensityFunctions.yClampedGradient(minY + maxY - fromYTop, minY + maxY - toYTop, 1, 0);
        density = DensityFunctions.lerp(topSlide, offset1, density);
        DensityFunction bottomSlide = DensityFunctions.yClampedGradient(minY + fromYBottom, minY + toYBottom, 0, 1);
        return DensityFunctions.lerp(bottomSlide, offset2, density);
    }


    private static DensityFunction getFunction(HolderGetter<DensityFunction> p_224465_, ResourceKey<DensityFunction> p_224466_) {
        return new DensityFunctions.HolderHolder(p_224465_.getOrThrow(p_224466_));
    }
}