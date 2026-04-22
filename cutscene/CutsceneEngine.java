package com.simvol.mod.cutscene;

import com.simvol.mod.SimvolClient;
import com.simvol.mod.client.hud.HudOverlay;
import com.simvol.mod.dialogue.DialogueEngine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.*;

/**
 * ДВИЖОК КАТСЦЕН МОДА "СИМВОЛ"
 * ================================
 * Управляет:
 * - Воспроизведением катсцен по keyframe-пути
 * - Letterbox (чёрные полосы)
 * - Блокировкой управления игрока
 * - Синхронизацией диалогов с камерой
 * - Запуском и остановкой катсцен
 *
 * Архитектура:
 * Катсцена = список CutsceneFrame (keyframe-ов)
 * Каждый Frame содержит:
 *   - Позицию камеры
 *   - Поворот камеры (yaw, pitch)
 *   - Время перехода в мс
 *   - Действие (диалог, звук, анимация NPC и т.д.)
 */
public class CutsceneEngine {

    // ── Состояние ─────────────────────────────────────────────

    private boolean isPlaying = false;
    private boolean isPaused  = false;

    private CutsceneDef currentCutscene = null;
    private int         currentFrame    = 0;
    private float       frameProgress   = 0f;  // 0.0 → 1.0 между кадрами
    private int         frameTimer      = 0;

    // ── Letterbox ─────────────────────────────────────────────

    /** Текущая высота чёрных полос (0 = нет, 1.0 = полный letterbox) */
    public float  letterboxProgress  = 0f;
    public boolean letterboxEnabled  = false;
    private static final float LETTERBOX_SPEED = 0.04f;
    private static final float LETTERBOX_HEIGHT = 0.11f; // 11% экрана

    // ── Диалоги ───────────────────────────────────────────────

    private final DialogueEngine dialogueEngine = new DialogueEngine();

    // ── Камера ────────────────────────────────────────────────

    private final CutsceneCamera camera = new CutsceneCamera();

    // ── Реестр катсцен ────────────────────────────────────────

    private final Map<String, CutsceneDef> registry = new HashMap<>();

    // ── Управление ────────────────────────────────────────────

    /** Разрешён ли движок управлять камерой */
    public boolean isCameraControlled = false;

    // ── Колбеки ───────────────────────────────────────────────

    private Runnable onCutsceneEnd = null;

    // =========================================================
    //  ПУБЛИЧНОЕ API
    // =========================================================

    /**
     * Зарегистрировать катсцену.
     * Вызывается при загрузке мода.
     */
    public void register(String id, CutsceneDef def) {
        registry.put(id, def);
    }

    /**
     * Запустить катсцену по ID.
     * @param id      ID катсцены
     * @param onEnd   Что сделать после завершения (может быть null)
     */
    public void play(String id, Runnable onEnd) {
        CutsceneDef def = registry.get(id);
        if (def == null) {
            SimvolClient.CUTSCENE = this;
            return;
        }

        currentCutscene  = def;
        currentFrame     = 0;
        frameProgress    = 0f;
        frameTimer       = 0;
        isPlaying        = true;
        isPaused         = false;
        onCutsceneEnd    = onEnd;

        // Включаем letterbox
        enableLetterbox(true);

        // Блокируем управление игрока
        lockPlayerControls(true);

        // Включаем управление камерой
        isCameraControlled = true;

        // Применяем первый кадр мгновенно
        applyFrame(currentCutscene.frames().get(0), 0f);
    }

    /** Остановить катсцену досрочно */
    public void stop() {
        isPlaying          = false;
        isCameraControlled = false;
        enableLetterbox(false);
        lockPlayerControls(false);

        if (onCutsceneEnd != null) {
            onCutsceneEnd.run();
            onCutsceneEnd = null;
        }
    }

    /** Поставить на паузу */
    public void pause() { isPaused = true; }

    /** Снять с паузы */
    public void resume() { isPaused = false; }

    public boolean isPlaying() { return isPlaying; }

    public DialogueEngine getDialogueEngine() { return dialogueEngine; }

    // =========================================================
    //  ТИК (вызывается каждый клиентский тик)
    // =========================================================

    public void tick(MinecraftClient client) {
        // Letterbox анимация
        tickLetterbox();

        // Диалоги
        dialogueEngine.tick(client);

        if (!isPlaying || isPaused) return;

        List<CutsceneFrame> frames = currentCutscene.frames();
        if (currentFrame >= frames.size()) {
            // Катсцена завершена
            onCutsceneFinished();
            return;
        }

        CutsceneFrame frame = frames.get(currentFrame);
        int duration = frame.durationTicks();

        frameTimer++;

        // Прогресс внутри текущего кадра (0.0 → 1.0)
        frameProgress = Math.min(1.0f, (float) frameTimer / Math.max(1, duration));

        // Применяем интерполяцию камеры между текущим и следующим кадром
        if (currentFrame + 1 < frames.size()) {
            applyFrame(frame, frameProgress);
        }

        // Выполняем действие кадра (один раз, в начале)
        if (frameTimer == 1 && frame.action() != null) {
            frame.action().execute(client, this);
        }

        // Переход к следующему кадру
        if (frameTimer >= duration) {
            currentFrame++;
            frameTimer   = 0;
            frameProgress = 0f;
        }
    }

    // =========================================================
    //  ВНУТРЕННИЕ МЕТОДЫ
    // =========================================================

    private void applyFrame(CutsceneFrame frame, float progress) {
        camera.apply(frame, progress, MinecraftClient.getInstance());
    }

    private void onCutsceneFinished() {
        isPlaying          = false;
        isCameraControlled = false;
        enableLetterbox(false);
        lockPlayerControls(false);

        if (onCutsceneEnd != null) {
            onCutsceneEnd.run();
            onCutsceneEnd = null;
        }
    }

    // ── Letterbox ─────────────────────────────────────────────

    public void enableLetterbox(boolean enable) {
        letterboxEnabled = enable;
    }

    private void tickLetterbox() {
        if (letterboxEnabled) {
            letterboxProgress = Math.min(1.0f, letterboxProgress + LETTERBOX_SPEED);
        } else {
            letterboxProgress = Math.max(0.0f, letterboxProgress - LETTERBOX_SPEED);
        }
    }

    /** Возвращает высоту одной полосы в пикселях */
    public int getLetterboxPixels(int screenHeight) {
        return (int)(screenHeight * LETTERBOX_HEIGHT * letterboxProgress);
    }

    // ── Блокировка управления ─────────────────────────────────

    private void lockPlayerControls(boolean lock) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        if (lock) {
            // Запрещаем движение и поворот камеры
            client.options.forwardKey.setPressed(false);
            client.options.backKey.setPressed(false);
            client.options.leftKey.setPressed(false);
            client.options.rightKey.setPressed(false);
            client.options.jumpKey.setPressed(false);
        }
        // Полная блокировка реализована через Mixin в SimvolMixins
    }
}
