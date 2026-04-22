package com.simvol.mod.director;

import com.simvol.mod.Simvol;
import com.simvol.mod.cutscene.CutsceneDef;
import com.simvol.mod.cutscene.CutsceneFrame;
import com.simvol.mod.cutscene.CutsceneAction;
import com.simvol.mod.SimvolClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

/**
 * СИСТЕМА РЕЖИССЁРА
 * ==================
 * Хранит текущую сессию записи катсцены.
 * Управляет записью кадров, предпросмотром, сохранением.
 *
 * WORKFLOW:
 * 1. Открыть меню → "Новая катсцена" → ввести ID
 * 2. Летать по карте, нажимать SHIFT+ПКМ в каждой точке
 * 3. Для каждого кадра задать: длительность, действие
 * 4. "Предпросмотр" → смотришь как выглядит
 * 5. "Сохранить" → катсцена готова
 */
public class DirectorSystem {

    // ── Текущая сессия ────────────────────────────────────────

    /** ID катсцены которую сейчас пишем */
    private String currentCutsceneId = "";

    /** Записанные кадры (в порядке добавления) */
    private final List<RecordedFrame> recordedFrames = new ArrayList<>();

    /** Сессия активна */
    private boolean isRecording = false;

    /** Выбранный кадр в редакторе */
    private int selectedFrameIndex = -1;

    // ── Все сохранённые катсцены ──────────────────────────────

    /** id → список кадров */
    private final Map<String, List<RecordedFrame>> savedCutscenes = new LinkedHashMap<>();

    // ── Предпросмотр ──────────────────────────────────────────

    private boolean isPreviewing = false;

    // =========================================================
    //  ЗАПИСЬ КАДРОВ
    // =========================================================

    /**
     * Начать новую сессию записи.
     * @param cutsceneId ID новой катсцены
     */
    public void startRecording(String cutsceneId) {
        currentCutsceneId = cutsceneId;
        recordedFrames.clear();
        isRecording = true;
        selectedFrameIndex = -1;
        Simvol.LOGGER.info("РЕЖИССЁР: Начата запись катсцены '" + cutsceneId + "'");
    }

    /**
     * Записать текущую позицию и поворот камеры как новый кадр.
     * Вызывается при SHIFT+ПКМ с Палочкой.
     */
    public void recordFrameNow(MinecraftClient client) {
        if (!isRecording) {
            // Если запись не активна — предупреждаем
            if (client.player != null) {
                client.player.sendMessage(
                    Text.literal("⚠ Сначала начни запись катсцены в меню палочки!")
                        .formatted(Formatting.RED), true
                );
            }
            return;
        }

        ClientPlayerEntity player = client.player;
        if (player == null) return;

        // Берём позицию КАМЕРЫ (не игрока)
        // Камера немного выше глаз игрока
        double camX = player.getX();
        double camY = player.getY() + player.getEyeHeight(player.getPose());
        double camZ = player.getZ();
        float  yaw   = player.getYaw();
        float  pitch = player.getPitch();

        // Создаём кадр с дефолтной длительностью 60 тиков (3 сек)
        RecordedFrame frame = new RecordedFrame(
            camX, camY, camZ,
            yaw, pitch,
            60,       // длительность по умолчанию — можно изменить в редакторе
            "",       // действие — пусто (можно добавить в редакторе)
            null      // текст действия
        );

        recordedFrames.add(frame);
        selectedFrameIndex = recordedFrames.size() - 1;

        // Сообщение игроку
        player.sendMessage(
            Text.literal("✓ Кадр #" + recordedFrames.size() + " записан")
                .formatted(Formatting.GREEN)
                .append(Text.literal("  [" +
                    String.format("%.1f", camX) + ", " +
                    String.format("%.1f", camY) + ", " +
                    String.format("%.1f", camZ) + "]")
                    .formatted(Formatting.DARK_GRAY)),
            true  // показывать в actionbar (над хотбаром)
        );

        Simvol.LOGGER.info("РЕЖИССЁР: Кадр #" + recordedFrames.size() +
            " записан на " + String.format("%.1f/%.1f/%.1f", camX, camY, camZ));
    }

    /**
     * Удалить последний записанный кадр (ЛКМ с Палочкой).
     */
    public void removeLastFrame(MinecraftClient client) {
        if (!isRecording || recordedFrames.isEmpty()) return;

        recordedFrames.remove(recordedFrames.size() - 1);
        selectedFrameIndex = recordedFrames.size() - 1;

        if (client.player != null) {
            client.player.sendMessage(
                Text.literal("✗ Последний кадр удалён. Осталось: " + recordedFrames.size())
                    .formatted(Formatting.RED),
                true
            );
        }
    }

    /**
     * Удалить кадр по индексу.
     */
    public void removeFrame(int index) {
        if (index < 0 || index >= recordedFrames.size()) return;
        recordedFrames.remove(index);
        if (selectedFrameIndex >= recordedFrames.size()) {
            selectedFrameIndex = recordedFrames.size() - 1;
        }
    }

    /**
     * Изменить длительность кадра.
     */
    public void setFrameDuration(int index, int ticks) {
        if (index < 0 || index >= recordedFrames.size()) return;
        recordedFrames.get(index).setDurationTicks(ticks);
    }

    /**
     * Установить действие для кадра.
     * @param index     индекс кадра
     * @param actionId  ID действия (например "dialogue:act0_fired")
     * @param actionArg аргумент (например ID диалога)
     */
    public void setFrameAction(int index, String actionId, String actionArg) {
        if (index < 0 || index >= recordedFrames.size()) return;
        recordedFrames.get(index).setActionId(actionId);
        recordedFrames.get(index).setActionArg(actionArg);
    }

    /**
     * Переместить кадр вверх в списке (изменить порядок).
     */
    public void moveFrameUp(int index) {
        if (index <= 0 || index >= recordedFrames.size()) return;
        Collections.swap(recordedFrames, index, index - 1);
        selectedFrameIndex = index - 1;
    }

    /**
     * Переместить кадр вниз в списке.
     */
    public void moveFrameDown(int index) {
        if (index < 0 || index >= recordedFrames.size() - 1) return;
        Collections.swap(recordedFrames, index, index + 1);
        selectedFrameIndex = index + 1;
    }

    // =========================================================
    //  СОХРАНЕНИЕ И ЗАГРУЗКА
    // =========================================================

    /**
     * Сохранить текущую сессию как готовую катсцену.
     * После сохранения — катсцена доступна для воспроизведения.
     */
    public boolean saveCurrent(MinecraftClient client) {
        if (currentCutsceneId.isEmpty()) {
            if (client.player != null) {
                client.player.sendMessage(
                    Text.literal("⚠ Задай ID катсцены перед сохранением!")
                        .formatted(Formatting.RED), false
                );
            }
            return false;
        }

        if (recordedFrames.isEmpty()) {
            if (client.player != null) {
                client.player.sendMessage(
                    Text.literal("⚠ Нет записанных кадров!")
                        .formatted(Formatting.RED), false
                );
            }
            return false;
        }

        // Сохраняем копию
        savedCutscenes.put(currentCutsceneId, new ArrayList<>(recordedFrames));

        // Конвертируем в CutsceneDef и регистрируем в движке
        CutsceneDef def = buildCutsceneDef(currentCutsceneId, recordedFrames);
        SimvolClient.CUTSCENE.register(currentCutsceneId, def);

        // Сохраняем в файл
        CutsceneSaver.save(currentCutsceneId, recordedFrames);

        if (client.player != null) {
            client.player.sendMessage(
                Text.literal("✓ Катсцена '" + currentCutsceneId + "' сохранена! " +
                    "(" + recordedFrames.size() + " кадров, " +
                    String.format("%.1f", getTotalDurationSeconds()) + " сек)")
                    .formatted(Formatting.GREEN), false
            );
        }

        isRecording = false;
        Simvol.LOGGER.info("РЕЖИССЁР: Катсцена '" + currentCutsceneId + "' сохранена.");
        return true;
    }

    /**
     * Загрузить все сохранённые катсцены из файлов.
     * Вызывается при старте клиента.
     */
    public void loadAll(MinecraftClient client) {
        Map<String, List<RecordedFrame>> loaded = CutsceneSaver.loadAll();
        savedCutscenes.putAll(loaded);

        // Регистрируем все загруженные катсцены в движке
        for (Map.Entry<String, List<RecordedFrame>> entry : loaded.entrySet()) {
            CutsceneDef def = buildCutsceneDef(entry.getKey(), entry.getValue());
            SimvolClient.CUTSCENE.register(entry.getKey(), def);
        }

        Simvol.LOGGER.info("РЕЖИССЁР: Загружено " + loaded.size() + " катсцен из файлов.");
    }

    /**
     * Загрузить существующую катсцену для редактирования.
     */
    public boolean loadForEditing(String id) {
        List<RecordedFrame> frames = savedCutscenes.get(id);
        if (frames == null) return false;

        currentCutsceneId = id;
        recordedFrames.clear();
        recordedFrames.addAll(frames);
        isRecording = true;
        selectedFrameIndex = recordedFrames.isEmpty() ? -1 : 0;
        return true;
    }

    // =========================================================
    //  ПРЕДПРОСМОТР
    // =========================================================

    /**
     * Запустить предпросмотр текущей записанной катсцены.
     */
    public void previewCurrent(MinecraftClient client) {
        if (recordedFrames.isEmpty()) {
            if (client.player != null) {
                client.player.sendMessage(
                    Text.literal("⚠ Нет кадров для предпросмотра!")
                        .formatted(Formatting.RED), false
                );
            }
            return;
        }

        // Строим временную CutsceneDef
        CutsceneDef previewDef = buildCutsceneDef("__preview__", recordedFrames);
        SimvolClient.CUTSCENE.register("__preview__", previewDef);

        isPreviewing = true;

        // Закрываем GUI и запускаем катсцену
        client.setScreen(null);
        SimvolClient.CUTSCENE.play("__preview__", () -> {
            isPreviewing = false;
            // После предпросмотра — снова открываем меню палочки
            client.setScreen(new com.simvol.mod.client.screen.WandScreen());
        });
    }

    /**
     * Остановить предпросмотр досрочно (клавиша ESC).
     */
    public void stopPreview(MinecraftClient client) {
        if (!isPreviewing) return;
        SimvolClient.CUTSCENE.stop();
        isPreviewing = false;
        client.setScreen(new com.simvol.mod.client.screen.WandScreen());
    }

    // =========================================================
    //  КОНВЕРТАЦИЯ RecordedFrame → CutsceneDef
    // =========================================================

    /**
     * Конвертирует список RecordedFrame в готовый CutsceneDef.
     * Здесь же подставляются CutsceneAction по их ID.
     */
    private CutsceneDef buildCutsceneDef(String id, List<RecordedFrame> frames) {
        CutsceneDef.Builder builder = CutsceneDef.builder(id);

        for (RecordedFrame rf : frames) {
            CutsceneAction action = resolveAction(rf.getActionId(), rf.getActionArg());

            CutsceneFrame frame = (action != null)
                ? CutsceneFrame.of(rf.x, rf.y, rf.z, rf.yaw, rf.pitch,
                                   rf.getDurationTicks(), action)
                : CutsceneFrame.of(rf.x, rf.y, rf.z, rf.yaw, rf.pitch,
                                   rf.getDurationTicks());

            builder.frame(frame);
        }

        return builder.build();
    }

    /**
     * Преобразует строковый ID действия в CutsceneAction объект.
     *
     * Форматы actionId:
     *   "dialogue:act0_fired"           → CutsceneAction.dialogue("act0_fired")
     *   "sound:simvol:sfx/door_heavy"   → CutsceneAction.sound("simvol:sfx/door_heavy")
     *   "title:АКТ 1"                   → CutsceneAction.showTitle("АКТ 1")
     *   "subtitle:СТЕКЛО"               → CutsceneAction.showSubtitle("СТЕКЛО")
     *   "flag:ACT0_FIRED"               → CutsceneAction.setFlag(StoryFlag.ACT0_FIRED)
     *   "paranoia:2"                    → CutsceneAction.paranoia(2)
     *   "fade_black:40"                 → CutsceneAction.fadeToBlack(40)
     *   "letterbox:true"                → CutsceneAction.letterbox(true)
     *   "end"                           → CutsceneAction.end()
     *   ""  или null                    → null (нет действия)
     */
    public CutsceneAction resolveAction(String actionId, String actionArg) {
        if (actionId == null || actionId.isEmpty()) return null;

        return switch (actionId) {
            case "dialogue"   -> CutsceneAction.dialogue(actionArg);
            case "sound"      -> CutsceneAction.sound(actionArg);
            case "title"      -> CutsceneAction.showTitle(actionArg);
            case "subtitle"   -> CutsceneAction.showSubtitle(actionArg);
            case "fade_black" -> CutsceneAction.fadeToBlack(parseInt(actionArg, 40));
            case "letterbox"  -> CutsceneAction.letterbox("true".equals(actionArg));
            case "end"        -> CutsceneAction.end();
            case "paranoia"   -> CutsceneAction.paranoia(parseInt(actionArg, 1));
            case "flag"       -> {
                try {
                    var flag = com.simvol.mod.story.StoryFlag.valueOf(actionArg);
                    yield CutsceneAction.setFlag(flag);
                } catch (IllegalArgumentException e) {
                    Simvol.LOGGER.warn("РЕЖИССЁР: Неизвестный флаг: " + actionArg);
                    yield null;
                }
            }
            default -> {
                Simvol.LOGGER.warn("РЕЖИССЁР: Неизвестное действие: " + actionId);
                yield null;
            }
        };
    }

    // ── Утилиты ───────────────────────────────────────────────

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return def; }
    }

    public float getTotalDurationSeconds() {
        return recordedFrames.stream()
            .mapToInt(RecordedFrame::getDurationTicks)
            .sum() / 20f;
    }

    // ── Геттеры ───────────────────────────────────────────────

    public boolean isRecording()             { return isRecording; }
    public boolean isPreviewing()            { return isPreviewing; }
    public String getCurrentId()             { return currentCutsceneId; }
    public List<RecordedFrame> getFrames()   { return Collections.unmodifiableList(recordedFrames); }
    public int getSelectedIndex()            { return selectedFrameIndex; }
    public void setSelectedIndex(int i)      { selectedFrameIndex = i; }
    public Map<String, List<RecordedFrame>> getSavedCutscenes() { return savedCutscenes; }

    public void setCurrentId(String id) {
        this.currentCutsceneId = id;
    }

    public void stopRecording() {
        isRecording = false;
    }
}
