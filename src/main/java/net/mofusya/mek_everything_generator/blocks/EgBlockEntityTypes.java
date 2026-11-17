package net.mofusya.mek_everything_generator.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.mofusya.mek_everything_generator.C;
import net.mofusya.mek_everything_generator.blocks.entity.EverythingGeneratorBlockEntity;

public class EgBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, C.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EverythingGeneratorBlockEntity>> EVERYTHING_GENERATOR = BLOCK_ENTITY_TYPES.register("everything_generator",
            () -> BlockEntityType.Builder.of(EverythingGeneratorBlockEntity::new, EgBlocks.EVERYTHING_GENERATOR.get()).build(null));
}
