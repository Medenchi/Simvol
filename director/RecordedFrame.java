package com.simvol.mod.director;

/**
 * Один записанный кадр катсцены.
 * Хранит позицию/поворот камеры + настройки кадра.
 *
 * Отличие от CutsceneFrame:
 * - RecordedFrame — изменяемый, используется в редакторе
 * - CutsceneFrame — неизменяемый record, используется движком
 */
public class RecordedFrame {

    // ── Позиция и поворот камеры ──────────────────────────────

    public double x, y, z;
    public float  yaw, pitch;

    // ── Настройки кадра ───────────────────────────────────────

    /** Длительность в тиках (20 = 1 секунда) */
    private int durationTicks;

    /**
     * ID действия для этого кадра.
     * Примеры: "dialogue", "sound", "title", "flag", "paranoia", "end"
     * Пусто = нет действия
     */
    private String actionId;

    /**
     * Аргумент действия.
     * Для "dialogue" — ID диалога
     * Для "sound"    — ID звука
     * Для "title"    — текст заголовка
     * Для "flag"     — имя StoryFlag
     * Для "paranoia" — количество (число строкой)
     */
    private String actionArg;

    // ── Конструктор ───────────────────────────────────────────

    public RecordedFrame(double x, double y, double z,
                         float yaw, float pitch,
                         int durationTicks,
                         String actionId,
                         String actionArg) {
        this.x             = x;
        this.y             = y;
        this.z             = z;
        this.yaw           = yaw;
        this.pitch         = pitch;
        this.durationTicks = durationTicks;
        this.actionId      = actionId  != null ? actionId  : "";
        this.actionArg     = actionArg != null ? actionArg : "";
    }

    // ── Геттеры / Сеттеры ─────────────────────────────────────

    public int    getDurationTicks()         { return durationTicks; }
    public void   setDurationTicks(int t)    { this.durationTicks = Math.max(1, t); }

    public String getActionId()              { return actionId; }
    public void   setActionId(String id)     { this.actionId = id != null ? id : ""; }

    public String getActionArg()             { return actionArg; }
    public void   setActionArg(String arg)   { this.actionArg = arg != null ? arg : ""; }

    public boolean hasAction() {
        return !actionId.isEmpty();
    }

    /** Длительность в секундах (для отображения в UI) */
    public float getDurationSeconds() {
        return durationTicks / 20f;
    }

    /** Краткое описание кадра для списка в UI */
    public String getSummary(int index) {
        String pos = String.format("%.1f / %.1f / %.1f", x, y, z);
        String dur = String.format("%.1f", getDurationSeconds()) + "с";
        String act = hasAction() ? " [" + actionId + "]" : "";
        return "#" + (index + 1) + "  " + pos + "  " + dur + act;
    }

    /** Глубокая копия (для сохранения снапшота) */
    public RecordedFrame copy() {
        return new RecordedFrame(x, y, z, yaw, pitch, durationTicks, actionId, actionArg);
    }
}
