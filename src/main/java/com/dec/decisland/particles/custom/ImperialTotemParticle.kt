package com.dec.decisland.particles.custom

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.core.particles.SimpleParticleType

class ImperialTotemParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double,
    private val sprites: SpriteSet,
) : TextureSheetParticle(level, x, y, z) {
    init {
        xd = xSpeed
        yd = ySpeed
        zd = zSpeed
        friction = 0.86f
        gravity = 0.02f
        lifetime = 60 + level.random.nextInt(24)
        quadSize = 0.16f + level.random.nextFloat() * 0.08f
        setColor(0.38f, 0.9f, 1.0f)
        setSpriteFromAge(sprites)
    }

    override fun tick() {
        super.tick()
        if (isAlive) setSpriteFromAge(sprites)
    }

    override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

    class Provider(private val sprites: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            type: SimpleParticleType,
            level: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double,
        ): Particle = ImperialTotemParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites)
    }
}
