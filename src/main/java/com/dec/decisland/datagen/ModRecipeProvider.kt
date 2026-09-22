package com.dec.decisland.datagen

import com.dec.decisland.datagen.RecipeProvider.RecipeContext
import com.dec.decisland.datagen.RecipeProvider.recipe.CookingRecipes
import com.dec.decisland.datagen.RecipeProvider.recipe.FishRecipes
import com.dec.decisland.datagen.RecipeProvider.recipe.FoodRecipes
import com.dec.decisland.datagen.RecipeProvider.recipe.MaterialRecipes
import com.dec.decisland.datagen.RecipeProvider.recipe.SummonItemRecipes
import com.dec.decisland.datagen.RecipeProvider.recipe.ToolRecipes
import com.dec.decisland.datagen.RecipeProvider.recipe.WeaponRecipes
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.concurrent.CompletableFuture

// 1.21.1 的 RecipeProvider 没有 Runner 内部类，直接继承并在 buildRecipes(RecipeOutput) 中生成。
// getName() 在 1.21.1 中是 final，不可覆写。
class ModRecipeProvider(
    output: PackOutput,
    private val lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : RecipeProvider(output, lookupProvider) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        // buildRecipes 由 registries.thenCompose 调用，此时 future 已完成，join() 不会阻塞。
        val context = RecipeContext(lookupProvider.join(), recipeOutput)
        MaterialRecipes.build(context)
        FoodRecipes.build(context)
        FishRecipes.build(context)
        WeaponRecipes.build(context)
        ToolRecipes.build(context)
        SummonItemRecipes.build(context)
        CookingRecipes.build(context)
    }
}
