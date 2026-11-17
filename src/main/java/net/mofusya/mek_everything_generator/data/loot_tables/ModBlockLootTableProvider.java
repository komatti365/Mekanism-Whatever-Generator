package net.mofusya.mek_everything_generator.data.loot_tables;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        ArrayList<DeferredHolder<Block, ? extends Block>> registries = new ArrayList<>();


        for (DeferredHolder<Block, ? extends Block> block : registries) {
            this.dropSelf(block.get());
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        ArrayList<DeferredHolder<Block, ? extends Block>> blocks = new ArrayList<>();


        return blocks.stream().<Block>map(DeferredHolder::get).toList();
    }
}
