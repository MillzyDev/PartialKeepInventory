package dev.millzy.partialkeepinventory.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerInventory.class)
public interface IPlayerInventoryAccessor {
    @Accessor("main")
    DefaultedList<ItemStack> getMain();

    @Accessor("player")
    PlayerEntity getPlayer();
}
