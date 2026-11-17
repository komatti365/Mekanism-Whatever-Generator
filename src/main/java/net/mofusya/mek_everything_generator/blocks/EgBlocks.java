package net.mofusya.mek_everything_generator.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.mofusya.mek_everything_generator.C;
import net.mofusya.mek_everything_generator.blocks.block.EverythingGeneratorBlock;

import java.util.Collection;
import java.util.function.Function;

public class EgBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(C.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(C.MOD_ID);

    public static final DeferredBlock<Block> EVERYTHING_GENERATOR = registerBlockWithItem(
            "everything_generator",
            EverythingGeneratorBlock::new,
            Blocks.IRON_BLOCK
    );

    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, T> blockFactory, Block template) {
        DeferredBlock<T> block = BLOCKS.registerBlock(name, blockFactory, BlockBehaviour.Properties.ofFullCopy(template));
        ITEMS.registerSimpleBlockItem(name, block);
        return block;
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }

    public static Collection<DeferredHolder<Item, ? extends Item>> getItems() {
        return ITEMS.getEntries();
    }
}
