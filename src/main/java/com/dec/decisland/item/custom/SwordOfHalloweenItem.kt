package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.custom.PumpkinBombEntity
import com.dec.decisland.network.Networking
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class SwordOfHalloweenItem(properties: Properties) : Item(properties) {
    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        val serverLevel = attacker.level() as? ServerLevel ?: return false
        if (serverLevel.random.nextInt(PROC_CHANCE) == 0) {
            serverLevel.addFreshEntity(PumpkinBombEntity(serverLevel, target.x, target.y, target.z))
            Networking.sendBedrockEmitterToNearby(
                serverLevel,
                BAT_SPURT_PARTICLE_ID,
                target.position().add(0.0, 0.5, 0.0),
            )
        }
        return true
    }

    companion object {
        private const val PROC_CHANCE = 5
        private val BAT_SPURT_PARTICLE_ID: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "bat_spurt_particle")
    }
}