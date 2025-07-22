package dev.millzy.partialkeepinventory;

import java.util.EnumSet;

public class PreservationSettingsHandler {
    private final EnumSet<PreservationSettings> preservationSettings = EnumSet.of(PreservationSettings.NONE);
    private final int SETTINGS_MAX_ORDINAL = EnumSet.allOf(PreservationSettings.class).stream()
            .max(PreservationSettings::compareTo).orElse(PreservationSettings.NONE).ordinal();

    public PreservationSettingsHandler() {
        this(0);
    }

    public PreservationSettingsHandler(int value) {
        for (int i = 0; (1 << i) <= (1 << SETTINGS_MAX_ORDINAL); i++) {
            int testFlag = 1 << i;

            if ((value & testFlag) == testFlag) {
                preservationSettings.add(PreservationSettings.values()[i]);
            }
        }
    }

    public int getFlagsValue() {
        return preservationSettings.stream()
            .mapToInt(m -> 1 << m.ordinal())
            .reduce(0, (st, v) -> st | v);
    }


    public boolean getSetting(PreservationSettings setting) {
        return preservationSettings.contains(setting);
    }

    public void enableSetting(PreservationSettings setting) {
        preservationSettings.add(setting);
    }

    public void disableSetting(PreservationSettings setting) {
        preservationSettings.remove(setting);
    }
}
