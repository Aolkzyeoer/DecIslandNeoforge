package com.dec.decisland.world.portal;

import com.dec.decisland.worldgen.ModDimensions;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public final class SnowPortalTeleporter {
    private SnowPortalTeleporter() {}

    public static @Nullable DimensionTransition createTransition(ServerLevel sourceLevel, Entity entity, BlockPos portalPos, Direction.Axis sourceAxis) {
        ServerLevel destination = sourceLevel.dimension().equals(ModDimensions.VOID_LEVEL) ? sourceLevel.getServer().getLevel(Level.OVERWORLD) : sourceLevel.getServer().getLevel(ModDimensions.VOID_LEVEL);
        if (destination == null) {
            return null;
        }

        double scale = DimensionType.getTeleportationScale(sourceLevel.dimensionType(), destination.dimensionType());
        BlockPos scaled = destination.getWorldBorder().clampToBounds(entity.getX() * scale, entity.getY(), entity.getZ() * scale);

        Optional<SnowPortalShape> sourceShape = SnowPortalShape.find(sourceLevel, portalPos, sourceAxis);
        Vec3 relativePos = sourceShape.map(shape -> shape.getRelativePosition(entity)).orElse(new Vec3(0.5, 0.0, 0.0));

        Optional<SnowPortalShape> destShape = findClosestPortal(destination, scaled, sourceAxis);
        if (destShape.isEmpty()) {
            destShape = createDestinationPortal(destination, scaled, sourceAxis);
        }
        if (destShape.isEmpty()) {
            return null;
        }

        SnowPortalShape shape = destShape.get();
        Vec3 teleportPos = shape.getTeleportPos(destination, entity, relativePos, sourceAxis);
        float yaw = entity.getYRot();
        if (sourceAxis != Direction.Axis.Y && shape.axis() != Direction.Axis.Y && sourceAxis != shape.axis()) {
            yaw += 90.0F;
        }
        return new DimensionTransition(
            destination,
            teleportPos,
            entity.getDeltaMovement(),
            yaw,
            entity.getXRot(),
            DimensionTransition.PLAY_PORTAL_SOUND.then(e -> e.placePortalTicket(shape.lowerCorner()))
        );
    }

    private static Optional<SnowPortalShape> findClosestPortal(ServerLevel level, BlockPos center, Direction.Axis preferredAxis) {
        double bestDistance = Double.MAX_VALUE;
        SnowPortalShape bestShape = null;
        Set<BlockPos> seen = new HashSet<>();
        int radius = 32;
        for (int x = center.getX() - radius; x <= center.getX() + radius; x++) {
            for (int z = center.getZ() - radius; z <= center.getZ() + radius; z++) {
                for (int y = level.getMaxBuildHeight(); y >= level.getMinBuildHeight(); y--) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!level.getBlockState(pos).is(com.dec.decisland.block.ModBlocks.SNOW_PORTAL.get())) {
                        continue;
                    }
                    Direction.Axis axis = level.getBlockState(pos).getValue(com.dec.decisland.block.custom.SnowPortalBlock.AXIS);
                    Optional<SnowPortalShape> shape = SnowPortalShape.find(level, pos, axis);
                    if (shape.isEmpty() || !shape.get().isComplete() || !seen.add(shape.get().lowerCorner())) {
                        continue;
                    }
                    double distance = shape.get().lowerCorner().distSqr(center);
                    if (axis == preferredAxis) {
                        distance -= 0.25;
                    }
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestShape = shape.get();
                    }
                }
            }
        }
        return Optional.ofNullable(bestShape);
    }

    private static Optional<SnowPortalShape> createDestinationPortal(ServerLevel level, BlockPos center, Direction.Axis axis) {
        for (int radius = 0; radius <= 16; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (Math.abs(dx) != radius && Math.abs(dz) != radius) {
                        continue;
                    }
                    int x = center.getX() + dx;
                    int z = center.getZ() + dz;
                    int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                    y = Mth.clamp(y, level.getMinBuildHeight() + 2, level.getMaxBuildHeight() - 4);
                    BlockPos lowerCorner = new BlockPos(x, y, z);
                    Optional<SnowPortalShape> created = SnowPortalShape.createMinimalPortal(level, lowerCorner, axis);
                    if (created.isPresent()) {
                        return created;
                    }
                    if (axis != Direction.Axis.Y) {
                        for (int offsetY = -4; offsetY <= 4; offsetY++) {
                            created = SnowPortalShape.createMinimalPortal(level, lowerCorner.offset(0, offsetY, 0), axis);
                            if (created.isPresent()) {
                                return created;
                            }
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
}
