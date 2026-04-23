package com.simvol.mod.mixin;

/**
 * Хелпер для блокировки движения игрока.
 * Используется DialogueEngine когда показывает 3D кнопки выбора.
 *
 * Реальная блокировка реализована в Mixin на ClientPlayerEntity.
 */
public class MovementLockHelper {

    /** Заблокировано ли движение игрока */
    private static boolean locked = false;

    public static void setLocked(boolean lock) {
        locked = lock;
    }

    public static boolean isLocked() {
        return locked;
    }
}
