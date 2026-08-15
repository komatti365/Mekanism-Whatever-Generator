package net.mofusya.mek_everything_generator;

import com.mojang.logging.LogUtils;
import mekanism.common.registries.MekanismCreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.mofusya.mek_everything_generator.blocks.EgBlockEntityTypes;
import net.mofusya.mek_everything_generator.blocks.EgBlocks;
import net.mofusya.mek_everything_generator.config.EgJsonConfigs;
import net.mofusya.mek_everything_generator.items.EgCreativeTabs;
import net.mofusya.mek_everything_generator.recipes.EgRecipeSerializers;
import org.slf4j.Logger;

@Mod(EverythingGenerator.MOD_ID)
public class EverythingGenerator
{
    public static final String MOD_ID = "mek_everything_generator";
    private static final Logger LOGGER = LogUtils.getLogger();

    public EverythingGenerator()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EgBlocks.BLOCKS.register(modEventBus);
        EgBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        EgJsonConfigs.MAIN_CONFIG.load();
        EgRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        EgCreativeTabs.CREATIVE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == MekanismCreativeTabs.MEKANISM.key()){
            EgBlocks.BLOCKS.getItems().forEach(event::accept);
        }
    }
}
