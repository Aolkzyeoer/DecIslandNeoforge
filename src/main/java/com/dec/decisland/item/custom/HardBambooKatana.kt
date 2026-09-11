package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel

class HardBambooKatana(properties: Properties) : BambooKatana(properties) {
    override fun getMaxAttackCount(): Int = 6

    override fun useSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, HARD_BAMBOO_KATANA_PARTICLE_ID, x, y, z)
    }

    override fun attackSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, HARD_BAMBOO_KATANA_PARTICLE_ID, x, y, z)
    }

    companion object {
        private val HARD_BAMBOO_KATANA_PARTICLE_ID: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "hard_bamboo_katana_particle")
    }
}
