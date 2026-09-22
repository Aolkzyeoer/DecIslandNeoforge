package com.dec.decisland.mixin;

import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCooldowns.class)
public interface ItemCooldownsAccessor {
    // 1.21.1: cooldowns map is keyed by Item (ResourceLocation groups came later)
    @Accessor("cooldowns")
    Map<Item, Object> decisland$getCooldowns();

    @Accessor("tickCount")
    int decisland$getTickCount();
}
