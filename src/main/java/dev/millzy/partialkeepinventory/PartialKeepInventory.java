package dev.millzy.partialkeepinventory;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class PartialKeepInventory implements ModInitializer {
	public static final String MOD_ID = "partialkeepinventory";

	private static final String RULE_NAME = "partialKeepInventory";
	private static final GameRules.Category RULE_CATEGORY = GameRules.Category.PLAYER;
	public static final GameRules.Key<GameRules.BooleanRule> RULE = GameRuleRegistry.register(RULE_NAME, RULE_CATEGORY, GameRuleFactory.createBooleanRule(false));

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
    }
}