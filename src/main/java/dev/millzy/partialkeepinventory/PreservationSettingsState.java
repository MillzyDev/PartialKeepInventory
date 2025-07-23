package dev.millzy.partialkeepinventory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.ArrayList;
import java.util.List;

public class PreservationSettingsState extends PersistentState {
    public static final PersistentStateType<PreservationSettingsState> ID = new PersistentStateType<>(
        "partialKeepInventorySettings",
        PreservationSettingsState::new,
        RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("settingsFlags").forGetter(self -> self.settingsFlags),
                Codec.STRING.listOf().fieldOf("itemList").forGetter(self -> self.itemList)
        ).apply(instance, PreservationSettingsState::new)),
        DataFixTypes.LEVEL
    );

    private int settingsFlags;
    private List<String> itemList;

    public PreservationSettingsState() {
        this.settingsFlags = 0;
        this.itemList = new ArrayList<>();
    }

    public PreservationSettingsState(int settingsFlags, List<String> itemList) {
        this.settingsFlags = settingsFlags;
        this.itemList = new ArrayList<>(itemList);
    }

    public PreservationSettingsHandler getSettings() {
        return new PreservationSettingsHandler(this.settingsFlags);
    }

    public void enableSetting(PreservationSettings setting) {
        PreservationSettingsHandler settingsHandler = getSettings();
        settingsHandler.enableSetting(setting);
        this.settingsFlags = settingsHandler.getFlagsValue();
        this.markDirty();
    }

    public void disableSetting(PreservationSettings setting) {
        PreservationSettingsHandler settingsHandler = getSettings();
        settingsHandler.disableSetting(setting);
        this.settingsFlags = settingsHandler.getFlagsValue();
        this.markDirty();
    }

    public boolean getSetting(PreservationSettings setting) {
        PreservationSettingsHandler settingsHandler = getSettings();
        return settingsHandler.getSetting(setting);
    }

    public String[] getEnabledSettingNames() {
        return getSettings().getValueDisplays();
    }

    public String[] getItemsFromList() {
        return this.itemList.toArray(new String[0]);
    }

    public void addToItemList(ItemStack item) {
        String itemString = item.getItem().toString();
        this.itemList.add(itemString);
        this.markDirty();
    }

    public void removeFromItemList(ItemStack item) {
        String itemString = item.getItem().toString();
        this.itemList.remove(itemString);
        this.markDirty();
    }

    public boolean isInItemList(ItemStack item) {
        return this.itemList.contains(item.getItem().toString());
    }
}
