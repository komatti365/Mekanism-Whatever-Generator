package net.mofusya.mek_everything_generator;

import com.mojang.logging.LogUtils;
import mekanism.common.registries.MekanismCreativeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.mofusya.mek_everything_generator.blocks.EgBlockEntityTypes;
import net.mofusya.mek_everything_generator.blocks.EgBlocks;
import net.mofusya.mek_everything_generator.blocks.entity.EverythingGeneratorBlockEntity;
import net.mofusya.mek_everything_generator.config.EgJsonConfigs;
import net.mofusya.mek_everything_generator.items.EgCreativeTabs;
import net.mofusya.mek_everything_generator.recipes.EgRecipeSerializers;
import org.slf4j.Logger;

@Mod(EverythingGenerator.MOD_ID)
public class EverythingGenerator
{
    public static final String MOD_ID = "mek_everything_generator";
    private static final Logger LOGGER = LogUtils.getLogger();

    public EverythingGenerator(IEventBus modEventBus, ModContainer modContainer)
    {
        EgBlocks.register(modEventBus);
        EgBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        EgJsonConfigs.MAIN_CONFIG.load();
        EgRecipeSerializers.register(modEventBus);
        EgCreativeTabs.CREATIVE_TABS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(EverythingGeneratorBlockEntity::registerCapabilities);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == MekanismCreativeTabs.MEKANISM.getKey()){
            EgBlocks.getItems().forEach(item -> event.accept(item.get()));
        }
    }
}
