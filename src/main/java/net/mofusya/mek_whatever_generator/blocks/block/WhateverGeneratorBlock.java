package net.mofusya.mek_whatever_generator.blocks.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.mofusya.mek_whatever_generator.blocks.WgBlockEntityTypes;
import net.mofusya.mek_whatever_generator.blocks.entity.WhateverGeneratorBlockEntity;
import org.jetbrains.annotations.Nullable;

public class WhateverGeneratorBlock extends BaseEntityBlock {
    public WhateverGeneratorBlock(Properties build) {
        super(build);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;

        return createTickerHelper(type, WgBlockEntityTypes.WHATEVER_GENERATOR.get(),
                (pLevel, pPos, pState, pBlockEntity) -> pBlockEntity.tick(pLevel, pPos, pState));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WhateverGeneratorBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
