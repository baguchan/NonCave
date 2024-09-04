package baguchan.noncave;

import baguchan.noncave.registry.ModBlocks;
import baguchan.noncave.registry.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NonCave.MODID)
public class NonCave {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "noncave";

    public NonCave(ModContainer modContainer, IEventBus modEventBus) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(ModBlocks.MIDSHALE.get());
    }

}
