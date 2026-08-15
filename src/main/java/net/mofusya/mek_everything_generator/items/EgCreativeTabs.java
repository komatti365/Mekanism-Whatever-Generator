package net.mofusya.mek_everything_generator.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.mek_everything_generator.C;
import net.mofusya.mek_everything_generator.blocks.EgBlocks;

public class EgCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, C.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("title.mek_everything_generator"))
            .icon(() -> new ItemStack(EgBlocks.EVERYTHING_GENERATOR.get()))
            .displayItems((parameter, output) -> {
                EgBlocks.BLOCKS.getItems().stream().map(RegistryObject::get).forEach(output::accept);
            })
            .build());
}
