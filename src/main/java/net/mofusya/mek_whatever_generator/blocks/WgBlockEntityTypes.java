package net.mofusya.mek_whatever_generator.blocks;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.mek_whatever_generator.C;
import net.mofusya.mek_whatever_generator.blocks.entity.WhateverGeneratorBlockEntity;

public class WgBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, C.MOD_ID);

    public static final RegistryObject<BlockEntityType<WhateverGeneratorBlockEntity>> WHATEVER_GENERATOR = BLOCK_ENTITY_TYPES.register("whatever_generator",
            () -> BlockEntityType.Builder.of(WhateverGeneratorBlockEntity::new, WgBlocks.WHATEVER_GENERATOR.get()).build(null));
}
