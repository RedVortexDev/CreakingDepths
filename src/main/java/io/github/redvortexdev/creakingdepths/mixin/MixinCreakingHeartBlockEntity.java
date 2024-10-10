package io.github.redvortexdev.creakingdepths.mixin;

import io.github.redvortexdev.creakingdepths.ModGameRules;
import net.minecraft.block.entity.CreakingHeartBlockEntity;
import net.minecraft.entity.mob.TransientCreakingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreakingHeartBlockEntity.class)
public class MixinCreakingHeartBlockEntity {
    @Inject(method = "spawnCreakingPuppet", at = @At("HEAD"), cancellable = true)
    private static void spawnCreakingPuppet(ServerWorld world, CreakingHeartBlockEntity blockEntity, CallbackInfoReturnable<TransientCreakingEntity> cir) {
        if (!world.getGameRules().getBoolean(ModGameRules.DO_CREAKING_SPAWNING)) cir.setReturnValue(null);
    }
}
