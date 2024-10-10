package io.github.redvortexdev.creakingdepths;

import net.fabricmc.api.ModInitializer;

public class Depths implements ModInitializer {

    public static final String MOD_ID = "creakingdepths";

    @Override
    public void onInitialize() {
        ModGameRules.initialize();
    }
}
