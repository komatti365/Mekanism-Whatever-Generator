package net.mofusya.mek_everything_generator.blocks.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.mofusya.mek_everything_generator.blocks.EgBlockEntityTypes;
import net.mofusya.mek_everything_generator.blocks.entity.EverythingGeneratorBlockEntity;
import org.jetbrains.annotations.Nullable;

public class EverythingGeneratorBlock extends BaseEntityBlock {
    public EverythingGeneratorBlock(Properties build) {
        super(build);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;

        return createTickerHelper(type, EgBlockEntityTypes.EVERYTHING_GENERATOR.get(),
                (pLevel, pPos, pState, pBlockEntity) -> pBlockEntity.tick(pLevel, pPos, pState));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EverythingGeneratorBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
