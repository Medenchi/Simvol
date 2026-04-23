package com.simvol.mod.mixin;

import com.simvol.mod.SimvolClient;
import com.simvol.mod.cutscene.CutsceneCamera;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MIXIN: Управление камерой во время катсцен
 * ============================================
 * Подменяет позицию и поворот камеры
 * когда активна катсцена.
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(
        method = "renderWorld",
        at = @At("HEAD")
    )
    private void onRenderWorld(float tickDelta, long limitTime,
                                net.minecraft.client.util.math.MatrixStack matrix,
                                CallbackInfo ci) {
        if (SimvolClient.CUTSCENE == null) return;
        if (!SimvolClient.CUTSCENE.isCameraControlled) return;

        // Камера управляется движком катсцен
        // Реальное переопределение позиции камеры реализуется
        // через Camera.update() mixin или через CameraAccessor
    }
}
