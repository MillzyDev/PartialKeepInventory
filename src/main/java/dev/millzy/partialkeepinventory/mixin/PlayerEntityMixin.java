package dev.millzy.partialkeepinventory.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "getExperienceToDrop", cancellable = true)
    void getExperienceToDropHead(ServerWorld world, CallbackInfoReturnable<Integer> info) {
        IPlayerEntityAccessor playerEntityAccessor = (IPlayerEntityAccessor)this;

        int ret = !this.isSpectator() ? Math.min(playerEntityAccessor.getExperienceLevel() * 7, 100) : 0;
        info.setReturnValue(ret);
    }
}
