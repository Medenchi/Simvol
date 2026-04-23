package com.simvol.mod.mixin;

import com.simvol.mod.SimvolClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MIXIN: Блокировка движения игрока
 * ===================================
 * Блокирует движение и поворот камеры в двух случаях:
 * 1. Идёт катсцена (SimvolClient.CUTSCENE.isPlaying())
 * 2. Показаны 3D кнопки выбора (MovementLockHelper.isLocked())
 */
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Shadow
    public Input input;

    @Inject(
        method = "tickMovement",
        at = @At("HEAD")
    )
    private void onTickMovement(CallbackInfo ci) {
        // Блокируем движение если нужно
        boolean shouldLock =
            MovementLockHelper.isLocked() ||
            SimvolClient.isCutscenePlaying();

        if (shouldLock && input != null) {
            input.movementForward  = 0f;
            input.movementSideways = 0f;
            input.jumping          = false;
            input.sneaking         = false;
        }
    }
}
