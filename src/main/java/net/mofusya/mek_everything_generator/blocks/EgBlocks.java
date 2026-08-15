package net.mofusya.mek_everything_generator.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.mek_everything_generator.C;
import net.mofusya.mek_everything_generator.blocks.block.EverythingGeneratorBlock;
import net.mofusya.ornatelib.registries.OrnateBlockDeferredRegister;

public class EgBlocks {

    public static final OrnateBlockDeferredRegister BLOCKS = OrnateBlockDeferredRegister.create(C.MOD_ID);

    public static final RegistryObject<Block> EVERYTHING_GENERATOR = BLOCKS.register("everything_generator", new OrnateBlockDeferredRegister.Builder()
            .blockFunc(EverythingGeneratorBlock::new)
            .blockBuildRef(Blocks.IRON_BLOCK));
}
