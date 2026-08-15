package net.mofusya.mek_everything_generator.recipes.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.mofusya.mek_everything_generator.C;
import net.mofusya.mek_everything_generator.recipes.SimpleItemGasFluidRLContainer;

import javax.annotation.Nullable;

public class EverythingGeneratingRecipe implements Recipe<SimpleItemGasFluidRLContainer> {

    private final ResourceLocation id;
    @Nullable
    private final Ingredient ingredient;
    @Nullable
    private final ResourceLocation fluidIngredient;
    @Nullable
    private final ResourceLocation gasIngredient;
    private final int scrapResult;

    public EverythingGeneratingRecipe(ResourceLocation id, @Nullable Ingredient ingredient, @Nullable ResourceLocation fluidIngredient, @Nullable ResourceLocation gasIngredient, int scrapResult) {
        this.id = id;
        this.ingredient = ingredient;
        this.fluidIngredient = fluidIngredient;
        this.gasIngredient = gasIngredient;
        this.scrapResult = scrapResult;
    }

    @Override
    public boolean matches(SimpleItemGasFluidRLContainer container, Level level) {
        return (this.getIngredient() != null && this.getIngredient().test(container.getItem(0)) ||
                (this.getFluidIngredient() != null && container.getFluidId() != null && this.getFluidIngredient().equals(container.getFluidId())) ||
                (this.getGasIngredient() != null && container.getGasId() != null && this.getGasIngredient().equals(container.getGasId()))
        );
    }

    @Override
    public ItemStack assemble(SimpleItemGasFluidRLContainer p_44001_, RegistryAccess p_267165_) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
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
    }

    public enum Serializer implements RecipeSerializer<EverythingGeneratingRecipe> {
        INSTANCE;

        private static boolean isNullLike(ResourceLocation resourceLocation) {
            return resourceLocation.getNamespace().equals(C.MOD_ID) && resourceLocation.getPath().equals(C.NULL);
        }

        private static ResourceLocation getNullLike() {
            return new ResourceLocation(C.MOD_ID, C.NULL);
        }

        private static void checkOnlyOneNotNull(String errMessage, Object... objects) {
            int nullCount = 0;
            for (Object object : objects) {
                if (object != null) nullCount++;
            }

            if (nullCount != 1) {
                throw new IllegalStateException(errMessage);
            }
        }

        @Override
        public EverythingGeneratingRecipe fromJson(ResourceLocation id, JsonObject json) {
            @Nullable
            JsonObject itemIngredientObject = json.getAsJsonObject(C.ITEM_INGREDIENT);
            @Nullable
            JsonPrimitive fluidIngredientObject = json.getAsJsonPrimitive(C.FLUID_INGREDIENT);
            @Nullable
            JsonPrimitive gasIngredientObject = json.getAsJsonPrimitive(C.GAS_INGREDIENT);
            int scrapResult = GsonHelper.getAsInt(json, C.SCRAP_RESULT);

            checkOnlyOneNotNull("Recipe \"mek_whatever_generator:whatever_generating\"'s file \"" + id + "\" has multiple, or too less ingredients and is illegal. Thank you. :)", itemIngredientObject, fluidIngredientObject, gasIngredientObject);

            return new EverythingGeneratingRecipe(id,
                    itemIngredientObject == null ? null : Ingredient.fromJson(itemIngredientObject),
                    fluidIngredientObject == null ? null : new ResourceLocation(fluidIngredientObject.getAsString()),
                    gasIngredientObject == null ? null : new ResourceLocation(gasIngredientObject.getAsString()),
                    scrapResult
            );
        }

        @Override
        public EverythingGeneratingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient itemIngredient = Ingredient.fromNetwork(buf);
            if (itemIngredient.isEmpty()) itemIngredient = null;
            ResourceLocation fluidIngredient = buf.readResourceLocation();
            if (isNullLike(fluidIngredient)) fluidIngredient = null;
            ResourceLocation gasIngredient = buf.readResourceLocation();
            if (isNullLike(gasIngredient)) gasIngredient = null;
            int scrapResult = buf.readInt();

            return new EverythingGeneratingRecipe(id, itemIngredient, fluidIngredient, gasIngredient, scrapResult);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, EverythingGeneratingRecipe recipe) {
            if (recipe.getIngredient() == null) {
                Ingredient.EMPTY.toNetwork(buf);
            } else {
                recipe.getIngredient().toNetwork(buf);
            }

            if (recipe.getFluidIngredient() == null) {
                buf.writeResourceLocation(getNullLike());
            } else {
                buf.writeResourceLocation(recipe.getFluidIngredient());
            }

            if (recipe.getGasIngredient() == null) {
                buf.writeResourceLocation(getNullLike());
            } else {
                buf.writeResourceLocation(recipe.getGasIngredient());
            }

            buf.writeInt(recipe.getScrapResult());
        }
    }
}