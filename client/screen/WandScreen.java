package com.simvol.mod.client.screen;

import com.simvol.mod.SimvolClient;
import com.simvol.mod.director.DirectorSystem;
import com.simvol.mod.director.RecordedFrame;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/**
 * ГЛАВНОЕ МЕНЮ ПАЛОЧКИ РЕЖИССЁРА
 * =================================
 * Состоит из трёх вкладок:
 *
 * [КАТСЦЕНЫ] — список всех катсцен, создание/редактирование/удаление
 * [РЕДАКТОР] — редактирование кадров текущей катсцены
 * [ИНСТРУМЕНТЫ] — расстановка NPC, кнопок, настройка блоков
 */
public class WandScreen extends Screen {

    // ── Вкладки ───────────────────────────────────────────────

    private enum Tab { CUTSCENES, EDITOR, TOOLS }
    private Tab currentTab = Tab.CUTSCENES;

    // ── Цвета (тёмный винтажный стиль) ───────────────────────

    private static final int BG_COLOR        = 0xE8000000; // Основной фон
    private static final int PANEL_COLOR      = 0xDD1A1208; // Панели (тёмно-коричневый)
    private static final int ACCENT_COLOR     = 0xFFB8860B; // Акцент (тёмное золото)
    private static final int TEXT_MAIN        = 0xFFEEDDCC; // Основной текст
    private static final int TEXT_DIM         = 0xFF887766; // Приглушённый текст
    private static final int SELECTED_COLOR   = 0xFF2D1F0A; // Выбранный элемент
    private static final int BORDER_COLOR     = 0xFF6B4F2A; // Граница
    private static final int FRAME_ITEM_COLOR = 0xFF1E150A; // Фон элемента кадра
    private static final int BUTTON_COLOR     = 0xFF3D2810; // Кнопки
    private static final int BUTTON_HOVER     = 0xFF5A3C18; // Кнопки (hover)
    private static final int SUCCESS_COLOR    = 0xFF3A7A3A; // Зелёный
    private static final int DANGER_COLOR     = 0xFF7A2020; // Красный

    // ── Размеры ───────────────────────────────────────────────

    private int guiWidth, guiHeight;
    private int guiX, guiY;

    // ── Вкладка КАТСЦЕНЫ ──────────────────────────────────────

    private TextFieldWidget newCutsceneIdField;
    private int cutsceneListScroll = 0;
    private String selectedCutsceneId = "";

    // ── Вкладка РЕДАКТОР ──────────────────────────────────────

    private int frameListScroll    = 0;
    private TextFieldWidget durationField;
    private TextFieldWidget actionIdField;
    private TextFieldWidget actionArgField;

    // ── Состояние ─────────────────────────────────────────────

    private String statusMessage   = "";
    private int    statusTimer     = 0;
    private boolean statusIsError  = false;

    // =========================================================

    public WandScreen() {
        super(Text.literal("Палочка Режиссёра"));
    }

    // =========================================================
    //  ИНИЦИАЛИЗАЦИЯ
    // =========================================================

    @Override
    protected void init() {
        // Размер окна — 85% экрана
        guiWidth  = Math.min((int)(this.width  * 0.85), 520);
        guiHeight = Math.min((int)(this.height * 0.85), 380);
        guiX = (this.width  - guiWidth)  / 2;
        guiY = (this.height - guiHeight) / 2;

        buildWidgets();
    }

    private void buildWidgets() {
        clearChildren();

        int tabY = guiY + 28;

        // ── Кнопки вкладок ────────────────────────────────────

        addDrawableChild(makeTabButton("КАТСЦЕНЫ",    guiX + 8,              tabY, Tab.CUTSCENES));
        addDrawableChild(makeTabButton("РЕДАКТОР",    guiX + 8 + 118,        tabY, Tab.EDITOR));
        addDrawableChild(makeTabButton("ИНСТРУМЕНТЫ", guiX + 8 + 118 + 118,  tabY, Tab.TOOLS));

        // ── Контент вкладки ───────────────────────────────────

        int contentY = guiY + 52;
        int contentH = guiHeight - 60;

        switch (currentTab) {
            case CUTSCENES  -> buildCutscenesTab(contentY, contentH);
            case EDITOR     -> buildEditorTab(contentY, contentH);
            case TOOLS      -> buildToolsTab(contentY, contentH);
        }
    }

    // =========================================================
    //  ВКЛАДКА: КАТСЦЕНЫ
    // =========================================================

    private void buildCutscenesTab(int contentY, int contentH) {
        DirectorSystem dir = SimvolClient.DIRECTOR_GUI;
        int x = guiX + 8;
        int w = guiWidth - 16;

        // ── Поле для ID новой катсцены ────────────────────────

        newCutsceneIdField = new TextFieldWidget(
            textRenderer,
            x, contentY,
            w - 90, 20,
            Text.literal("ID катсцены")
        );
        newCutsceneIdField.setMaxLength(64);
        newCutsceneIdField.setPlaceholder(Text.literal("Введи ID, например: act0_fired")
            .formatted(Formatting.DARK_GRAY));
        if (!dir.getCurrentId().isEmpty()) {
            newCutsceneIdField.setText(dir.getCurrentId());
        }
        addDrawableChild(newCutsceneIdField);

        // ── Кнопка "Создать / Начать запись" ─────────────────

        addDrawableChild(ButtonWidget.builder(
            Text.literal(dir.isRecording() ? "⏺ Запись..." : "➕ Новая"),
            btn -> {
                String id = newCutsceneIdField.getText().trim();
                if (id.isEmpty()) {
                    showStatus("Введи ID катсцены!", true);
                    return;
                }
                dir.startRecording(id);
                showStatus("Запись начата: " + id, false);
                currentTab = Tab.EDITOR;
                buildWidgets();
            }
        ).dimensions(x + w - 85, contentY, 85, 20).build());

        int listY = contentY + 28;
        int listH = contentH - 60;

        // ── Список сохранённых катсцен ────────────────────────

        List<String> saved = new java.util.ArrayList<>(dir.getSavedCutscenes().keySet());

        if (saved.isEmpty()) {
            // Заглушка если катсцен нет
        } else {
            int itemH = 22;
            int visibleCount = listH / itemH;
            cutsceneListScroll = Math.max(0,
                Math.min(cutsceneListScroll, saved.size() - visibleCount));

            for (int i = cutsceneListScroll;
                 i < Math.min(saved.size(), cutsceneListScroll + visibleCount); i++) {

                String csId    = saved.get(i);
                int    itemIdx = i;
                int    iy      = listY + (i - cutsceneListScroll) * itemH;
                var    frames  = dir.getSavedCutscenes().get(csId);
                float  durSec  = frames != null
                    ? frames.stream().mapToInt(RecordedFrame::getDurationTicks).sum() / 20f
                    : 0f;

                // Кнопка выбора катсцены
                addDrawableChild(ButtonWidget.builder(
                    Text.literal(csId + "  §7(" + frames.size() + " кадров, " +
                        String.format("%.1f", durSec) + "с)"),
                    btn -> {
                        selectedCutsceneId = csId;
                        buildWidgets();
                    }
                ).dimensions(x, iy, w - 120, itemH - 2).build());

                // Кнопка Редактировать
                addDrawableChild(ButtonWidget.builder(
                    Text.literal("✏"),
                    btn -> {
                        dir.loadForEditing(csId);
                        currentTab = Tab.EDITOR;
                        buildWidgets();
                        showStatus("Редактирование: " + csId, false);
                    }
                ).dimensions(x + w - 118, iy, 38, itemH - 2).build());

                // Кнопка Предпросмотр
                addDrawableChild(ButtonWidget.builder(
                    Text.literal("▶"),
                    btn -> {
                        dir.loadForEditing(csId);
                        dir.previewCurrent(this.client);
                    }
                ).dimensions(x + w - 78, iy, 38, itemH - 2).build());

                // Кнопка Удалить
                addDrawableChild(ButtonWidget.builder(
                    Text.literal("✗").formatted(Formatting.RED),
                    btn -> {
                        com.simvol.mod.director.CutsceneSaver.delete(csId);
                        dir.getSavedCutscenes().remove(csId);
                        showStatus("Удалено: " + csId, true);
                        buildWidgets();
                    }
                ).dimensions(x + w - 38, iy, 38, itemH - 2).build());
            }
        }

        // ── Кнопки внизу ──────────────────────────────────────

        int bottomY = guiY + guiHeight - 28;

        // Сохранить текущую
        addDrawableChild(ButtonWidget.builder(
            Text.literal("💾 Сохранить текущую"),
            btn -> {
                boolean ok = dir.saveCurrent(this.client);
                showStatus(ok ? "Сохранено!" : "Ошибка сохранения", !ok);
                if (ok) buildWidgets();
            }
        ).dimensions(x, bottomY, 140, 20).build());

        // Закрыть
        addDrawableChild(ButtonWidget.builder(
            Text.literal("✕ Закрыть"),
            btn -> this.close()
        ).dimensions(guiX + guiWidth - 90, bottomY, 82, 20).build());
    }

    // =========================================================
    //  ВКЛАДКА: РЕДАКТОР КАДРОВ
    // =========================================================

    private void buildEditorTab(int contentY, int contentH) {
        DirectorSystem dir = SimvolClient.DIRECTOR_GUI;
        int x = guiX + 8;
        int w = guiWidth - 16;

        // ── Статус записи ─────────────────────────────────────

        // Кнопка "Записать кадр сейчас"
        addDrawableChild(ButtonWidget.builder(
            Text.literal("⦿ Записать кадр (или SHIFT+ПКМ)"),
            btn -> dir.recordFrameNow(this.client)
        ).dimensions(x, contentY, 200, 20).build());

        // Кнопка "Предпросмотр"
        addDrawableChild(ButtonWidget.builder(
            Text.literal("▶ Предпросмотр"),
            btn -> dir.previewCurrent(this.client)
        ).dimensions(x + 205, contentY, 120, 20).build());

        // Кнопка "Удалить последний"
        addDrawableChild(ButtonWidget.builder(
            Text.literal("✗ Удалить последний").formatted(Formatting.RED),
            btn -> {
                dir.removeLastFrame(this.client);
                buildWidgets();
            }
        ).dimensions(x + 330, contentY, 160, 20).build());

        // ── Список кадров ─────────────────────────────────────

        int listY  = contentY + 28;
        int listH  = contentH - 100;
        int itemH  = 22;
        int visible = listH / itemH;

        List<RecordedFrame> frames = dir.getFrames();
        frameListScroll = Math.max(0,
            Math.min(frameListScroll, Math.max(0, frames.size() - visible)));

        if (frames.isEmpty()) {
            // Инструкция
        } else {
            for (int i = frameListScroll;
                 i < Math.min(frames.size(), frameListScroll + visible); i++) {

                RecordedFrame frame = frames.get(i);
                int idx = i;
                int iy  = listY + (i - frameListScroll) * itemH;
                boolean selected = (dir.getSelectedIndex() == i);

                // Кнопка выбора кадра
                addDrawableChild(ButtonWidget.builder(
                    Text.literal(frame.getSummary(i)),
                    btn -> {
                        dir.setSelectedIndex(idx);
                        updateFieldsForFrame(frames.get(idx));
                        buildWidgets();
                    }
                ).dimensions(x, iy, w - 80, itemH - 2).build());

                // Кнопки вверх/вниз
                addDrawableChild(ButtonWidget.builder(
                    Text.literal("↑"),
                    btn -> { dir.moveFrameUp(idx); buildWidgets(); }
                ).dimensions(x + w - 78, iy, 38, itemH - 2).build());

                addDrawableChild(ButtonWidget.builder(
                    Text.literal("↓"),
                    btn -> { dir.moveFrameDown(idx); buildWidgets(); }
                ).dimensions(x + w - 38, iy, 38, itemH - 2).build());
            }
        }

        // ── Редактирование выбранного кадра ───────────────────

        int editY = listY + listH + 5;
        int selIdx = dir.getSelectedIndex();

        if (selIdx >= 0 && selIdx < frames.size()) {
            RecordedFrame selFrame = frames.get(selIdx);

            // Длительность
            durationField = new TextFieldWidget(
                textRenderer, x, editY, 80, 18,
                Text.literal("Длительность")
            );
            durationField.setMaxLength(6);
            durationField.setText(String.valueOf(selFrame.getDurationTicks()));
            addDrawableChild(durationField);

            addDrawableChild(ButtonWidget.builder(
                Text.literal("✓ тиков"),
                btn -> {
                    try {
                        int ticks = Integer.parseInt(durationField.getText().trim());
                        dir.setFrameDuration(selIdx, ticks);
                        showStatus("Длительность: " + ticks + " тиков (" +
                            String.format("%.1f", ticks / 20f) + "с)", false);
                    } catch (NumberFormatException e) {
                        showStatus("Введи число!", true);
                    }
                }
            ).dimensions(x + 85, editY, 80, 18).build());

            // Действие
            actionIdField = new TextFieldWidget(
                textRenderer, x + 175, editY, 100, 18,
                Text.literal("Действие")
            );
            actionIdField.setMaxLength(32);
            actionIdField.setText(selFrame.getActionId());
            actionIdField.setPlaceholder(
                Text.literal("dialogue/sound/flag...").formatted(Formatting.DARK_GRAY));
            addDrawableChild(actionIdField);

            actionArgField = new TextFieldWidget(
                textRenderer, x + 280, editY, 140, 18,
                Text.literal("Аргумент")
            );
            actionArgField.setMaxLength(128);
            actionArgField.setText(selFrame.getActionArg());
            actionArgField.setPlaceholder(
                Text.literal("ID диалога / звука / флага").formatted(Formatting.DARK_GRAY));
            addDrawableChild(actionArgField);

            addDrawableChild(ButtonWidget.builder(
                Text.literal("✓"),
                btn -> {
                    dir.setFrameAction(selIdx,
                        actionIdField.getText().trim(),
                        actionArgField.getText().trim());
                    showStatus("Действие сохранено", false);
                }
            ).dimensions(x + w - 30, editY, 22, 18).build());
        }

        // ── Кнопка сохранения ─────────────────────────────────

        int bottomY = guiY + guiHeight - 28;

        addDrawableChild(ButtonWidget.builder(
            Text.literal("💾 Сохранить катсцену"),
            btn -> {
                boolean ok = dir.saveCurrent(this.client);
                showStatus(ok ? "✓ Сохранено!" : "✗ Ошибка!", !ok);
                if (ok) {
                    currentTab = Tab.CUTSCENES;
                    buildWidgets();
                }
            }
        ).dimensions(x, bottomY, 150, 20).build());

        // Общая длительность
        addDrawableChild(ButtonWidget.builder(
            Text.literal("Итого: " + String.format("%.1f", dir.getTotalDurationSeconds()) + " сек"),
            btn -> {}
        ).dimensions(x + 155, bottomY, 140, 20).build());
    }

    // =========================================================
    //  ВКЛАДКА: ИНСТРУМЕНТЫ
    // =========================================================

    private void buildToolsTab(int contentY, int contentH) {
        int x = guiX + 8;
        int w = guiWidth - 16;

        // ── Раздел: NPC ───────────────────────────────────────

        int y = contentY;

        addDrawableChild(ButtonWidget.builder(
            Text.literal("👤 Поставить NPC — Нина"),
            btn -> spawnNPC("npc_nina")
        ).dimensions(x, y, 180, 20).build());

        addDrawableChild(ButtonWidget.builder(
            Text.literal("👤 Поставить NPC — Рашид"),
            btn -> spawnNPC("npc_rashid")
        ).dimensions(x + 185, y, 180, 20).build());

        y += 24;

        addDrawableChild(ButtonWidget.builder(
            Text.literal("👤 NPC — Толя"),
            btn -> spawnNPC("npc_tolya")
        ).dimensions(x, y, 180, 20).build());

        addDrawableChild(ButtonWidget.builder(
            Text.literal("👤 NPC — Семёныч"),
            btn -> spawnNPC("npc_semyonych")
        ).dimensions(x + 185, y, 180, 20).build());

        y += 24;

        addDrawableChild(ButtonWidget.builder(
            Text.literal("👤 NPC — Громов"),
            btn -> spawnNPC("npc_gromov")
        ).dimensions(x, y, 180, 20).build());

        addDrawableChild(ButtonWidget.builder(
            Text.literal("👤 NPC — Валерия"),
            btn -> spawnNPC("npc_valeria")
        ).dimensions(x + 185, y, 180, 20).build());

        y += 24;

        addDrawableChild(ButtonWidget.builder(
            Text.literal("👤 NPC — Начальник"),
            btn -> spawnNPC("npc_boss")
        ).dimensions(x, y, 180, 20).build());

        addDrawableChild(ButtonWidget.builder(
            Text.literal("👤 NPC — Детектив (катсцена)"),
            btn -> spawnNPC("npc_detective")
        ).dimensions(x + 185, y, 180, 20).build());

        // ── Раздел: 3D кнопки ─────────────────────────────────

        y += 32;

        addDrawableChild(ButtonWidget.builder(
            Text.literal("🔲 Большая кнопка НАЧАТЬ"),
            btn -> spawnButton("button_start")
        ).dimensions(x, y, 180, 20).build());

        addDrawableChild(ButtonWidget.builder(
            Text.literal("🔳 Кнопка выбора"),
            btn -> spawnButton("button_choice")
        ).dimensions(x + 185, y, 180, 20).build());

        // ── Раздел: Утилиты ───────────────────────────────────

        y += 32;

        addDrawableChild(ButtonWidget.builder(
            Text.literal("📷 Телепорт к кадру #1"),
            btn -> teleportToFirstFrame()
        ).dimensions(x, y, 180, 20).build());

        addDrawableChild(ButtonWidget.builder(
            Text.literal("🎬 Запустить катсцену по ID"),
            btn -> openPlayCutsceneDialog()
        ).dimensions(x + 185, y, 180, 20).build());
    }

    // =========================================================
    //  РЕНДЕР
    // =========================================================

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Затемнение фона
        renderBackground(context, mouseX, mouseY, delta);

        // ── Основная панель ───────────────────────────────────

        context.fill(guiX, guiY, guiX + guiWidth, guiY + guiHeight, BG_COLOR);

        // Граница
        drawBorder(context, guiX, guiY, guiWidth, guiHeight, BORDER_COLOR);

        // ── Заголовок ─────────────────────────────────────────

        context.fill(guiX, guiY, guiX + guiWidth, guiY + 22, PANEL_COLOR);
        context.drawCenteredTextWithShadow(textRenderer,
            Text.literal("✦  ПАЛОЧКА РЕЖИССЁРА  ✦").formatted(Formatting.GOLD),
            guiX + guiWidth / 2, guiY + 7, TEXT_MAIN);

        // ── Индикатор записи ──────────────────────────────────

        DirectorSystem dir = SimvolClient.DIRECTOR_GUI;
        if (dir.isRecording()) {
            String recText = "⏺ ЗАПИСЬ: " + dir.getCurrentId() +
                "  [" + dir.getFrames().size() + " кадров]";
            context.drawTextWithShadow(textRenderer,
                Text.literal(recText).formatted(Formatting.RED),
                guiX + 8, guiY + guiHeight - 12, 0xFFFF4444);
        }

        // ── Вкладки — подчёркивание активной ─────────────────

        int tabY  = guiY + 28;
        int tabX  = guiX + 8;
        int tabW  = 115;

        for (int i = 0; i < 3; i++) {
            Tab t = Tab.values()[i];
            if (t == currentTab) {
                context.fill(tabX + i * (tabW + 3), tabY + 18,
                             tabX + i * (tabW + 3) + tabW, tabY + 20,
                             ACCENT_COLOR);
            }
        }

        // ── Пустой список катсцен — подсказка ─────────────────

        if (currentTab == Tab.CUTSCENES &&
                dir.getSavedCutscenes().isEmpty()) {
            drawCenteredMultiline(context,
                new String[]{
                    "Катсцен пока нет.",
                    "Введи ID и нажми «Новая» чтобы начать запись.",
                    "Затем летай по карте и жми SHIFT+ПКМ",
                    "чтобы записывать точки камеры."
                },
                guiX + guiWidth / 2,
                guiY + 130,
                TEXT_DIM
            );
        }

        // ── Пустой список кадров — подсказка ─────────────────

        if (currentTab == Tab.EDITOR && dir.getFrames().isEmpty()) {
            drawCenteredMultiline(context,
                new String[]{
                    "Нет записанных кадров.",
                    "Нажми «Записать кадр» или используй",
                    "SHIFT+ПКМ находясь в нужной точке."
                },
                guiX + guiWidth / 2,
                guiY + 140,
                TEXT_DIM
            );
        }

        // ── Статусное сообщение ───────────────────────────────

        if (statusTimer > 0) {
            statusTimer--;
            int alpha = Math.min(255, statusTimer * 8);
            int color = (alpha << 24) | (statusIsError ? 0xFF4444 : 0x44FF44);
            context.drawCenteredTextWithShadow(textRenderer,
                Text.literal(statusMessage), guiX + guiWidth / 2,
                guiY + guiHeight - 28, color);
        }

        // Дочерние виджеты (кнопки, поля)
        super.render(context, mouseX, mouseY, delta);
    }

    // =========================================================
    //  ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // =========================================================

    private ButtonWidget makeTabButton(String label, int x, int y, Tab tab) {
        return ButtonWidget.builder(
            Text.literal(label),
            btn -> {
                currentTab = tab;
                buildWidgets();
            }
        ).dimensions(x, y, 115, 18).build();
    }

    private void drawBorder(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x,         y,         x + w,     y + 1,     color); // верх
        ctx.fill(x,         y + h - 1, x + w,     y + h,     color); // низ
        ctx.fill(x,         y,         x + 1,     y + h,     color); // лево
        ctx.fill(x + w - 1, y,         x + w,     y + h,     color); // право
    }

    private void drawCenteredMultiline(DrawContext ctx, String[] lines,
                                        int cx, int startY, int color) {
        for (int i = 0; i < lines.length; i++) {
            ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal(lines[i]), cx, startY + i * 12, color);
        }
    }

    private void showStatus(String msg, boolean isError) {
        statusMessage = msg;
        statusTimer   = 120; // 6 секунд
        statusIsError = isError;
    }

    private void updateFieldsForFrame(RecordedFrame frame) {
        if (durationField  != null) durationField.setText(String.valueOf(frame.getDurationTicks()));
        if (actionIdField  != null) actionIdField.setText(frame.getActionId());
        if (actionArgField != null) actionArgField.setText(frame.getActionArg());
    }

    private void spawnNPC(String typeId) {
        if (this.client == null || this.client.player == null) return;
        // Отправляем команду на сервер для спавна NPC
        this.client.player.networkHandler.sendChatCommand(
            "summon simvol:" + typeId + " ~ ~ ~"
        );
        showStatus("NPC '" + typeId + "' заспавнен", false);
    }

    private void spawnButton(String typeId) {
        if (this.client == null || this.client.player == null) return;
        this.client.player.networkHandler.sendChatCommand(
            "summon simvol:" + typeId + " ~ ~1 ~"
        );
        showStatus("Кнопка '" + typeId + "' создана", false);
    }

    private void teleportToFirstFrame() {
        DirectorSystem dir = SimvolClient.DIRECTOR_GUI;
        if (dir.getFrames().isEmpty()) {
            showStatus("Нет кадров!", true);
            return;
        }
        RecordedFrame first = dir.getFrames().get(0);
        if (this.client != null && this.client.player != null) {
            this.client.player.networkHandler.sendChatCommand(
                String.format("tp @s %.2f %.2f %.2f", first.x, first.y, first.z)
            );
        }
        showStatus("Телепорт к первому кадру", false);
    }

    private void openPlayCutsceneDialog() {
        // TODO: открыть диалог ввода ID катсцены для воспроизведения
        showStatus("Используй: /simvol play <id>", false);
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // ESC — закрыть (или остановить предпросмотр)
        if (keyCode == 256) {
            DirectorSystem dir = SimvolClient.DIRECTOR_GUI;
            if (dir.isPreviewing()) {
                dir.stopPreview(this.client);
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
