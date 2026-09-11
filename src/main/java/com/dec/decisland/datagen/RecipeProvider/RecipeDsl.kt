package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder
import net.minecraft.data.recipes.SpecialRecipeBuilder
import net.minecraft.resources.ResourceLocation

object RecipeDsl {
    fun save(
        context: RecipeContext,
        config: RecipeConfig,
    ) {
        when (config) {
            is ShapedRecipeConfig -> saveShaped(context, config)
            is ShapelessRecipeConfig -> saveShapeless(context, config)
            is CookingRecipeConfig -> saveCooking(context, config)
            is StonecuttingRecipeConfig -> saveStonecutting(context, config)
            is SmithingTransformRecipeConfig -> saveSmithingTransform(context, config)
            is SmithingTrimRecipeConfig -> saveSmithingTrim(context, config)
            is SpecialRecipeConfig -> saveSpecial(context, config)
            else -> throw IllegalArgumentException("Unsupported recipe config: ${config.name}")
        }
    }

    private fun saveShaped(
        context: RecipeContext,
        config: ShapedRecipeConfig,
    ) {
        val builder = ShapedRecipeBuilder.shaped(config.category, config.result, config.count)
        config.pattern.forEach(builder::pattern)
        config.keys.forEach { (key, ingredient) ->
            builder.define(key, ingredient.toIngredient(context.items))
        }
        applyCommon(builder, context, config)
    }

    private fun saveShapeless(
        context: RecipeContext,
        config: ShapelessRecipeConfig,
    ) {
        val builder = ShapelessRecipeBuilder.shapeless(config.category, config.result, config.count)
        config.ingredients.forEach { entry ->
            builder.requires(entry.ingredient.toIngredient(context.items), entry.count)
        }
        applyCommon(builder, context, config)
    }

    private fun saveCooking(
        context: RecipeContext,
        config: CookingRecipeConfig,
    ) {
        val ingredient = config.ingredient.toIngredient(context.items)
        val builder = when (config.type) {
            CookingRecipeConfig.Type.SMELTING ->
                SimpleCookingRecipeBuilder.smelting(ingredient, config.category, config.result, config.experience, config.cookingTime)
            CookingRecipeConfig.Type.BLASTING ->
                SimpleCookingRecipeBuilder.blasting(ingredient, config.category, config.result, config.experience, config.cookingTime)
            CookingRecipeConfig.Type.SMOKING ->
                SimpleCookingRecipeBuilder.smoking(ingredient, config.category, config.result, config.experience, config.cookingTime)
            CookingRecipeConfig.Type.CAMPFIRE ->
                SimpleCookingRecipeBuilder.campfireCooking(ingredient, config.category, config.result, config.experience, config.cookingTime)
        }
        applyCommon(builder, context, config)
    }

    private fun saveStonecutting(
        context: RecipeContext,
        config: StonecuttingRecipeConfig,
    ) {
        val builder = SingleItemRecipeBuilder.stonecutting(
            config.ingredient.toIngredient(context.items),
            config.category,
            config.result,
            config.count,
        )
        applyCommon(builder, context, config)
    }

    private fun saveSmithingTransform(
        context: RecipeContext,
        config: SmithingTransformRecipeConfig,
    ) {
        val builder = SmithingTransformRecipeBuilder.smithing(
            config.template.toIngredient(context.items),
            config.base.toIngredient(context.items),
            config.addition.toIngredient(context.items),
            config.category,
            config.result,
        )
        config.unlockCriteria.forEach { unlock ->
            builder.unlocks(unlock.name, unlock.build(context.items))
        }
        builder.save(context.output, recipeKey(config))
    }

    private fun saveSmithingTrim(
        context: RecipeContext,
        config: SmithingTrimRecipeConfig,
    ) {
        // 1.21.1 的锻造模板纹样在运行时由模板物品推导，datagen 无需指定 trimPattern。
        val builder = SmithingTrimRecipeBuilder.smithingTrim(
            config.template.toIngredient(context.items),
            config.base.toIngredient(context.items),
            config.addition.toIngredient(context.items),
            config.category,
        )
        config.unlockCriteria.forEach { unlock ->
            builder.unlocks(unlock.name, unlock.build(context.items))
        }
        builder.save(context.output, recipeKey(config))
    }

    private fun saveSpecial(
        context: RecipeContext,
        config: SpecialRecipeConfig,
    ) {
        SpecialRecipeBuilder.special(config.factory).save(context.output, recipeKey(config))
    }

    private fun applyCommon(
        builder: RecipeBuilder,
        context: RecipeContext,
        config: RecipeConfig,
    ) {
        if (config.group != null) {
            builder.group(config.group)
        }
        config.unlockCriteria.forEach { unlock ->
            builder.unlockedBy(unlock.name, unlock.build(context.items))
        }
        builder.save(context.output, recipeKey(config))
    }

    private fun recipeKey(config: RecipeConfig): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath("decisland", config.name)
}
