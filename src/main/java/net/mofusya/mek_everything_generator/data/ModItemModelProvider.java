package net.mofusya.mek_everything_generator.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.mofusya.mek_everything_generator.C;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, C.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ArrayList<DeferredHolder<Item, ? extends Item>> registries = new ArrayList<>();

        for (DeferredHolder<Item, ? extends Item> item : registries) {
            this.simpleItem(item);
        }
    }

    private void simpleItem(DeferredHolder<Item, ? extends Item> item) {
        this.withExistingParent(item.getId().getPath(),
                        ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(C.MOD_ID, "item/" + item.getId().getPath()));
    }

    private void layeredSimpleItem(DeferredHolder<Item, ? extends Item> item, @Nullable String... textures) {
        var model = this.withExistingParent(item.getId().getPath(), ResourceLocation.withDefaultNamespace("item/generated"));
        for (int i = 0; i < textures.length; i++) {
            model.texture("layer" + i, ResourceLocation.fromNamespaceAndPath(C.MOD_ID, "item/" + (textures[i] == null ? item.getId().getPath() : textures[i])));
        }
    }

    private void simpleItemWithSublayer(DeferredHolder<Item, ? extends Item> item, String prefix) {
        this.layeredSimpleItem(item, item.getId().getPath(), item.getId().getPath() + prefix);
    }

    private static String getPath(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }
}
