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

    private int settingsFlags = 0;
    private List<String> itemList = new ArrayList<>();

    public PreservationSettingsState() {}

    public PreservationSettingsState(int settingsFlags, List<String> itemList) {
        this.settingsFlags = settingsFlags;
        this.itemList = itemList;
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
}
