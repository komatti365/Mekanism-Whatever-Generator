package net.mofusya.mek_everything_generator.blocks;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.mek_everything_generator.C;
import net.mofusya.mek_everything_generator.blocks.entity.EverythingGeneratorBlockEntity;

public class EgBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, C.MOD_ID);

    public static final RegistryObject<BlockEntityType<EverythingGeneratorBlockEntity>> EVERYTHING_GENERATOR = BLOCK_ENTITY_TYPES.register("everything_generator",
            () -> BlockEntityType.Builder.of(EverythingGeneratorBlockEntity::new, EgBlocks.EVERYTHING_GENERATOR.get()).build(null));
}
