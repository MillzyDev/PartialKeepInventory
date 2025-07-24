package dev.millzy.partialkeepinventory;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Rarity;

import java.util.HashMap;
import java.util.function.Predicate;

public class InventorySlotChecker {
    private final HashMap<PreservationSettings, Predicate<Integer>> slotCheckers = new HashMap<>();
    private final PlayerInventory inventory;
    private final PreservationSettingsState preservationSettings;

    public InventorySlotChecker(PlayerInventory inventory, PreservationSettingsState preservationSettings) {
        this.inventory = inventory;
        this.preservationSettings = preservationSettings;

        this.slotCheckers.put(PreservationSettings.EQUIPMENT, this::isEquipment);
        this.slotCheckers.put(PreservationSettings.EQUIPPABLES, this::isEquippable);
        this.slotCheckers.put(PreservationSettings.TOOLS, this::isTool);
        this.slotCheckers.put(PreservationSettings.HOTBAR, this::isHotbar);
        this.slotCheckers.put(PreservationSettings.OFFHAND, this::isOffhand);
        this.slotCheckers.put(PreservationSettings.EPIC_ITEMS, this::isEpicRarity);
        this.slotCheckers.put(PreservationSettings.RARE_ITEMS, this::isRareRarity);
        this.slotCheckers.put(PreservationSettings.UNCOMMON_ITEMS, this::isUncommonRarity);
        this.slotCheckers.put(PreservationSettings.COMMON_ITEMS, this::isCommonRarity);
        this.slotCheckers.put(PreservationSettings.CUSTOM_LIST, this::isInItemList);
    }

    public boolean shouldDrop(int slot) {
        boolean shouldNotDrop = false;

        for (PreservationSettings setting : this.preservationSettings.getSettings().get()) {
            shouldNotDrop |= this.slotCheckers.getOrDefault(setting, (u) -> false).test(slot);
        }

        return !shouldNotDrop;
    }

    private boolean isEquipment(int slot) {
        return PlayerInventory.EQUIPMENT_SLOTS.containsKey(slot);
    }

    private boolean isEquippable(int slot) {
        return this.inventory.getStack(slot).contains(DataComponentTypes.EQUIPPABLE);
    }

    private boolean isTool(int slot) {
        return this.inventory.getStack(slot).contains(DataComponentTypes.TOOL);
    }

    private boolean isHotbar(int slot) {
        return slot < 9;
    }

    private boolean isOffhand(int slot) {
        return slot == PlayerInventory.OFF_HAND_SLOT;
    }

    private boolean isEpicRarity(int slot) {
        return this.inventory.getStack(slot).getRarity() == Rarity.EPIC;
    }

    private boolean isRareRarity(int slot) {
        return this.inventory.getStack(slot).getRarity() == Rarity.RARE;
    }

    private boolean isUncommonRarity(int slot) {
        return this.inventory.getStack(slot).getRarity() == Rarity.UNCOMMON;
    }

    private boolean isCommonRarity(int slot) {
        return this.inventory.getStack(slot).getRarity() == Rarity.COMMON;
    }

    private boolean isInItemList(int slot) {
        return this.preservationSettings.isInItemList(inventory.getStack(slot));
    }
}
