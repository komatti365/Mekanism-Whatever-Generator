package net.mofusya.mek_everything_generator.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class SimpleItemGasFluidRLContainer extends SimpleContainer {
    @Nullable
    private final ResourceLocation fluidId;
    @Nullable
    private final ResourceLocation gasId;

    public SimpleItemGasFluidRLContainer(@Nullable ItemStack itemStack, @Nullable ResourceLocation fluidId, @Nullable ResourceLocation gasId) {
        super(itemStack == null ? ItemStack.EMPTY : itemStack);
        this.fluidId = fluidId;
        this.gasId = gasId;
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
