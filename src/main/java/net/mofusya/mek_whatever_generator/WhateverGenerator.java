package net.mofusya.mek_whatever_generator;

import com.mojang.logging.LogUtils;
import mekanism.common.registries.MekanismCreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.mofusya.mek_whatever_generator.blocks.WgBlockEntityTypes;
import net.mofusya.mek_whatever_generator.blocks.WgBlocks;
import net.mofusya.mek_whatever_generator.blocks.entity.WhateverGeneratorBlockEntity;
import net.mofusya.mek_whatever_generator.config.WgJsonConfigs;
import org.slf4j.Logger;

@Mod(WhateverGenerator.MOD_ID)
public class WhateverGenerator
{
    public static final String MOD_ID = "mek_whatever_generator";
    private static final Logger LOGGER = LogUtils.getLogger();

    public WhateverGenerator()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        WgBlocks.BLOCKS.register(modEventBus);
        WgBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        WgJsonConfigs.MAIN_CONFIG.load();

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == MekanismCreativeTabs.MEKANISM.key()){
            WgBlocks.BLOCKS.getItems().forEach(event::accept);
        }
    }
}
