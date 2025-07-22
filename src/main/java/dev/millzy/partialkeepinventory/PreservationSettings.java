package dev.millzy.partialkeepinventory;

import java.util.EnumSet;
import java.util.Objects;

public enum PreservationSettings {
    NONE("none"),
    EQUIPMENT("equipment"),
    EQUIPPABLES("equippables"),
    TOOLS("tools"),
    HOTBAR("hotbar"),
    OFFHAND("offhand"),
    EPIC_ITEMS("epicItems"),
    RARE_ITEMS("rareItems"),
    UNCOMMON_ITEMS("uncommonItems"),
    COMMON_ITEMS("commonItems"),
    EXPERIENCE("experience"),
    CUSTOM_LIST("customList");

    private final String display;
    private static final EnumSet<PreservationSettings> ALL_VALUES = EnumSet.allOf(PreservationSettings.class);

    PreservationSettings(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return this.display;
    }

    public static PreservationSettings fromString(String str) {
        return ALL_VALUES.stream()
                .filter(v -> Objects.equals(v.display, str))
                .findFirst().orElse(NONE);
    }
}