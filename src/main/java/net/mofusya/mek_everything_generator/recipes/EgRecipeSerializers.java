package net.mofusya.mek_everything_generator.recipes;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mofusya.mek_everything_generator.C;
import net.mofusya.mek_everything_generator.recipes.recipe.EverythingGeneratingRecipe;

public class EgRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, C.MOD_ID);

    public static final RegistryObject<RecipeSerializer<EverythingGeneratingRecipe>> EVERYTHING_GENERATING = RECIPE_SERIALIZERS.register("everything_generating", () -> EverythingGeneratingRecipe.Serializer.INSTANCE);
}
