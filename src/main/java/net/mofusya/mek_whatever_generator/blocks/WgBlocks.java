package net.mofusya.mek_whatever_generator.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.mek_whatever_generator.C;
import net.mofusya.mek_whatever_generator.blocks.block.WhateverGeneratorBlock;
import net.mofusya.ornatelib.registries.OrnateBlockDeferredRegister;

public class WgBlocks {

    public static final OrnateBlockDeferredRegister BLOCKS = OrnateBlockDeferredRegister.create(C.MOD_ID);

    public static final RegistryObject<Block> WHATEVER_GENERATOR = BLOCKS.register("whatever_generator", new OrnateBlockDeferredRegister.Builder()
            .blockFunc(WhateverGeneratorBlock::new)
            .blockBuildRef(Blocks.IRON_BLOCK));
}
