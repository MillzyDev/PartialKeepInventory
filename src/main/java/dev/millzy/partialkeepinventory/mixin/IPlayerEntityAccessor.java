package dev.millzy.partialkeepinventory.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerEntity.class)
public interface IPlayerEntityAccessor{
    @Accessor("experienceLevel")
    int getExperienceLevel();
}
