package io.github.redvortexdev.creakingdepths.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.redvortexdev.creakingdepths.ModDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.CreakingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreakingEntity.class)
public abstract class MixinCreaking extends HostileEntity {
    protected MixinCreaking(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    // Attempt to entirely stop movement.
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/CreakingEntity;stopMovement()V"), method = "tickMovement")
    public void stopMovement(CallbackInfo ci) {
        this.setVelocity(0, 0, 0);
        this.teleport(this.lastRenderX, this.lastRenderY, this.lastRenderZ, false);
    }

    // Decrease the range required for a Creaking to unfreeze, so it can move in the fog.
    @ModifyExpressionValue(method = "shouldBeUnrooted", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/CreakingEntity;isEntityLookingAtMe(Lnet/minecraft/entity/LivingEntity;DZZLjava/util/function/Predicate;[Ljava/util/function/DoubleSupplier;)Z"))
    public boolean shouldBeUnrooted(boolean original, @Local(ordinal = 0) PlayerEntity playerEntity) {
        if (playerEntity.squaredDistanceTo(this) < 100) {
            return original;
        } else {
            // Out of range.
            return false;
        }
    }

    // Override tryAttack with a neck snap.
    @Inject(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/HostileEntity;tryAttack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;)Z"))
    private void createCreakingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir, @Local(ordinal = 0, argsOnly = true) Entity entity, @Local(ordinal = 0, argsOnly = true) ServerWorld world) {
        cir.cancel();
        // I wish I knew a cleaner way to do this.
        for (ServerPlayerEntity serverPlayerEntity : world.getPlayers()) {
            serverPlayerEntity.networkHandler.sendPacket(new PlaySoundS2CPacket(RegistryEntry.of(SoundEvents.ENTITY_CREAKING_ATTACK), SoundCategory.HOSTILE, entity.getX(), entity.getY(), entity.getZ(), 1F, 1F, 0));
            serverPlayerEntity.networkHandler.sendPacket(new PlaySoundS2CPacket(RegistryEntry.of(SoundEvents.ENTITY_CREAKING_SWAY), SoundCategory.HOSTILE, entity.getX(), entity.getY(), entity.getZ(), 1F, 0F, 0));
        }
        entity.damage(world, ModDamageTypes.of(world, ModDamageTypes.NECK_BREAK_DAMAGE_TYPE), 9999);
    }
}
