package dev.millzy.partialkeepinventory.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.millzy.partialkeepinventory.PartialKeepInventory;
import dev.millzy.partialkeepinventory.PreservationSettingsHandler;
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
                        .executes(null) // TODO: Add feature
                    )
                )
                .then(
                    CommandManager.literal("remove")
                    .then(
                        CommandManager.argument("feature", StringArgumentType.word())
                        .suggests(new PreservationSettingsSuggestionProvider())
                        .executes(null) // TODO: Remove feature
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
                        .executes(null) // TODO: Add item to list
                    )
                )
                .then(
                    CommandManager.literal("remove")
                    .then(
                        CommandManager.argument("item", ItemStackArgumentType.itemStack(registryAccess))
                        .executes(null) // TODO: Remove item from list
                    )
                )
            );

        dispatcher.register(builder);
    }

    private static int listFeatures(CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();

        int settingsFlags = world.getAttachedOrCreate(PartialKeepInventory.PRESERVATION_SETTINGS_ATTACHMENT, () -> 0);
        PreservationSettingsHandler preservationSettings = new PreservationSettingsHandler(settingsFlags);
        String[] enabledSettings = preservationSettings.getValueDisplays();
        Optional<String> combinedList = Arrays.stream(enabledSettings).reduce((st, v) -> st.concat("\n").concat(v));

        Text message;

        message = combinedList
                .map(s -> Text.of("Enabled features:\n" + s))
                .orElseGet(() -> Text.of("No features are enabled."));

        context.getSource().sendMessage(message);
        return 0;
    }

}
