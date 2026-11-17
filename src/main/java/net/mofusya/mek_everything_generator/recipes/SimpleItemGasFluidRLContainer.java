package net.mofusya.mek_everything_generator.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import javax.annotation.Nullable;

public class SimpleItemGasFluidRLContainer implements RecipeInput {
    private final ItemStack itemStack;
    @Nullable
    private final ResourceLocation fluidId;
    @Nullable
    private final ResourceLocation gasId;

    public SimpleItemGasFluidRLContainer(@Nullable ItemStack itemStack, @Nullable ResourceLocation fluidId, @Nullable ResourceLocation gasId) {
        this.itemStack = itemStack == null ? ItemStack.EMPTY : itemStack;
        this.fluidId = fluidId;
        this.gasId = gasId;
    }

    @Override
    public ItemStack getItem(int index) {
        return index == 0 ? this.itemStack : ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.itemStack.isEmpty() && this.fluidId == null && this.gasId == null;
    }

    @Nullable
    public ResourceLocation getFluidId() {
        return fluidId;
    }

    @Nullable
    public ResourceLocation getGasId() {
        return gasId;
    }
}
