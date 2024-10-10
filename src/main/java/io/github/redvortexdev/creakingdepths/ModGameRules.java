package io.github.redvortexdev.creakingdepths;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class ModGameRules {
    public static final GameRules.Key<GameRules.BooleanRule> DO_CREAKING_SPAWNING =
            GameRuleRegistry.register("doCreakingSpawning", GameRules.Category.SPAWNING, GameRuleFactory.createBooleanRule(true));

    public static void initialize() {

    }

}
