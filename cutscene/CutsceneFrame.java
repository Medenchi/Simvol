package com.simvol.mod.cutscene;

import net.minecraft.util.math.Vec3d;

/**
 * Один keyframe (ключевой кадр) катсцены.
 *
 * Пример использования:
 *   CutsceneFrame.of(100, 64, 200,   // камера на X=100 Y=64 Z=200
 *                    -90f, 15f,       // смотрит на запад, чуть вниз
 *                    60)              // длится 60 тиков = 3 секунды
 *
 * Система сама плавно интерполирует (lerp) между двумя соседними кадрами.
 * Тебе не нужно думать о промежуточных позициях — только ключевые точки.
 */
public final class CutsceneFrame {

    // ── Поля ──────────────────────────────────────────────────

    /**
     * Позиция камеры в мире Minecraft.
     * Это НЕ позиция игрока — это буквально где висит камера.
     * Vec3d = (X, Y, Z) как double
     */
    private final Vec3d cameraPos;

    /**
     * Горизонтальный поворот камеры в градусах.
     * Такой же как у игрока:
     *   0   = на юг (в сторону +Z)
     *  90   = на запад (-X)
     * 180   = на север (-Z)
     * -90   = на восток (+X)
     */
    private final float yaw;

    /**
     * Вертикальный поворот камеры в градусах.
     *  0   = смотрит прямо (горизонт)
     * -90  = смотрит прямо вверх
     * +90  = смотрит прямо вниз
     *  15  = слегка вниз (типичный кинематографический угол)
     * -10  = слегка вверх (показывает величие объекта)
     */
    private final float pitch;

    /**
     * Сколько тиков длится этот кадр.
     * 20 тиков = 1 секунда реального времени.
     *
     * Примеры:
     *  20 = 1 секунда
     *  40 = 2 секунды
     *  60 = 3 секунды
     * 100 = 5 секунд
     * 200 = 10 секунд
     *
     * За это время система плавно перемещает камеру
     * из положения ЭТОГО кадра к положению СЛЕДУЮЩЕГО.
     */
    private final int durationTicks;

    /**
     * Действие которое выполняется В НАЧАЛЕ этого кадра (один раз).
     * Может быть null — тогда просто движение камеры без событий.
     *
     * Примеры действий:
     * - Начать диалог
     * - Воспроизвести звук
     * - Заставить NPC сыграть анимацию
     * - Появление текста на экране
     */
    private final CutsceneAction action;

    // ── Конструктор (приватный, используй статические методы) ──

    private CutsceneFrame(Vec3d cameraPos, float yaw, float pitch,
                          int durationTicks, CutsceneAction action) {
        this.cameraPos     = cameraPos;
        this.durationTicks = durationTicks;
        this.yaw           = yaw;
        this.pitch         = pitch;
        this.action        = action;
    }

    // =========================================================
    //  СТАТИЧЕСКИЕ ФАБРИЧНЫЕ МЕТОДЫ (то что ты будешь использовать)
    // =========================================================

    /**
     * Создать кадр БЕЗ действия (просто движение камеры).
     *
     * @param x     позиция камеры по X
     * @param y     позиция камеры по Y (высота)
     * @param z     позиция камеры по Z
     * @param yaw   поворот по горизонтали (градусы)
     * @param pitch поворот по вертикали (градусы)
     * @param ticks длительность в тиках (20 = 1 секунда)
     */
    public static CutsceneFrame of(double x, double y, double z,
                                    float yaw, float pitch, int ticks) {
        return new CutsceneFrame(new Vec3d(x, y, z), yaw, pitch, ticks, null);
    }

    /**
     * Создать кадр С ДЕЙСТВИЕМ.
     * Действие выполняется в НАЧАЛЕ кадра.
     *
     * @param x      позиция камеры по X
     * @param y      позиция камеры по Y
     * @param z      позиция камеры по Z
     * @param yaw    поворот по горизонтали
     * @param pitch  поворот по вертикали
     * @param ticks  длительность
     * @param action что произойдёт в начале кадра
     */
    public static CutsceneFrame of(double x, double y, double z,
                                    float yaw, float pitch, int ticks,
                                    CutsceneAction action) {
        return new CutsceneFrame(new Vec3d(x, y, z), yaw, pitch, ticks, action);
    }

    /**
     * Создать статичный кадр (камера не движется, просто ждёт).
     * Удобно для диалоговых сцен где камера стоит неподвижно.
     *
     * @param x     X
     * @param y     Y
     * @param z     Z
     * @param yaw   поворот
     * @param pitch наклон
     * @param ticks сколько ждать
     */
    public static CutsceneFrame still(double x, double y, double z,
                                       float yaw, float pitch, int ticks) {
        // still = такой же как of, но семантически "стоит на месте"
        return new CutsceneFrame(new Vec3d(x, y, z), yaw, pitch, ticks, null);
    }

    /**
     * Создать статичный кадр С действием.
     */
    public static CutsceneFrame still(double x, double y, double z,
                                       float yaw, float pitch, int ticks,
                                       CutsceneAction action) {
        return new CutsceneFrame(new Vec3d(x, y, z), yaw, pitch, ticks, action);
    }

    // ── Геттеры ───────────────────────────────────────────────

    public Vec3d pos()           { return cameraPos;     }
    public float yaw()           { return yaw;           }
    public float pitch()         { return pitch;         }
    public int   durationTicks() { return durationTicks; }
    public CutsceneAction action() { return action;      }
}
