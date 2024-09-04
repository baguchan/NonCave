package baguchan.noncave.data;

import baguchan.noncave.NonCave;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class ModDensityFunctions {
    private static final DensityFunction BLENDING_FACTOR = DensityFunctions.constant(10.0D);
    private static final DensityFunction BLENDING_JAGGEDNESS = DensityFunctions.zero();

    private static final ResourceKey<DensityFunction> SHIFT_X = createVannilaKey("shift_x");
    private static final ResourceKey<DensityFunction> SHIFT_Z = createVannilaKey("shift_z");
    private static final ResourceKey<DensityFunction> BASE_3D_NOISE_NETHER = createVannilaKey("nether/base_3d_noise");
    public static final ResourceKey<DensityFunction> CONTINENTS = createVannilaKey("overworld/continents");
    public static final ResourceKey<DensityFunction> EROSION = createVannilaKey("overworld/erosion");
    public static final ResourceKey<DensityFunction> RIDGES = createVannilaKey("overworld/ridges");
    public static final ResourceKey<DensityFunction> RIDGES_FOLDED = createVannilaKey("overworld/ridges_folded");
    public static final ResourceKey<DensityFunction> OFFSET = createVannilaKey("overworld/offset");
    public static final ResourceKey<DensityFunction> FACTOR = createVannilaKey("overworld/factor");
    public static final ResourceKey<DensityFunction> JAGGEDNESS = createVannilaKey("overworld/jaggedness");
    public static final ResourceKey<DensityFunction> DEPTH = createVannilaKey("overworld/depth");
    public static final ResourceKey<DensityFunction> SLOPED_CHEESE_NETHER = createKey("nether/sloped_cheese");

    public static final ResourceKey<DensityFunction> PILLARS = createKey("overworld/pillars");
    public static final ResourceKey<DensityFunction> UNDERGROUND = createKey("overworld/underground");
    public static final ResourceKey<DensityFunction> UNDERGROUND_NETHER = createKey("nether/underground");

    private static ResourceKey<DensityFunction> createVannilaKey(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(name));
    }

    private static ResourceKey<DensityFunction> createKey(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation(NonCave.MODID, name));
    }

    public static void bootstrap(BootstapContext<DensityFunction> context) {
        HolderGetter<NormalNoise.NoiseParameters> noiseHolderGetter = context.lookup(Registries.NOISE);
        HolderGetter<DensityFunction> density = context.lookup(Registries.DENSITY_FUNCTION);
        DensityFunction densityfunction = getFunction(density, SHIFT_X);
        DensityFunction densityfunction1 = getFunction(density, SHIFT_Z);

        Holder<DensityFunction> holder = density.getOrThrow(CONTINENTS);
        Holder<DensityFunction> holder1 = density.getOrThrow(EROSION);

        DensityFunction densityfunction3 = DensityFunctions.noise(noiseHolderGetter.getOrThrow(Noises.JAGGED), 1500.0D, 0.0D);
        registerTerrainNoises(context, density, densityfunction3, holder, holder1, OFFSET, FACTOR, JAGGEDNESS, DEPTH, SLOPED_CHEESE_NETHER, false);
        context.register(PILLARS, pillars(noiseHolderGetter));

        context.register(UNDERGROUND, ModNoiseRouterData.underground(density, noiseHolderGetter, getFunction(density, ModNoiseRouterData.SLOPED_CHEESE), 0.03F));
        context.register(UNDERGROUND_NETHER, ModNoiseRouterData.underground(density, noiseHolderGetter, getFunction(density, SLOPED_CHEESE_NETHER), 0.03F));

     }


    private static DensityFunction pillars(HolderGetter<NormalNoise.NoiseParameters> p_255985_) {
        double $$1 = 25.0;
        double $$2 = 0.3;
        DensityFunction $$3 = DensityFunctions.noise(p_255985_.getOrThrow(Noises.PILLAR), 13.0, 0.3);
        DensityFunction $$4 = DensityFunctions.mappedNoise(p_255985_.getOrThrow(Noises.PILLAR_RARENESS), -0.5, -3.0);
        DensityFunction $$5 = DensityFunctions.mappedNoise(p_255985_.getOrThrow(Noises.PILLAR_THICKNESS), 0.5, 2.0);
        DensityFunction $$6 = DensityFunctions.add(DensityFunctions.mul($$3, DensityFunctions.constant(2.0)), $$4);
        return DensityFunctions.cacheOnce(DensityFunctions.mul($$6, $$5.cube()));
    }

    private static void registerBiomeNoises(BootstapContext<NormalNoise.NoiseParameters> p_256503_, int p_236479_, ResourceKey<NormalNoise.NoiseParameters> p_236482_, ResourceKey<NormalNoise.NoiseParameters> p_236483_) {
        register(p_256503_, p_236482_, -9 + p_236479_, 1.0D, 1.0D, 2.0D, 2.0D, 2.0D, 1.0D, 1.0D, 1.0D, 1.0D);
        register(p_256503_, p_236483_, -9 + p_236479_, 1.0D, 1.0D, 0.0D, 1.0D, 1.0D);
    }

    private static DensityFunction splineWithBlending(DensityFunction p_224454_, DensityFunction p_224455_) {
        DensityFunction densityfunction = DensityFunctions.lerp(DensityFunctions.blendAlpha(), p_224455_, p_224454_);
        return DensityFunctions.flatCache(DensityFunctions.cache2d(densityfunction));
    }

    private static DensityFunction registerAndWrap(BootstapContext<DensityFunction> p_256149_, ResourceKey<DensityFunction> p_255905_, DensityFunction p_255856_) {
        return new DensityFunctions.HolderHolder(p_256149_.register(p_255905_, p_255856_));
    }

    private static void registerTerrainNoises(BootstapContext<DensityFunction> p_256336_, HolderGetter<DensityFunction> p_256393_, DensityFunction p_224476_, Holder<DensityFunction> p_224477_, Holder<DensityFunction> p_224478_, ResourceKey<DensityFunction> p_224479_, ResourceKey<DensityFunction> p_224480_, ResourceKey<DensityFunction> p_224481_, ResourceKey<DensityFunction> p_224482_, ResourceKey<DensityFunction> p_224483_, boolean p_224484_) {
        DensityFunctions.Spline.Coordinate densityfunctions$spline$coordinate = new DensityFunctions.Spline.Coordinate(p_224477_);
        DensityFunctions.Spline.Coordinate densityfunctions$spline$coordinate1 = new DensityFunctions.Spline.Coordinate(p_224478_);
        DensityFunctions.Spline.Coordinate densityfunctions$spline$coordinate2 = new DensityFunctions.Spline.Coordinate(p_256393_.getOrThrow(RIDGES));
        DensityFunctions.Spline.Coordinate densityfunctions$spline$coordinate3 = new DensityFunctions.Spline.Coordinate(p_256393_.getOrThrow(RIDGES_FOLDED));
        DensityFunction densityfunction = registerAndWrap(p_256336_, p_224479_, splineWithBlending(DensityFunctions.add(DensityFunctions.constant((double) -0.50375F), DensityFunctions.spline(TerrainProvider.overworldOffset(densityfunctions$spline$coordinate, densityfunctions$spline$coordinate1, densityfunctions$spline$coordinate3, p_224484_))), DensityFunctions.blendOffset()));
        DensityFunction densityfunction1 = registerAndWrap(p_256336_, p_224480_, splineWithBlending(DensityFunctions.spline(TerrainProvider.overworldFactor(densityfunctions$spline$coordinate, densityfunctions$spline$coordinate1, densityfunctions$spline$coordinate2, densityfunctions$spline$coordinate3, p_224484_)), BLENDING_FACTOR));
        DensityFunction densityfunction2 = registerAndWrap(p_256336_, p_224482_, DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5D, -1.5D), densityfunction));
        DensityFunction densityfunction3 = registerAndWrap(p_256336_, p_224481_, splineWithBlending(DensityFunctions.spline(TerrainProvider.overworldJaggedness(densityfunctions$spline$coordinate, densityfunctions$spline$coordinate1, densityfunctions$spline$coordinate2, densityfunctions$spline$coordinate3, p_224484_)), BLENDING_JAGGEDNESS));
        DensityFunction densityfunction4 = DensityFunctions.mul(densityfunction3, p_224476_.halfNegative());
        DensityFunction densityfunction5 = noiseGradientDensity(densityfunction1, DensityFunctions.add(densityfunction2, densityfunction4));
        p_256336_.register(p_224483_, DensityFunctions.add(densityfunction5, getFunction(p_256393_, BASE_3D_NOISE_NETHER)));
    }

    private static DensityFunction noiseGradientDensity(DensityFunction p_212272_, DensityFunction p_212273_) {
        DensityFunction densityfunction = DensityFunctions.mul(p_212273_, p_212272_);
        return DensityFunctions.mul(DensityFunctions.constant(4.0D), densityfunction.quarterNegative());
    }

    public static void register(BootstapContext<NormalNoise.NoiseParameters> ctx, ResourceKey<NormalNoise.NoiseParameters> key, int firstOctave, double firstAmplitude, double... amplitudes) {
        ctx.register(key, new NormalNoise.NoiseParameters(firstOctave, firstAmplitude, amplitudes));
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> p_256312_, ResourceKey<DensityFunction> p_256077_) {
        return new DensityFunctions.HolderHolder(p_256312_.getOrThrow(p_256077_));
    }

}