package io.github.redvortexdev.creakingdepths;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModDamageTypes {

    public static final RegistryKey<DamageType> NECK_BREAK_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Depths.MOD_ID, "neck_break"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return world.getDamageSources().create(key);
    }
}

