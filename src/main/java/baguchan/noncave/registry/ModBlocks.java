package baguchan.noncave.registry;

import baguchan.noncave.NonCave;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, NonCave.MODID);

    public static final DeferredHolder<Block, Block> MIDSHALE = register("midshale", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.DRIPSTONE_BLOCK)));


    private static <T extends Block> DeferredHolder<Block, T> baseRegister(String name, Supplier<? extends T> block, Function<Supplier<T>, Supplier<? extends Item>> item) {
        DeferredHolder<Block, T> register = BLOCKS.register(name, block);
        Supplier<? extends Item> itemSupplier = item.apply(register);
        ModItems.ITEMS.register(name, itemSupplier);
        return register;
    }

    private static <T extends Block> Supplier<T> noItemRegister(String name, Supplier<? extends T> block) {
        Supplier<T> register = BLOCKS.register(name, block);
        return register;
    }

    private static <B extends Block> DeferredHolder<Block, B> register(String name, Supplier<? extends Block> block) {
        return (DeferredHolder<Block, B>) baseRegister(name, block, (object) -> ModBlocks.registerBlockItem(object));
    }

    private static <T extends Block> Supplier<BlockItem> registerBlockItem(final Supplier<T> block) {
        return () -> {
            return new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties());
        };
    }
}
