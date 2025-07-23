package dev.millzy.partialkeepinventory.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.millzy.partialkeepinventory.PartialKeepInventory;
import dev.millzy.partialkeepinventory.PreservationSettings;
import dev.millzy.partialkeepinventory.PreservationSettingsHandler;
import dev.millzy.partialkeepinventory.PreservationSettingsState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Optional;

public class PartialKeepInventoryCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        final LiteralArgumentBuilder<ServerCommandSource> builder =
            CommandManager.literal("partialKeepInventory")
            .requires(CommandManager.requirePermissionLevel(2))
            .then(
                CommandManager.literal("features")
                .executes(PartialKeepInventoryCommand::listFeatures)
                .then(
                    CommandManager.literal("add")
                    .then(
                        CommandManager.argument("feature", StringArgumentType.word())
                        .suggests(new PreservationSettingsSuggestionProvider())
                        .executes(PartialKeepInventoryCommand::addFeature)
                    )
                )
                .then(
                    CommandManager.literal("remove")
                    .then(
                        CommandManager.argument("feature", StringArgumentType.word())
                        .suggests(new PreservationSettingsSuggestionProvider())
                        .executes(PartialKeepInventoryCommand::removeFeature)
                    )
                )
            )
            .then(
                CommandManager.literal("itemList")
                .executes(null) // TODO: Show custom list
                .then(
                    CommandManager.literal("add")
                    .then(
                        CommandManager.argument("item", ItemStackArgumentType.itemStack(registryAccess))
                        //.executes(null) // TODO: Add item to list
                    )
                )
                .then(
                    CommandManager.literal("remove")
                    .then(
                        CommandManager.argument("item", ItemStackArgumentType.itemStack(registryAccess))
                        //.executes(null) // TODO: Remove item from list
                    )
                )
            );

        dispatcher.register(builder);
    }

    private static int listFeatures(CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();

        PreservationSettingsState settingsState = world.getPersistentStateManager().getOrCreate(PreservationSettingsState.ID);
        String[] enabledSettings = settingsState.getEnabledSettingNames();
        Optional<String> combinedList = Arrays.stream(enabledSettings).reduce((st, v) -> st.concat("\n").concat(v));

        Text message;

        message = combinedList
                .map(s -> Text.of("Enabled features:\n" + s))
                .orElseGet(() -> Text.of("No features are enabled."));

        context.getSource().sendMessage(message);
        return 0;
    }

    private static int addFeature(CommandContext<ServerCommandSource> context) {
        String feature = StringArgumentType.getString(context, "feature");
        PreservationSettings setting = PreservationSettings.fromString(feature);

        if (setting == PreservationSettings.NONE) {
            // TODO: exception
        }

        ServerWorld world = context.getSource().getWorld();

        PreservationSettingsState settingsState = world.getPersistentStateManager().getOrCreate(PreservationSettingsState.ID);
        settingsState.enableSetting(setting);
        world.getPersistentStateManager().set(PreservationSettingsState.ID, settingsState);

        context.getSource().sendMessage(Text.of("Enabled feature: " + feature));
        return 0;
    }

    private static int removeFeature(CommandContext<ServerCommandSource> context) {
        String feature = StringArgumentType.getString(context, "feature");
        PreservationSettings setting = PreservationSettings.fromString(feature);

        if (setting == PreservationSettings.NONE) {
            // TODO: exception
        }

        ServerWorld world = context.getSource().getWorld();

        PreservationSettingsState settingsState = world.getPersistentStateManager().getOrCreate(PreservationSettingsState.ID);
        settingsState.disableSetting(setting);
        world.getPersistentStateManager().set(PreservationSettingsState.ID, settingsState);

        context.getSource().sendMessage(Text.of("Disabled feature: " + feature));
        return 0;
    }
}
