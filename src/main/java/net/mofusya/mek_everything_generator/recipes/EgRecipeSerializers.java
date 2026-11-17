package net.mofusya.mek_everything_generator.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.mofusya.mek_everything_generator.C;
import net.mofusya.mek_everything_generator.recipes.recipe.EverythingGeneratingRecipe;

public class EgRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, C.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, C.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EverythingGeneratingRecipe>> EVERYTHING_GENERATING = RECIPE_SERIALIZERS.register("everything_generating", () -> EverythingGeneratingRecipe.Serializer.INSTANCE);
    public static final DeferredHolder<RecipeType<?>, RecipeType<EverythingGeneratingRecipe>> EVERYTHING_GENERATING_TYPE = RECIPE_TYPES.register("everything_generating", () -> EverythingGeneratingRecipe.Type.INSTANCE);

    public static void register(IEventBus modEventBus) {
        RECIPE_SERIALIZERS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
    }
}
