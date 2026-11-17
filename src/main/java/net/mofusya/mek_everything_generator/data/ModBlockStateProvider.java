package net.mofusya.mek_everything_generator.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.mofusya.mek_everything_generator.C;

import java.util.ArrayList;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, C.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ArrayList<DeferredHolder<Block, ? extends Block>> registries = new ArrayList<>();

        for (DeferredHolder<Block, ? extends Block> block : registries) {
            this.blockWithItem(block);
        }
    }

    private void blockWithItem(DeferredHolder<Block, ? extends Block> block) {
        this.simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
}
