package com.simvol.mod.client.hud;

import com.simvol.mod.SimvolClient;
import com.simvol.mod.Simvol;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

/**
 * HUD ОВЕРЛЕЙ МОДА "СИМВОЛ"
 * ===========================
 * Рендерит поверх всего экрана:
 *
 * 1. LETTERBOX      — чёрные полосы сверху/снизу (кино-эффект)
 * 2. FADE TO BLACK  — полное затемнение экрана (переходы между сценами)
 * 3. VIGNETTE       — тёмные края (нарастает с паранойей)
 * 4. FLASH          — белая вспышка (резкие моменты)
 *
 * ВСЕ ЭФФЕКТЫ АНИМИРОВАНЫ — плавное появление и исчезновение.
 *
 * ПОРЯДОК РЕНДЕРА (снизу вверх):
 *   1. Виньетка паранойи  (самый нижний слой)
 *   2. Letterbox          (поверх виньетки)
 *   3. Fade to black      (поверх всего)
 *   4. Flash              (самый верхний слой)
 */
public class HudOverlay {

    // =========================================================
    //  LETTERBOX
    // =========================================================

    /**
     * Высота чёрных полос от 0.0 до 1.0.
     * 0.0 = полосы невидимы
     * 1.0 = полосы полностью видимы (11% экрана каждая)
     */
    private float letterboxProgress = 0f;

    /** Целевое значение (к чему анимируемся) */
    private float letterboxTarget = 0f;

    /**
     * Скорость анимации letterbox.
     * 0.04f = ~0.5 секунды на полное появление/исчезновение
     */
    private static final float LETTERBOX_SPEED = 0.04f;

    /**
     * Высота одной полосы как доля экрана.
     * 0.11 = 11% — стандартный кинематографический формат 2.35:1
     */
    private static final float LETTERBOX_HEIGHT_FRACTION = 0.11f;

    /** Цвет letterbox — чисто чёрный */
    private static final int LETTERBOX_COLOR = 0xFF000000;

    // =========================================================
    //  FADE TO BLACK
    // =========================================================

    /**
     * Текущая непрозрачность затемнения от 0.0 до 1.0.
     * 0.0 = прозрачный (не виден)
     * 1.0 = полностью чёрный экран
     */
    private float fadeAlpha = 0f;

    /** Режим fade: появление или исчезновение */
    private FadeMode fadeMode = FadeMode.NONE;

    /** Длительность fade в тиках */
    private int fadeDurationTicks = 0;

    /** Сколько тиков прошло с начала fade */
    private int fadeTimer = 0;

    /** Что делать когда fade завершён */
    private Runnable fadeCallback = null;

    private enum FadeMode {
        NONE,       // Нет активного fade
        TO_BLACK,   // Темнеем (0 → 1)
        FROM_BLACK, // Светлеем (1 → 0)
        HOLD        // Держим чёрный экран (ждём следующей команды)
    }

    /** Цвет затемнения — чёрный */
    private static final int FADE_COLOR_BASE = 0x000000;

    // =========================================================
    //  ВИНЬЕТКА ПАРАНОЙИ
    // =========================================================

    /**
     * Текущая интенсивность виньетки от 0.0 до 1.0.
     * Плавно нарастает при повышении паранойи.
     */
    private float vignetteIntensity = 0f;

    /** Скорость изменения виньетки (очень медленная — незаметно для игрока) */
    private static final float VIGNETTE_LERP_SPEED = 0.003f;

    // =========================================================
    //  FLASH (белая/чёрная вспышка)
    // =========================================================

    /** Текущая непрозрачность вспышки */
    private float flashAlpha = 0f;

    /** Цвет вспышки */
    private int flashColor = 0xFFFFFF;

    /** Длительность вспышки в тиках */
    private int flashDuration = 0;

    /** Тик вспышки */
    private int flashTimer = 0;

    // =========================================================
    //  ТИК (вызывается каждый клиентский тик)
    // =========================================================

    /**
     * Обновляем все анимации каждый тик.
     * Вызывается из SimvolClient → ClientTickEvents.
     *
     * ВАЖНО: render() вызывается каждый кадр (60+ раз в секунду),
     * а tick() — 20 раз в секунду. Анимации считаются в тиках,
     * интерполяция между тиками — в render() через tickDelta.
     */
    public void tick(MinecraftClient client) {
        tickLetterbox();
        tickFade();
        tickVignette();
        tickFlash();
    }

    // ── Тик letterbox ─────────────────────────────────────────

    private void tickLetterbox() {
        // Плавно двигаем progress к target
        if (letterboxProgress < letterboxTarget) {
            letterboxProgress = Math.min(
                letterboxTarget,
                letterboxProgress + LETTERBOX_SPEED
            );
        } else if (letterboxProgress > letterboxTarget) {
            letterboxProgress = Math.max(
                letterboxTarget,
                letterboxProgress - LETTERBOX_SPEED
            );
        }
    }

    // ── Тик fade ──────────────────────────────────────────────

    private void tickFade() {
        if (fadeMode == FadeMode.NONE || fadeMode == FadeMode.HOLD) return;

        fadeTimer++;
        float progress = fadeDurationTicks > 0
            ? (float) fadeTimer / fadeDurationTicks
            : 1f;
        progress = MathHelper.clamp(progress, 0f, 1f);

        if (fadeMode == FadeMode.TO_BLACK) {
            fadeAlpha = smoothstep(progress);

            if (fadeTimer >= fadeDurationTicks) {
                fadeAlpha  = 1f;
                fadeMode   = FadeMode.HOLD;
                fadeTimer  = 0;
                // Вызываем колбек (например: переход к следующей сцене)
                if (fadeCallback != null) {
                    Runnable cb = fadeCallback;
                    fadeCallback = null;
                    cb.run();
                }
            }

        } else if (fadeMode == FadeMode.FROM_BLACK) {
            fadeAlpha = smoothstep(1f - progress);

            if (fadeTimer >= fadeDurationTicks) {
                fadeAlpha = 0f;
                fadeMode  = FadeMode.NONE;
                fadeTimer = 0;
                if (fadeCallback != null) {
                    Runnable cb = fadeCallback;
                    fadeCallback = null;
                    cb.run();
                }
            }
        }
    }

    // ── Тик виньетки ──────────────────────────────────────────

    private void tickVignette() {
        // Целевая интенсивность = уровень паранойи / 20
        // При паранойе 10 → виньетка 0.5 (50% максимальной)
        float target = SimvolClient.getParanoiaLevel() / 20f;

        // Очень медленное изменение — игрок не замечает момент появления
        vignetteIntensity += (target - vignetteIntensity) * VIGNETTE_LERP_SPEED;
        vignetteIntensity = MathHelper.clamp(vignetteIntensity, 0f, 1f);
    }

    // ── Тик вспышки ───────────────────────────────────────────

    private void tickFlash() {
        if (flashDuration <= 0) return;

        flashTimer++;
        float progress = (float) flashTimer / flashDuration;

        // Вспышка: быстро появляется, потом исчезает
        if (progress < 0.3f) {
            // Нарастание (30% времени)
            flashAlpha = progress / 0.3f;
        } else {
            // Угасание (70% времени)
            flashAlpha = 1f - ((progress - 0.3f) / 0.7f);
        }
        flashAlpha = MathHelper.clamp(flashAlpha, 0f, 1f);

        if (flashTimer >= flashDuration) {
            flashAlpha    = 0f;
            flashDuration = 0;
            flashTimer    = 0;
        }
    }

    // =========================================================
    //  РЕНДЕР (вызывается каждый кадр)
    // =========================================================

    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        int w = client.getWindow().getScaledWidth();
        int h = client.getWindow().getScaledHeight();

        // Порядок важен — рисуем снизу вверх

        // 1. Виньетка паранойи (самый нижний слой)
        renderVignette(context, w, h);

        // 2. Letterbox
        renderLetterbox(context, w, h);

        // 3. Fade to black
        renderFade(context, w, h);

        // 4. Flash
        renderFlash(context, w, h);
    }

    // ── Рендер letterbox ──────────────────────────────────────

    private void renderLetterbox(DrawContext context, int w, int h) {
        if (letterboxProgress <= 0f) return;

        // Высота одной полосы в пикселях
        int barH = (int)(h * LETTERBOX_HEIGHT_FRACTION * letterboxProgress);
        if (barH <= 0) return;

        // Верхняя полоса
        context.fill(0, 0, w, barH, LETTERBOX_COLOR);

        // Нижняя полоса
        context.fill(0, h - barH, w, h, LETTERBOX_COLOR);
    }

    // ── Рендер fade to black ───────────────────────────────────

    private void renderFade(DrawContext context, int w, int h) {
        if (fadeAlpha <= 0f) return;

        // Альфа от 0 до 255
        int alpha = (int)(fadeAlpha * 255f);
        alpha = MathHelper.clamp(alpha, 0, 255);

        int color = (alpha << 24) | FADE_COLOR_BASE;
        context.fill(0, 0, w, h, color);
    }

    // ── Рендер виньетки ───────────────────────────────────────

    private void renderVignette(DrawContext context, int w, int h) {
        if (vignetteIntensity <= 0f) return;

        // Виньетка = 4 градиентные полосы по краям экрана
        // Цвет: тёмно-красный с нарастающей непрозрачностью

        // Максимальная прозрачность полосы
        int maxAlpha = (int)(vignetteIntensity * 120f);
        maxAlpha = MathHelper.clamp(maxAlpha, 0, 120);

        // Ширина/высота полос
        int bw = (int)(w * 0.18f * vignetteIntensity);
        int bh = (int)(h * 0.18f * vignetteIntensity);

        int color = (maxAlpha << 24) | 0x0A0000;

        // Левая полоса
        renderGradientH(context, 0, 0, bw, h, color, 0x00000000);

        // Правая полоса
        renderGradientH(context, w - bw, 0, w, h, 0x00000000, color);

        // Верхняя полоса
        renderGradientV(context, 0, 0, w, bh, color, 0x00000000);

        // Нижняя полоса
        renderGradientV(context, 0, h - bh, w, h, 0x00000000, color);
    }

    // ── Рендер вспышки ────────────────────────────────────────

    private void renderFlash(DrawContext context, int w, int h) {
        if (flashAlpha <= 0f) return;

        int alpha = (int)(flashAlpha * 255f);
        alpha = MathHelper.clamp(alpha, 0, 255);
        int color = (alpha << 24) | (flashColor & 0xFFFFFF);
        context.fill(0, 0, w, h, color);
    }

    // ── Градиент по горизонтали ───────────────────────────────

    private void renderGradientH(DrawContext context,
                                  int x1, int y1, int x2, int y2,
                                  int colorLeft, int colorRight) {
        // DrawContext.fillGradient рисует градиент сверху вниз,
        // нам нужен слева направо — рисуем вертикальными полосами
        int steps = Math.max(1, x2 - x1);
        for (int i = 0; i < steps; i++) {
            float t = (float) i / steps;
            int alpha = lerpAlpha(colorLeft, colorRight, t);
            int base  = lerpColor(colorLeft, colorRight, t);
            context.fill(x1 + i, y1, x1 + i + 1, y2, (alpha << 24) | (base & 0xFFFFFF));
        }
    }

    private void renderGradientV(DrawContext context,
                                  int x1, int y1, int x2, int y2,
                                  int colorTop, int colorBottom) {
        context.fillGradient(x1, y1, x2, y2, colorTop, colorBottom);
    }

    // =========================================================
    //  ПУБЛИЧНОЕ API
    //  Вызывай эти методы из CutsceneAction и других систем
    // =========================================================

    // ── Letterbox ─────────────────────────────────────────────

    /**
     * Включить или выключить letterbox.
     * Появление/исчезновение плавное (0.5 секунды).
     *
     * Вызывается автоматически движком катсцен.
     * Можно вызвать вручную для особых моментов.
     */
    public void setLetterbox(boolean enabled) {
        letterboxTarget = enabled ? 1f : 0f;
    }

    /**
     * Получить высоту одной полосы letterbox в пикселях.
     * Используется SubtitleOverlay чтобы не рисовать субтитры поверх полос.
     */
    public int getLetterboxPixels(int screenHeight) {
        return (int)(screenHeight * LETTERBOX_HEIGHT_FRACTION * letterboxProgress);
    }

    /**
     * Текущий прогресс letterbox (0.0 - 1.0).
     */
    public float getLetterboxProgress() {
        return letterboxProgress;
    }

    // ── Fade to black ─────────────────────────────────────────

    /**
     * Плавное затемнение экрана до чёрного.
     *
     * @param durationTicks длительность затемнения в тиках (20 = 1 сек)
     * @param onComplete    что сделать когда экран стал чёрным (может быть null)
     *
     * Пример — затемнить за 1 секунду, потом переключить сцену:
     *   SimvolClient.HUD.startFadeToBlack(20, () -> {
     *       SimvolClient.playCutscene("act1_start", null);
     *       SimvolClient.HUD.startFadeFromBlack(20, null);
     *   });
     */
    public void startFadeToBlack(int durationTicks, Runnable onComplete) {
        fadeMode          = FadeMode.TO_BLACK;
        fadeDurationTicks = Math.max(1, durationTicks);
        fadeTimer         = 0;
        fadeCallback      = onComplete;
        Simvol.LOGGER.info("СИМВОЛ HUD: Fade to black начат (" + durationTicks + " тиков)");
    }

    /**
     * Удобная версия без колбека.
     */
    public void startFadeToBlack(int durationTicks) {
        startFadeToBlack(durationTicks, null);
    }

    /**
     * Плавное появление из чёрного экрана.
     * Обычно вызывается сразу после startFadeToBlack в колбеке.
     *
     * @param durationTicks длительность появления в тиках
     * @param onComplete    что сделать когда экран полностью появился
     */
    public void startFadeFromBlack(int durationTicks, Runnable onComplete) {
        fadeAlpha         = 1f;
        fadeMode          = FadeMode.FROM_BLACK;
        fadeDurationTicks = Math.max(1, durationTicks);
        fadeTimer         = 0;
        fadeCallback      = onComplete;
        Simvol.LOGGER.info("СИМВОЛ HUD: Fade from black начат (" + durationTicks + " тиков)");
    }

    /**
     * Удобная версия без колбека.
     */
    public void startFadeFromBlack(int durationTicks) {
        startFadeFromBlack(durationTicks, null);
    }

    /**
     * Мгновенно сделать экран чёрным (без анимации).
     * Используй когда нужен мгновенный переход.
     */
    public void setBlackInstant() {
        fadeAlpha = 1f;
        fadeMode  = FadeMode.HOLD;
        fadeTimer = 0;
    }

    /**
     * Мгновенно убрать затемнение.
     */
    public void clearBlackInstant() {
        fadeAlpha = 0f;
        fadeMode  = FadeMode.NONE;
        fadeTimer = 0;
    }

    /**
     * Проверить — идёт ли сейчас fade.
     */
    public boolean isFading() {
        return fadeMode != FadeMode.NONE && fadeMode != FadeMode.HOLD;
    }

    /**
     * Проверить — чёрный ли экран сейчас (полностью или почти).
     */
    public boolean isBlack() {
        return fadeAlpha >= 0.95f;
    }

    // ── Flash ─────────────────────────────────────────────────

    /**
     * Белая вспышка экрана.
     * Используется для резких драматических моментов.
     *
     * @param durationTicks длительность вспышки (5-15 тиков обычно)
     *
     * Пример — резкий момент обнаружения тел в стекле:
     *   SimvolClient.HUD.flashWhite(8);
     */
    public void flashWhite(int durationTicks) {
        flash(0xFFFFFF, durationTicks);
    }

    /**
     * Красная вспышка (удар, боль, шок).
     */
    public void flashRed(int durationTicks) {
        flash(0xFF0000, durationTicks);
    }

    /**
     * Вспышка произвольного цвета.
     */
    public void flash(int color, int durationTicks) {
        flashColor    = color;
        flashDuration = Math.max(1, durationTicks);
        flashTimer    = 0;
        flashAlpha    = 0f;
    }

    // ── Виньетка ──────────────────────────────────────────────

    /**
     * Принудительно установить интенсивность виньетки.
     * Обычно не нужно — она сама нарастает с паранойей.
     * Используй только для особых моментов.
     */
    public void setVignetteIntensity(float intensity) {
        vignetteIntensity = MathHelper.clamp(intensity, 0f, 1f);
    }

    public float getVignetteIntensity() {
        return vignetteIntensity;
    }

    // =========================================================
    //  СОСТАВНЫЕ ЭФФЕКТЫ (удобные комбинации)
    // =========================================================

    /**
     * Стандартный переход между сценами:
     * Затемнение → колбек → появление из темноты
     *
     * @param fadeOutTicks  тиков на затемнение
     * @param holdTicks     тиков держать чёрный экран
     * @param fadeInTicks   тиков на появление
     * @param onMidpoint    что сделать в момент полного затемнения
     *
     * Пример:
     *   SimvolClient.HUD.sceneTransition(20, 10, 20, () -> {
     *       // Телепортируем игрока, меняем локацию
     *   });
     */
    public void sceneTransition(int fadeOutTicks, int holdTicks,
                                 int fadeInTicks, Runnable onMidpoint) {
        startFadeToBlack(fadeOutTicks, () -> {
            // Выполняем колбек в момент черноты
            if (onMidpoint != null) onMidpoint.run();

            // Держим чёрный экран N тиков, потом светлеем
            // Упрощённо: сразу начинаем fade in
            // В полной реализации — через scheduler с holdTicks задержкой
            startFadeFromBlack(fadeInTicks, null);
        });
    }

    /**
     * Конец акта:
     * Затемнение → показ названия акта → появление
     *
     * @param actTitle    текст типа "АКТ 1"
     * @param actSubtitle текст типа "СТЕКЛО"
     */
    public void actTransition(String actTitle, String actSubtitle) {
        startFadeToBlack(30, () -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null) {
                client.inGameHud.setTitle(
                    net.minecraft.text.Text.literal(actTitle)
                );
                client.inGameHud.setSubtitle(
                    net.minecraft.text.Text.literal(actSubtitle)
                );
                client.inGameHud.setTitleTicks(10, 80, 20);
            }
            startFadeFromBlack(30, null);
        });
    }

    // =========================================================
    //  МАТЕМАТИЧЕСКИЕ УТИЛИТЫ
    // =========================================================

    /** Сглаженная функция (ease in-out) */
    private float smoothstep(float t) {
        t = MathHelper.clamp(t, 0f, 1f);
        return t * t * (3f - 2f * t);
    }

    /** Интерполяция альфа-канала двух цветов */
    private int lerpAlpha(int colorA, int colorB, float t) {
        int alphaA = (colorA >> 24) & 0xFF;
        int alphaB = (colorB >> 24) & 0xFF;
        return (int)(alphaA + (alphaB - alphaA) * t);
    }

    /** Интерполяция RGB двух цветов */
    private int lerpColor(int colorA, int colorB, float t) {
        int rA = (colorA >> 16) & 0xFF;
        int gA = (colorA >> 8)  & 0xFF;
        int bA =  colorA        & 0xFF;
        int rB = (colorB >> 16) & 0xFF;
        int gB = (colorB >> 8)  & 0xFF;
        int bB =  colorB        & 0xFF;
        int r  = (int)(rA + (rB - rA) * t);
        int g  = (int)(gA + (gB - gA) * t);
        int b  = (int)(bA + (bB - bA) * t);
        return (r << 16) | (g << 8) | b;
    }
}
