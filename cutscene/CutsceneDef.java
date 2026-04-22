package com.simvol.mod.cutscene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Определение (definition) катсцены.
 *
 * Катсцена = ID + список кадров по порядку.
 * Движок CutsceneEngine берёт эту запись и воспроизводит кадры один за другим.
 *
 * КАК СОЗДАВАТЬ КАТСЦЕНУ (пример):
 *
 *   CutsceneDef cs = CutsceneDef.builder("act0_fired")
 *       .frame(CutsceneFrame.of(100, 70, 200,  0f, -30f, 60))   // кадр 1: снизу вверх по зданию
 *       .frame(CutsceneFrame.of(100, 80, 200,  0f,  0f,  40))   // кадр 2: уровень глаз
 *       .frame(CutsceneFrame.of(105, 65, 195, 45f, 10f,  80,    // кадр 3: заходим в здание
 *           (client, engine) -> engine.getDialogueEngine()
 *               .startDialogue("boss_fires_detective", null, client.player)))
 *       .build();
 */
public final class CutsceneDef {

    /** Уникальный ID катсцены. По этому ID запускаешь: engine.play("act0_fired", ...) */
    private final String id;

    /** Кадры в порядке воспроизведения */
    private final List<CutsceneFrame> frames;

    // Приватный конструктор
    private CutsceneDef(String id, List<CutsceneFrame> frames) {
        this.id     = id;
        this.frames = Collections.unmodifiableList(frames);
    }

    // =========================================================
    //  СТАТИЧЕСКИЕ МЕТОДЫ СОЗДАНИЯ
    // =========================================================

    /**
     * Быстрое создание через varargs (для коротких катсцен).
     *
     * Пример:
     *   CutsceneDef.of("my_cutscene",
     *       CutsceneFrame.of(0, 64, 0,  0f, 0f, 40),
     *       CutsceneFrame.of(5, 64, 0,  0f, 0f, 40)
     *   );
     */
    public static CutsceneDef of(String id, CutsceneFrame... frames) {
        return new CutsceneDef(id, List.of(frames));
    }

    /**
     * Создание через билдер (для длинных катсцен — удобнее).
     *
     * Пример:
     *   CutsceneDef cs = CutsceneDef.builder("long_cutscene")
     *       .frame(...)
     *       .frame(...)
     *       .frame(...)
     *       .build();
     */
    public static Builder builder(String id) {
        return new Builder(id);
    }

    // ── Геттеры ───────────────────────────────────────────────

    public String              id()     { return id;     }
    public List<CutsceneFrame> frames() { return frames; }

    /** Общая длительность катсцены в тиках */
    public int totalDurationTicks() {
        return frames.stream()
                     .mapToInt(CutsceneFrame::durationTicks)
                     .sum();
    }

    /** Общая длительность в секундах (примерно) */
    public float totalDurationSeconds() {
        return totalDurationTicks() / 20f;
    }

    // =========================================================
    //  БИЛДЕР
    // =========================================================

    public static final class Builder {

        private final String id;
        private final List<CutsceneFrame> frames = new ArrayList<>();

        private Builder(String id) {
            this.id = id;
        }

        /**
         * Добавить кадр в конец катсцены.
         */
        public Builder frame(CutsceneFrame frame) {
            frames.add(frame);
            return this;
        }

        /**
         * Добавить паузу (камера стоит неподвижно N тиков).
         * Позиция/поворот берутся из последнего добавленного кадра.
         *
         * Если кадров ещё нет — ничего не добавляется.
         */
        public Builder pause(int ticks) {
            if (!frames.isEmpty()) {
                CutsceneFrame last = frames.get(frames.size() - 1);
                frames.add(CutsceneFrame.still(
                        last.pos().x, last.pos().y, last.pos().z,
                        last.yaw(), last.pitch(), ticks
                ));
            }
            return this;
        }

        /**
         * Добавить паузу с действием.
         */
        public Builder pause(int ticks, CutsceneAction action) {
            if (!frames.isEmpty()) {
                CutsceneFrame last = frames.get(frames.size() - 1);
                frames.add(CutsceneFrame.still(
                        last.pos().x, last.pos().y, last.pos().z,
                        last.yaw(), last.pitch(), ticks, action
                ));
            }
            return this;
        }

        /** Построить CutsceneDef */
        public CutsceneDef build() {
            if (frames.isEmpty()) {
                throw new IllegalStateException(
                    "CutsceneDef '" + id + "' не содержит ни одного кадра!");
            }
            return new CutsceneDef(id, frames);
        }
    }
}
