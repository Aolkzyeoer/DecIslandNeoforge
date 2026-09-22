package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.item.compat.ItemCompat
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.Random

class IllagerSword(properties: Properties) : Katana(properties) {
    override fun getUseSkillRadius(): Float = 1.4f

    override fun getUseSkillBreakAmount(): Int = 1

    override fun getUseSkillBonusDamage(): Float = 5.0f

    override fun getAttackSkillRadius(): Float = 1.4f

    override fun getAttackSkillBreakAmount(): Int = 1

    override fun getAttackSkillBonusDamage(): Float = 5.0f

    override fun useSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, ILLAGER_SWORD_PARTICLE_ID, x, y, z)
    }

    override fun attackSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, ILLAGER_SWORD_PARTICLE_ID, x, y, z)
    }

    override fun onAttackTriggerSweep(stack: ItemStack): Boolean = Random().nextInt(8) == 0

    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
        super.inventoryTick(stack, level, entity, slotId, isSelected)
        updateMovementSpeedModifier(
            entity,
            ItemCompat.slotFromIndex(slotId),
            MOVEMENT_SPEED_ADDITION,
            MOVEMENT_SPEED_MODIFIER_ID,
        )
    }

    companion object {
        private val ILLAGER_SWORD_PARTICLE_ID: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "illager_sword_particle")
        private val MOVEMENT_SPEED_MODIFIER_ID: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "movement_speed/illager_sword")
        private const val MOVEMENT_SPEED_ADDITION: Double = 0.015
    }
}
