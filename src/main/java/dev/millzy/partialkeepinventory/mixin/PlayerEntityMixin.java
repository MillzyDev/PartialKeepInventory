package dev.millzy.partialkeepinventory.mixin;

import dev.millzy.partialkeepinventory.PartialKeepInventory;
import net.minecraft.component.DataComponentTypes;
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

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "getExperienceToDrop", cancellable = true)
    private void getExperienceToDropHead(ServerWorld world, CallbackInfoReturnable<Integer> info) {
        int ret = !this.isSpectator() ? Math.min(this.experienceLevel * 7, 100) : 0;
        info.setReturnValue(ret);
    }

    @Unique
    private void dropInventoryNoHotbar() {
        // Skip the first 9 slots, as this is the hotbar
        for (int i = 9; i < this.inventory.getMainStacks().size(); i++) {
            ItemStack itemStack = this.inventory.getMainStacks().get(i);

            if (itemStack.isEmpty()) {
                continue;
            }

            this.dropItem(itemStack, true, false);
            this.inventory.getMainStacks().set(i, ItemStack.EMPTY);
        }
    }

    @Unique
    private void dropInventoryNoTools() {
        // We check all slots including up to the offhand
        for (int i = 0; i < PlayerInventory.OFF_HAND_SLOT + 1; i++) {
            ItemStack itemStack = this.inventory.getStack(i);

            itemStack.streamTags().forEach(tag -> PartialKeepInventory.LOGGER.info(tag.id().toString()));

            if (itemStack.getComponents().stream().anyMatch(
                    c -> c.type() == DataComponentTypes.TOOL || c.type() == DataComponentTypes.EQUIPPABLE)) {
                continue;
            }

            this.dropItem(itemStack, true, false);
            this.inventory.setStack(i, ItemStack.EMPTY);
        }
    }

    @Inject(at = @At("TAIL"), method = "dropInventory")
    void dropInventoryHead(ServerWorld world, CallbackInfo info) {
        if (world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY) ||
                !world.getGameRules().getBoolean(PartialKeepInventory.RULE)) {
            return;
        }

       if (!world.getGameRules().getBoolean(PartialKeepInventory.RULE)) {
           return;
       }
    }
}
