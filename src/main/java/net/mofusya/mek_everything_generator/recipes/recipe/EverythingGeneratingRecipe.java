package net.mofusya.mek_everything_generator.recipes.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.mofusya.mek_everything_generator.C;
import net.mofusya.mek_everything_generator.recipes.SimpleItemGasFluidRLContainer;

import javax.annotation.Nullable;
import java.util.Optional;

public class EverythingGeneratingRecipe implements Recipe<SimpleItemGasFluidRLContainer> {

    @Nullable
    private final Ingredient ingredient;
    @Nullable
    private final ResourceLocation fluidIngredient;
    @Nullable
    private final ResourceLocation gasIngredient;
    private final int scrapResult;

    public EverythingGeneratingRecipe(@Nullable Ingredient ingredient, @Nullable ResourceLocation fluidIngredient, @Nullable ResourceLocation gasIngredient, int scrapResult) {
        this.ingredient = ingredient;
        this.fluidIngredient = fluidIngredient;
        this.gasIngredient = gasIngredient;
        this.scrapResult = scrapResult;
    }

    @Override
    public boolean matches(SimpleItemGasFluidRLContainer container, Level level) {
        return (this.getIngredient() != null && this.getIngredient().test(container.getItem(0))) ||
                (this.getFluidIngredient() != null && container.getFluidId() != null && this.getFluidIngredient().equals(container.getFluidId())) ||
                (this.getGasIngredient() != null && container.getGasId() != null && this.getGasIngredient().equals(container.getGasId()));
    }

    @Override
    public ItemStack assemble(SimpleItemGasFluidRLContainer container, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Nullable
    public Ingredient getIngredient() {
        return ingredient;
    }

    @Nullable
    public ResourceLocation getFluidIngredient() {
        return fluidIngredient;
    }

    @Nullable
    public ResourceLocation getGasIngredient() {
        return gasIngredient;
    }

    public int getScrapResult() {
        return scrapResult;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public enum Type implements RecipeType<EverythingGeneratingRecipe> {
        INSTANCE;

        @Override
        public String toString() {
            return C.MOD_ID + ":everything_generating";
        }
    }

    public enum Serializer implements RecipeSerializer<EverythingGeneratingRecipe> {
        INSTANCE;

        public static final MapCodec<EverythingGeneratingRecipe> CODEC = RecordCodecBuilder.<EverythingGeneratingRecipe>mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.optionalFieldOf(C.ITEM_INGREDIENT).forGetter(recipe -> Optional.ofNullable(recipe.getIngredient())),
                ResourceLocation.CODEC.optionalFieldOf(C.FLUID_INGREDIENT).forGetter(recipe -> Optional.ofNullable(recipe.getFluidIngredient())),
                ResourceLocation.CODEC.optionalFieldOf(C.GAS_INGREDIENT).forGetter(recipe -> Optional.ofNullable(recipe.getGasIngredient())),
                Codec.INT.fieldOf(C.SCRAP_RESULT).forGetter(EverythingGeneratingRecipe::getScrapResult)
        ).apply(instance, (item, fluid, gas, scrap) -> new EverythingGeneratingRecipe(
                item.orElse(null),
                fluid.orElse(null),
                gas.orElse(null),
                scrap
        ))).validate(recipe -> {
            int count = (recipe.getIngredient() != null ? 1 : 0)
                    + (recipe.getFluidIngredient() != null ? 1 : 0)
                    + (recipe.getGasIngredient() != null ? 1 : 0);
            if (count != 1) {
                return DataResult.error(() -> "Recipe \"" + C.MOD_ID + ":everything_generating\" has multiple, or too few ingredients and is illegal.");
            }
            return DataResult.success(recipe);
        });

        public static final StreamCodec<RegistryFriendlyByteBuf, EverythingGeneratingRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), recipe -> Optional.ofNullable(recipe.getIngredient()),
                ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> Optional.ofNullable(recipe.getFluidIngredient()),
                ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> Optional.ofNullable(recipe.getGasIngredient()),
                ByteBufCodecs.VAR_INT, EverythingGeneratingRecipe::getScrapResult,
                (item, fluid, gas, scrap) -> new EverythingGeneratingRecipe(
                        item.orElse(null),
                        fluid.orElse(null),
                        gas.orElse(null),
                        scrap
                )
        );

        @Override
        public MapCodec<EverythingGeneratingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EverythingGeneratingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
