package com.dec.decisland.item.compat

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * 1.21.1 兼容层：替代 1.21.4+ 的 `Projectile.spawnProjectileUsingShoot`。
 * 工厂负责构造并定位投射物（通常为 (Level, LivingEntity, ItemStack) 辅助构造器），
 * 本工具随后按方向 + 初速 + 散布发射并生成实体。
 */
object ModProjectileUtil {
    fun <T : Projectile> spawnUsingShoot(
        factory: (Level, LivingEntity, ItemStack) -> T,
        level: Level,
        weapon: ItemStack,
        owner: LivingEntity,
        vx: Double,
        vy: Double,
        vz: Double,
        inaccuracy: Float,
        velocity: Float,
    ): T {
        val projectile = factory(level, owner, weapon)
        projectile.shoot(vx, vy, vz, velocity, inaccuracy)
        level.addFreshEntity(projectile)
        return projectile
    }
}
