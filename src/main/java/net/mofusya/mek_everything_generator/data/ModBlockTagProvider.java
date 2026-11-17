package net.mofusya.mek_everything_generator.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.mofusya.mek_everything_generator.C;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, C.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        {
            ArrayList<DeferredHolder<Block, ? extends Block>> registries = new ArrayList<>();


            var tags = this.tag(BlockTags.MINEABLE_WITH_PICKAXE);
            for (DeferredHolder<Block, ? extends Block> block : registries) {
                tags.add(block.get());
            }
        }

        {
            ArrayList<DeferredHolder<Block, ? extends Block>> registries = new ArrayList<>();


            var tags = this.tag(BlockTags.NEEDS_IRON_TOOL);
            for (DeferredHolder<Block, ? extends Block> block : registries) {
                tags.add(block.get());
            }
        }
    }
}
