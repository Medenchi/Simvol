package com.simvol.mod.cutscene;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Управляет камерой во время катсцены.
 * Интерполирует позицию и поворот между keyframe-ами.
 */
public class CutsceneCamera {

    // Текущие значения камеры (интерполированные)
    public double camX, camY, camZ;
    public float  camYaw, camPitch;

    // Предыдущий кадр (для плавной интерполяции)
    private double prevX, prevY, prevZ;
    private float  prevYaw, prevPitch;

    /**
     * Применяет интерполяцию между текущим кадром и следующим.
     * @param frame     текущий keyframe
     * @param progress  прогресс (0.0 → 1.0)
     * @param client    клиент Minecraft
     */
    public void apply(CutsceneFrame frame, float progress, MinecraftClient client) {
        if (client.player == null) return;

        // Сглаженный прогресс (ease in-out)
        float smooth = smoothstep(progress);

        // Целевая позиция (из кадра)
        double targetX = frame.pos().x;
        double targetY = frame.pos().y;
        double targetZ = frame.pos().z;
        float  targetYaw   = frame.yaw();
        float  targetPitch = frame.pitch();

        // Интерполяция
        camX   = MathHelper.lerp(smooth, prevX,     targetX);
        camY   = MathHelper.lerp(smooth, prevY,     targetY);
        camZ   = MathHelper.lerp(smooth, prevZ,     targetZ);
        camYaw   = lerpAngle(prevYaw,   targetYaw,   smooth);
        camPitch = lerpAngle(prevPitch, targetPitch, smooth);

        // Если прогресс достиг 1.0 — запоминаем как предыдущий
        if (progress >= 1.0f) {
            prevX     = targetX;
            prevY     = targetY;
            prevZ     = targetZ;
            prevYaw   = targetYaw;
            prevPitch = targetPitch;
        }
    }

    public void reset(MinecraftClient client) {
        if (client.player == null) return;
        prevX     = client.player.getX();
        prevY     = client.player.getY() + client.player.getEyeHeight(client.player.getPose());
        prevZ     = client.player.getZ();
        prevYaw   = client.player.getYaw();
        prevPitch = client.player.getPitch();
    }

    /** Плавная функция ease in-out */
    private float smoothstep(float t) {
        return t * t * (3f - 2f * t);
    }

    /** Интерполяция углов (учитывает переход через 360°) */
    private float lerpAngle(float a, float b, float t) {
        float diff = b - a;
        while (diff > 180f)  diff -= 360f;
        while (diff < -180f) diff += 360f;
        return a + diff * t;
    }
}
