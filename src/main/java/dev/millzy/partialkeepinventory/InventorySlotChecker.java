package dev.millzy.partialkeepinventory;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Rarity;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiPredicate;

public class InventorySlotChecker {
    private final static HashMap<PreservationSettings, BiPredicate<PlayerInventory, Integer>> SLOT_CHECKERS = new HashMap<>();

    public static boolean shouldDrop(PlayerInventory inventory, int slot, PreservationSettingsHandler preservationSettings) {
        boolean shouldNotDrop = false;

        for (PreservationSettings setting : preservationSettings.get()) {
            shouldNotDrop |= SLOT_CHECKERS.getOrDefault(setting, (t, u) -> false).test(inventory, slot);
        }

        return !shouldNotDrop;
    }

    private static boolean isEquipment(PlayerInventory inventory, int slot) {
        return PlayerInventory.EQUIPMENT_SLOTS.containsKey(slot);
    }

    private static boolean isEquippable(PlayerInventory inventory, int slot) {
        return inventory.getStack(slot).contains(DataComponentTypes.EQUIPPABLE);
    }

    private static boolean isTool(PlayerInventory inventory, int slot) {
        return inventory.getStack(slot).contains(DataComponentTypes.TOOL);
    }

    private static boolean isHotbar(PlayerInventory inventory, int slot) {
        return slot < 9;
    }

    private static boolean isOffhand(PlayerInventory inventory, int slot) {
        return slot == PlayerInventory.OFF_HAND_SLOT;
    }

    private static boolean isEpicRarity(PlayerInventory inventory, int slot) {
        return inventory.getStack(slot).getRarity() == Rarity.EPIC;
    }

    private static boolean isRareRarity(PlayerInventory inventory, int slot) {
        return inventory.getStack(slot).getRarity() == Rarity.RARE;
    }

    private static boolean isUncommonRarity(PlayerInventory inventory, int slot) {
        return inventory.getStack(slot).getRarity() == Rarity.UNCOMMON;
    }

    private static boolean isCommonRarity(PlayerInventory inventory, int slot) {
        return inventory.getStack(slot).getRarity() == Rarity.COMMON;
    }

    private static boolean isInItemList(PlayerInventory inventory, int slot) {
        ServerWorld overworld = Objects.requireNonNull(inventory.player.getServer()).getOverworld();
        PreservationSettingsState settingsState = overworld.getPersistentStateManager().getOrCreate(PreservationSettingsState.ID);

        return settingsState.isInItemList(inventory.getStack(slot));
    }

    static {
        SLOT_CHECKERS.put(PreservationSettings.EQUIPMENT, InventorySlotChecker::isEquipment);
        SLOT_CHECKERS.put(PreservationSettings.EQUIPPABLES, InventorySlotChecker::isEquippable);
        SLOT_CHECKERS.put(PreservationSettings.TOOLS, InventorySlotChecker::isTool);
        SLOT_CHECKERS.put(PreservationSettings.HOTBAR, InventorySlotChecker::isHotbar);
        SLOT_CHECKERS.put(PreservationSettings.OFFHAND, InventorySlotChecker::isOffhand);
        SLOT_CHECKERS.put(PreservationSettings.EPIC_ITEMS, InventorySlotChecker::isEpicRarity);
        SLOT_CHECKERS.put(PreservationSettings.RARE_ITEMS, InventorySlotChecker::isRareRarity);
        SLOT_CHECKERS.put(PreservationSettings.UNCOMMON_ITEMS, InventorySlotChecker::isUncommonRarity);
        SLOT_CHECKERS.put(PreservationSettings.COMMON_ITEMS, InventorySlotChecker::isCommonRarity);
        SLOT_CHECKERS.put(PreservationSettings.CUSTOM_LIST, InventorySlotChecker::isInItemList);
    }
}
