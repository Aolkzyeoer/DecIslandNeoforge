package com.dec.decisland.client.renderer

import com.dec.decisland.client.model.ClothesModel
import net.minecraft.client.Minecraft
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

object MaskClientItemExtensions : IClientItemExtensions {
    private var cachedModel: ClothesModel<LivingEntity>? = null

    override fun getHumanoidArmorModel(
        livingEntity: LivingEntity,
        itemStack: ItemStack,
        equipmentSlot: EquipmentSlot,
        original: HumanoidModel<*>,
    ): HumanoidModel<*> {
        if (equipmentSlot != EquipmentSlot.HEAD) {
            return original
        }

        val model = cachedModel
        if (model != null) {
            return model
        }

        val baked = Minecraft.getInstance().entityModels.bakeLayer(ClothesModel.LAYER_LOCATION)
        return ClothesModel<LivingEntity>(baked).also {
            cachedModel = it
        }
    }
}
