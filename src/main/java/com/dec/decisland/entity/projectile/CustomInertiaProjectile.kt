package com.dec.decisland.entity.projectile

import com.dec.decisland.api.CustomInertia
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.level.Level

/**
 * 可配置惯性的投掷物基类（1.21.1 下替代原 ThrowableProjectileMixin）。
 *
 * 1.21.1 的 ThrowableProjectile.tick() 会先按原版惯性
 * （空气 0.99 / 水 0.8）缩放速度，然后立即调用 applyGravity()，
 * 因此在这里把刚缩放过的速度重新缩放到配置值即可精确生效，无需 mixin。
 */
abstract class CustomInertiaProjectile(
    entityType: EntityType<out ThrowableItemProjectile>,
    level: Level,
) : ThrowableItemProjectile(entityType, level), CustomInertia {

    override fun applyGravity() {
        val inWater = isInWater()
        val vanillaInertia = if (inWater) VANILLA_WATER_INERTIA else VANILLA_AIR_INERTIA
        val configuredInertia = if (inWater) waterInertia else airInertia
        val desiredInertia = resolveInertia(configuredInertia, vanillaInertia)
        if (desiredInertia != vanillaInertia) {
            val scale = desiredInertia / vanillaInertia
            val motion = deltaMovement
            setDeltaMovement(motion.x * scale, motion.y * scale, motion.z * scale)
        }
        super.applyGravity()
    }

    companion object {
        private const val VANILLA_AIR_INERTIA = 0.99f
        private const val VANILLA_WATER_INERTIA = 0.8f

        /**
         * 基岩版投掷物惯性值大于 1 表示"保留更多速度"，
         * 而不是像原版那样每 tick 将速度乘以大于 1 的系数。
         */
        private fun resolveInertia(configured: Float, vanilla: Float): Float =
            if (configured <= 1.0f) {
                configured
            } else {
                (vanilla + (1.0f - vanilla) * (1.0f - 1.0f / configured)).coerceAtMost(0.9999f)
            }
    }
}
