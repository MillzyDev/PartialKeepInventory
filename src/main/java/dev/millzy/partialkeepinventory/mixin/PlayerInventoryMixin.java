package dev.millzy.partialkeepinventory.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements Inventory, Nameable {

	@Inject(at = @At("HEAD"), method = "dropAll", cancellable = true)
	private void dropAllHead(CallbackInfo info) {
		IPlayerInventoryAccessor playerInventoryAccessor = (IPlayerInventoryAccessor)this;

		DefaultedList<ItemStack> main = playerInventoryAccessor.getMain();
		PlayerEntity player = playerInventoryAccessor.getPlayer();

		// start at 9 to skip hotbar
		for (int i = 9; i < main.size(); i++) {
			ItemStack itemStack = main.get(i);

			if (itemStack.isEmpty()) {
				continue; // skip empty item slots
			}

			player.dropItem(itemStack, true, false);
			main.set(i, ItemStack.EMPTY);
		}

		// return early
		info.cancel();
	}
}