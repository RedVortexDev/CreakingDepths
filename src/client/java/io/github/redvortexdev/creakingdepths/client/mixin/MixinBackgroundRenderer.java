package io.github.redvortexdev.creakingdepths.client.mixin;

import io.github.redvortexdev.creakingdepths.client.DepthsClient;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.world.biome.WinterDropBuiltinBiomes;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public abstract class MixinBackgroundRenderer {
    // Apply extra fog in the Pale Garden.
    @Inject(at = @At("HEAD"), method = "applyFog", cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir) {
        if (DepthsClient.MC.player == null || camera.getSubmersionType() != CameraSubmersionType.NONE) return;

        var biome = DepthsClient.MC.player.clientWorld.getBiome(DepthsClient.MC.player.getBlockPos()).getKey();
        if (biome.isPresent() && biome.get().equals(WinterDropBuiltinBiomes.PALE_GARDEN)) {
            cir.setReturnValue(new Fog(5F, 10F, FogShape.CYLINDER, color.x, color.y, color.z, color.w));
        }
    }
}
