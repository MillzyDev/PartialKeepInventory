package dev.millzy.partialkeepinventory.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(at = @At("TAIL"), method = "copyFrom")
    void copyFromTail(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        if (!alive) {
            this.getAttributes().setBaseFrom(oldPlayer.getAttributes());
            this.setHealth(oldPlayer.getMaxHealth());

            // keep inventory exclusive stuff
            this.getInventory().clone(oldPlayer.getInventory());
            //this.experienceLevel = oldPlayer.experienceLevel;
            //this.totalExperience = oldPlayer.totalExperience;
            //this.experienceProgress = oldPlayer.experienceProgress;
            this.setScore(oldPlayer.getScore());
        }
    }
}
