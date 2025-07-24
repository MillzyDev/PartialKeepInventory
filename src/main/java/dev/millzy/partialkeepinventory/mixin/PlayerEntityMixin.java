package dev.millzy.partialkeepinventory.mixin;

import dev.millzy.partialkeepinventory.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow public int experienceLevel;
    @Shadow @Final PlayerInventory inventory;

    @Shadow protected abstract void vanishCursedItems();

    @Unique private final int MAX_SLOT = PlayerInventory.MAIN_SIZE + PlayerInventory.EQUIPMENT_SLOTS.size();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "getExperienceToDrop", cancellable = true)
    private void getExperienceToDropHead(ServerWorld world, CallbackInfoReturnable<Integer> info) {
        PreservationSettingsState settingsState = world.getPersistentStateManager().getOrCreate(PreservationSettingsState.ID);

        boolean preserveExperience = settingsState.getSetting(PreservationSettings.EXPERIENCE);
        int ret = !preserveExperience && !this.isSpectator() ? Math.min(this.experienceLevel * 7, 100) : 0;
        info.setReturnValue(ret);
    }

    @Inject(at = @At("HEAD"), method = "dropInventory", cancellable = true)
    void dropInventoryHead(ServerWorld world, CallbackInfo info) {
        super.dropInventory(world);

        if (!world.getGameRules().getBoolean(PartialKeepInventory.RULE)) {
            return;
        }

        if (!world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY) &&
                world.getGameRules().getBoolean(PartialKeepInventory.RULE)) {
            info.cancel();
        }

        PreservationSettingsState settingsState = world.getPersistentStateManager().getOrCreate(PreservationSettingsState.ID);
        InventorySlotChecker slotChecker = new InventorySlotChecker(this.inventory, settingsState);

        for (int i = 0; i < MAX_SLOT; i++) {
            if (!slotChecker.shouldDrop(i)) {
                continue;
            }

            ItemStack itemStack = this.inventory.getStack(i);
            this.dropItem(itemStack, true, false);
            this.inventory.setStack(i, ItemStack.EMPTY);
        }

        this.vanishCursedItems();
    }
}
