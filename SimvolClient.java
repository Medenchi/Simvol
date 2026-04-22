package com.simvol.mod;

import com.simvol.mod.client.hud.HudOverlay;
import com.simvol.mod.client.hud.SubtitleOverlay;
import com.simvol.mod.client.render.ModRenderers;
import com.simvol.mod.client.screen.ModScreens;
import com.simvol.mod.client.screen.WandScreen;
import com.simvol.mod.cutscene.CutsceneEngine;
import com.simvol.mod.cutscene.CutsceneRegistry;
import com.simvol.mod.director.DirectorSystem;
import com.simvol.mod.paranoia.ParanoiaEffects;
import com.simvol.mod.dialogue.VoiceManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class SimvolClient implements ClientModInitializer {

    // =========================================================
    //  СТАТИЧЕСКИЕ СИСТЕМЫ (доступны из любого места)
    // =========================================================

    /** Движок катсцен — управляет камерой, letterbox, кадрами */
    public static CutsceneEngine CUTSCENE;

    /** Эффекты паранойи — виньетка, помехи, пульсация */
    public static ParanoiaEffects PARANOIA_FX;

    /** Менеджер голосовой озвучки */
    public static VoiceManager VOICE;

    /** HUD оверлей — letterbox + виньетка */
    public static HudOverlay HUD;

    /** Субтитры диалогов */
    public static SubtitleOverlay SUBTITLES;

    /**
     * Система Палочки Режиссёра.
     * Хранит текущую сессию записи катсцены,
     * все сохранённые катсцены, логику редактора.
     */
    public static DirectorSystem DIRECTOR_GUI;

    // =========================================================
    //  ГОРЯЧИЕ КЛАВИШИ
    // =========================================================

    /**
     * Клавиша быстрой записи кадра.
     * По умолчанию: R
     * Работает так же как SHIFT+ПКМ с палочкой,
     * но без необходимости держать палочку в руке.
     */
    public static KeyBinding KEY_RECORD_FRAME;

    /**
     * Клавиша остановки катсцены / предпросмотра.
     * По умолчанию: F7
     */
    public static KeyBinding KEY_STOP_CUTSCENE;

    /**
     * Клавиша открытия меню палочки.
     * По умолчанию: F6
     */
    public static KeyBinding KEY_OPEN_WAND;

    // =========================================================
    //  ИНИЦИАЛИЗАЦИЯ
    // =========================================================

    @Override
    public void onInitializeClient() {
        Simvol.LOGGER.info("=== СИМВОЛ: Инициализация клиента ===");

        // ── Шаг 1: Создаём все клиентские системы ────────────
        initSystems();

        // ── Шаг 2: Загружаем сохранённые катсцены из файлов ──
        loadSavedData();

        // ── Шаг 3: Регистрируем рендереры entity ─────────────
        ModRenderers.registerAll();

        // ── Шаг 4: Регистрируем экраны GUI ───────────────────
        ModScreens.registerAll();

        // ── Шаг 5: Регистрируем горячие клавиши ──────────────
        registerKeybindings();

        // ── Шаг 6: Регистрируем HUD рендер ───────────────────
        registerHudRender();

        // ── Шаг 7: Регистрируем тики клиента ─────────────────
        registerClientTicks();

        Simvol.LOGGER.info("=== СИМВОЛ: Клиент готов ===");
    }

    // =========================================================
    //  ШАГ 1 — ИНИЦИАЛИЗАЦИЯ СИСТЕМ
    // =========================================================

    private void initSystems() {
        // Движок катсцен — создаём первым, остальные могут от него зависеть
        CUTSCENE = new CutsceneEngine();
        Simvol.LOGGER.info("СИМВОЛ: CutsceneEngine создан");

        // Эффекты паранойи
        PARANOIA_FX = new ParanoiaEffects();
        Simvol.LOGGER.info("СИМВОЛ: ParanoiaEffects создан");

        // Голосовой менеджер
        VOICE = new VoiceManager();
        Simvol.LOGGER.info("СИМВОЛ: VoiceManager создан");

        // HUD
        HUD = new HudOverlay();
        Simvol.LOGGER.info("СИМВОЛ: HudOverlay создан");

        // Субтитры
        SUBTITLES = new SubtitleOverlay();
        Simvol.LOGGER.info("СИМВОЛ: SubtitleOverlay создан");

        // Система Палочки Режиссёра
        DIRECTOR_GUI = new DirectorSystem();
        Simvol.LOGGER.info("СИМВОЛ: DirectorSystem создан");
    }

    // =========================================================
    //  ШАГ 2 — ЗАГРУЗКА СОХРАНЁННЫХ ДАННЫХ
    // =========================================================

    private void loadSavedData() {
        // Загружаем все катсцены которые ты записал Палочкой
        // Они хранятся в .minecraft/simvol_cutscenes/*.json
        DIRECTOR_GUI.loadAll(MinecraftClient.getInstance());
        Simvol.LOGGER.info("СИМВОЛ: Сохранённые катсцены загружены");

        // Регистрируем все встроенные катсцены из кода
        // (заглушки актов — заменятся твоими записанными)
        CutsceneRegistry.registerAll(CUTSCENE);
        Simvol.LOGGER.info("СИМВОЛ: Встроенные катсцены зарегистрированы");
    }

    // =========================================================
    //  ШАГ 5 — ГОРЯЧИЕ КЛАВИШИ
    // =========================================================

    private void registerKeybindings() {
        // Клавиша записи кадра (R)
        KEY_RECORD_FRAME = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.simvol.record_frame",        // translation key
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,                  // клавиша R
            "category.simvol.director"        // категория в настройках
        ));

        // Клавиша остановки катсцены (F7)
        KEY_STOP_CUTSCENE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.simvol.stop_cutscene",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F7,
            "category.simvol.director"
        ));

        // Клавиша открытия меню палочки (F6)
        KEY_OPEN_WAND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.simvol.open_wand",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F6,
            "category.simvol.director"
        ));

        Simvol.LOGGER.info("СИМВОЛ: Горячие клавиши зарегистрированы (R, F6, F7)");
    }

    // =========================================================
    //  ШАГ 6 — HUD РЕНДЕР
    // =========================================================

    private void registerHudRender() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {

            // 1. Letterbox (чёрные полосы) + виньетка паранойи
            if (HUD != null) {
                HUD.render(drawContext, tickDelta);
            }

            // 2. Субтитры диалогов
            if (SUBTITLES != null) {
                SUBTITLES.render(drawContext, tickDelta);
            }

            // 3. Помехи и эффекты паранойи
            if (PARANOIA_FX != null) {
                PARANOIA_FX.renderHud(drawContext, tickDelta);
            }
        });

        Simvol.LOGGER.info("СИМВОЛ: HUD рендер зарегистрирован");
    }

    // =========================================================
    //  ШАГ 7 — КЛИЕНТСКИЕ ТИКИ
    // =========================================================

    private void registerClientTicks() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            // ── Тик движка катсцен ────────────────────────────
            if (CUTSCENE != null) {
                CUTSCENE.tick(client);
            }

            // ── Тик эффектов паранойи ─────────────────────────
            if (PARANOIA_FX != null) {
                PARANOIA_FX.tick(client);
            }

            // ── Тик голосового менеджера ──────────────────────
            if (VOICE != null) {
                VOICE.tick(client);
            }

            // ── Обработка горячих клавиш ──────────────────────
            handleKeybindings(client);
        });

        Simvol.LOGGER.info("СИМВОЛ: Клиентские тики зарегистрированы");
    }

    // =========================================================
    //  ОБРАБОТКА ГОРЯЧИХ КЛАВИШ (вызывается каждый тик)
    // =========================================================

    private void handleKeybindings(MinecraftClient client) {
        // Клавиша R — записать кадр
        while (KEY_RECORD_FRAME != null && KEY_RECORD_FRAME.wasPressed()) {
            if (DIRECTOR_GUI != null) {
                DIRECTOR_GUI.recordFrameNow(client);
            }
        }

        // Клавиша F6 — открыть меню палочки
        while (KEY_OPEN_WAND != null && KEY_OPEN_WAND.wasPressed()) {
            // Не открывать если уже открыт какой-то экран
            if (client.currentScreen == null) {
                client.setScreen(new WandScreen());
            }
        }

        // Клавиша F7 — остановить катсцену / предпросмотр
        while (KEY_STOP_CUTSCENE != null && KEY_STOP_CUTSCENE.wasPressed()) {
            if (CUTSCENE != null && CUTSCENE.isPlaying()) {
                if (DIRECTOR_GUI != null && DIRECTOR_GUI.isPreviewing()) {
                    // Остановить предпросмотр
                    DIRECTOR_GUI.stopPreview(client);
                } else {
                    // Остановить обычную катсцену
                    CUTSCENE.stop();
                }
            }
        }
    }

    // =========================================================
    //  СТАТИЧЕСКИЕ УТИЛИТЫ
    //  (вызывай из любого места через SimvolClient.xxx())
    // =========================================================

    /**
     * Запустить катсцену по ID.
     * Катсцена должна быть записана Палочкой или зарегистрирована в коде.
     *
     * Пример:
     *   SimvolClient.playCutscene("act0_fired", () -> {
     *       // что сделать после окончания
     *   });
     */
    public static void playCutscene(String id, Runnable onEnd) {
        if (CUTSCENE == null) {
            Simvol.LOGGER.error("СИМВОЛ: CutsceneEngine не инициализирован!");
            return;
        }
        CUTSCENE.play(id, onEnd);
    }

    /**
     * Остановить текущую катсцену.
     */
    public static void stopCutscene() {
        if (CUTSCENE != null) {
            CUTSCENE.stop();
        }
    }

    /**
     * Проверить — идёт ли катсцена прямо сейчас.
     */
    public static boolean isCutscenePlaying() {
        return CUTSCENE != null && CUTSCENE.isPlaying();
    }

    /**
     * Получить текущий уровень паранойи (0-10).
     * Удобно для условий в диалогах и рендере.
     */
    public static int getParanoiaLevel() {
        return Simvol.PARANOIA != null
            ? Simvol.PARANOIA.getLevel()
            : 0;
    }

    /**
     * Открыть меню Палочки Режиссёра.
     */
    public static void openWandMenu() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.setScreen(new WandScreen());
        }
    }
}
