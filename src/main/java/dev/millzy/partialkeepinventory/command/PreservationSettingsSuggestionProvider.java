package dev.millzy.partialkeepinventory.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.millzy.partialkeepinventory.PreservationSettings;
import dev.millzy.partialkeepinventory.PreservationSettingsHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

public class PreservationSettingsSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        for (PreservationSettings setting : PreservationSettingsHandler.ALL_SETTINGS) {
            if (!CommandSource.shouldSuggest(builder.getRemaining(), setting.getDisplay())) {
               continue;
            }

            builder.suggest(setting.getDisplay());
        }

        return builder.buildFuture();
    }
}
