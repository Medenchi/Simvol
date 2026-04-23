package com.simvol.mod.dialogue;

import com.simvol.mod.Simvol;
import com.simvol.mod.SimvolClient;
import com.simvol.mod.story.StoryFlag;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.*;

/**
 * ДВИЖОК ДИАЛОГОВ
 * =================
 * Управляет деревом диалогов:
 *
 * DialogueNode (реплика)
 *   └── List<DialogueChoice> (варианты ответа)
 *         └── DialogueNode (следующая реплика)
 *               └── ...
 *
 * ЦИКЛ ЖИЗНИ ДИАЛОГА:
 * 1. startDialogue(id)     → находим корневой узел
 * 2. tick()                → SubtitleOverlay печатает текст
 * 3. (текст напечатан)     → показываем кнопки выбора (если есть)
 * 4. onChoiceSelected(i)   → переходим к следующему узлу
 * 5. (узел без выборов)    → авто-переход через N тиков
 * 6. (next == null)        → endDialogue()
 */
public class DialogueEngine {

    // =========================================================
    //  РЕЕСТР ДИАЛОГОВ
    // =========================================================

    /** Все зарегистрированные диалоги: id → корневой узел */
    private final Map<String, DialogueNode> registry = new HashMap<>();

    // =========================================================
    //  ТЕКУЩИЙ ДИАЛОГ
    // =========================================================

    private boolean      isActive      = false;
    private DialogueNode currentNode   = null;
    private Entity       currentSpeakerEntity = null;

    /** Таймер авто-перехода (считает тики после завершения печати) */
    private int autoAdvanceTimer = 0;

    /** Ждём ли авто-перехода */
    private boolean waitingForAutoAdvance = false;

    /** Ждём ли выбора игрока */
    private boolean waitingForChoice = false;

    /** Колбек по завершению диалога */
    private Runnable onDialogueEnd = null;

    // =========================================================
    //  ПУБЛИЧНОЕ API
    // =========================================================

    /**
     * Зарегистрировать диалог.
     * Вызывается при загрузке мода из файлов диалогов каждого акта.
     */
    public void register(String id, DialogueNode rootNode) {
        registry.put(id, rootNode);
        Simvol.LOGGER.info("ДИАЛОГ: Зарегистрирован '" + id + "'");
    }

    /**
     * Зарегистрировать несколько диалогов сразу.
     */
    public void registerAll(Map<String, DialogueNode> dialogues) {
        registry.putAll(dialogues);
        Simvol.LOGGER.info("ДИАЛОГ: Зарегистрировано " + dialogues.size() + " диалогов");
    }

    /**
     * Запустить диалог по ID.
     *
     * @param id             ID диалога (должен быть зарегистрирован)
     * @param speakerEntity  entity говорящего (может быть null для монологов)
     * @param player         игрок
     */
    public void startDialogue(String id, Entity speakerEntity, PlayerEntity player) {
        DialogueNode root = registry.get(id);
        if (root == null) {
            Simvol.LOGGER.error("ДИАЛОГ: Не найден диалог '" + id + "'");
            return;
        }

        currentSpeakerEntity = speakerEntity;
        isActive             = true;
        autoAdvanceTimer     = 0;
        waitingForAutoAdvance = false;
        waitingForChoice     = false;

        goToNode(root);
        Simvol.LOGGER.info("ДИАЛОГ: Запущен '" + id + "'");
    }

    /**
     * Запустить диалог с колбеком по завершению.
     */
    public void startDialogue(String id, Entity speakerEntity,
                               PlayerEntity player, Runnable onEnd) {
        onDialogueEnd = onEnd;
        startDialogue(id, speakerEntity, player);
    }

    /**
     * Завершить диалог досрочно.
     */
    public void endDialogue() {
        isActive              = false;
        currentNode           = null;
        currentSpeakerEntity  = null;
        waitingForAutoAdvance = false;
        waitingForChoice      = false;
        autoAdvanceTimer      = 0;

        // Убираем 3D кнопки выбора если они были
        hideChoiceButtons();

        // Вызываем колбек
        if (onDialogueEnd != null) {
            Runnable cb   = onDialogueEnd;
            onDialogueEnd = null;
            cb.run();
        }

        Simvol.LOGGER.info("ДИАЛОГ: Завершён");
    }

    // =========================================================
    //  ТИК
    // =========================================================

    public void tick(MinecraftClient client) {
        if (!isActive || currentNode == null) return;

        // Ждём пока SubtitleOverlay закончит печатать текст
        boolean typingDone = SimvolClient.SUBTITLES != null
            && SimvolClient.SUBTITLES.isTypingComplete();

        if (!typingDone) return; // Ещё печатаем — ничего не делаем

        // Текст напечатан — теперь либо ждём выбора, либо авто-переход

        if (waitingForChoice) {
            // Ждём нажатия на кнопку — ничего не делаем, кнопки уже показаны
            return;
        }

        if (!waitingForAutoAdvance) {
            // Первый тик после завершения печати
            waitingForAutoAdvance = true;
            autoAdvanceTimer      = 0;

            if (!currentNode.choices().isEmpty()) {
                // Есть выборы — показываем 3D кнопки
                waitingForChoice = true;
                showChoiceButtons(currentNode.choices(), client);
            }
            // Если нет выборов — просто ждём autoAdvanceTicks
        }

        if (!waitingForChoice) {
            // Авто-переход (нет выборов)
            autoAdvanceTimer++;
            if (autoAdvanceTimer >= currentNode.autoAdvanceTicks()) {
                advanceAuto();
            }
        }
    }

    // =========================================================
    //  ПЕРЕХОДЫ МЕЖДУ УЗЛАМИ
    // =========================================================

    /**
     * Перейти к узлу диалога.
     */
    private void goToNode(DialogueNode node) {
        currentNode           = node;
        autoAdvanceTimer      = 0;
        waitingForAutoAdvance = false;
        waitingForChoice      = false;

        // Передаём текст субтитрам
        if (SimvolClient.SUBTITLES != null) {
            SimvolClient.SUBTITLES.showDirect(
                node.speakerName(),
                node.text()
            );
        }

        // Запускаем голосовую линию
        if (node.voiceLine() != null && !node.voiceLine().isEmpty()) {
            if (SimvolClient.VOICE != null) {
                SimvolClient.VOICE.play(node.voiceLine());
            }
        }

        // Выполняем действие узла (если есть)
        if (node.onEnter() != null) {
            node.onEnter().run();
        }
    }

    /**
     * Авто-переход к следующему узлу (когда нет выборов).
     */
    private void advanceAuto() {
        if (currentNode == null) return;

        DialogueNode next = currentNode.next();
        if (next == null) {
            // Конец диалога
            endDialogue();
        } else {
            goToNode(next);
        }
    }

    /**
     * Игрок выбрал вариант ответа.
     * Вызывается из Button3DChoice при нажатии.
     *
     * @param choiceIndex индекс выбранного варианта (0, 1, 2, ...)
     */
    public void onChoiceSelected(int choiceIndex) {
        if (!isActive || currentNode == null) return;
        if (choiceIndex < 0 || choiceIndex >= currentNode.choices().size()) return;

        DialogueChoice choice = currentNode.choices().get(choiceIndex);

        // Убираем кнопки
        hideChoiceButtons();
        waitingForChoice = false;

        // Выполняем действие выбора
        if (choice.onSelect() != null) {
            choice.onSelect().run();
        }

        // Переходим к следующему узлу
        DialogueNode next = choice.next();
        if (next == null) {
            endDialogue();
        } else {
            goToNode(next);
        }
    }

    // =========================================================
    //  3D КНОПКИ ВЫБОРА
    // =========================================================

    /**
     * Показать 3D кнопки выбора рядом с игроком.
     * Кнопки парят в воздухе полукругом перед игроком.
     */
    private void showChoiceButtons(List<DialogueChoice> choices,
                                    MinecraftClient client) {
        if (client.player == null) return;

        // Позиция игрока
        double px = client.player.getX();
        double py = client.player.getY() + 1.5;
        double pz = client.player.getZ();
        float  yaw = client.player.getYaw();

        // Расставляем кнопки полукругом перед игроком
        int count  = choices.size();
        float arcSpreadDeg = Math.min(60f, count * 20f);
        float startAngle   = yaw - arcSpreadDeg / 2f;
        float stepAngle    = count > 1 ? arcSpreadDeg / (count - 1) : 0f;
        float radius       = 2.5f;

        for (int i = 0; i < count; i++) {
            DialogueChoice choice = choices.get(i);
            float angle = startAngle + stepAngle * i;
            float rad   = (float) Math.toRadians(angle);

            double bx = px + Math.sin(rad) * radius;
            double by = py + 0.3;
            double bz = pz + Math.cos(rad) * radius;

            // Спавним 3D кнопку выбора
            spawnChoiceButton(choice.text(), i, bx, by, bz, client);
        }

        // Блокируем движение игрока пока не выбрал
        lockPlayerMovement(true, client);
    }

    /**
     * Убрать все 3D кнопки выбора.
     */
    private void hideChoiceButtons() {
        // TODO: Button3DManager.clearChoiceButtons()
        // Разблокируем движение
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) lockPlayerMovement(false, client);
    }

    /**
     * Спавн одной 3D кнопки выбора.
     */
    private void spawnChoiceButton(String label, int index,
                                    double x, double y, double z,
                                    MinecraftClient client) {
        if (client.player == null) return;

        // Отправляем команду на сервер для спавна кнопки
        // Сервер создаёт entity, клиент её рендерит
        client.player.networkHandler.sendChatCommand(
            String.format("simvol:spawn_choice_button %d \"%s\" %.2f %.2f %.2f",
                index, label, x, y, z)
        );
    }

    /**
     * Заблокировать/разблокировать движение игрока.
     * Пока не выбрал — стоит неподвижно.
     */
    private void lockPlayerMovement(boolean lock, MinecraftClient client) {
        if (client.player == null) return;

        // Реализуется через Mixin на GameRenderer
        // Флаг читается в SimvolMixin.java
        com.simvol.mod.mixin.MovementLockHelper.setLocked(lock);
    }

    // =========================================================
    //  ГЕТТЕРЫ ДЛЯ SubtitleOverlay И CutsceneEngine
    // =========================================================

    public boolean isActive() {
        return isActive;
    }

    public String getCurrentSpeakerName() {
        if (currentNode == null) return null;
        return currentNode.speakerName();
    }

    /**
     * Полный текст текущей реплики (не обрезанный).
     * SubtitleOverlay сама обрезает по visibleChars.
     */
    public String getCurrentFullText() {
        if (currentNode == null) return null;
        return currentNode.text();
    }

    public List<DialogueChoice> getCurrentChoices() {
        if (currentNode == null) return List.of();
        return currentNode.choices();
    }

    public boolean isWaitingForChoice() {
        return waitingForChoice;
    }

    public Entity getCurrentSpeakerEntity() {
        return currentSpeakerEntity;
    }

    /**
     * Пропустить анимацию текста и/или перейти к следующей реплике.
     * Вызывается при нажатии пробела или ЛКМ.
     */
    public void skip() {
        if (!isActive) return;

        // Если текст ещё печатается — показываем весь сразу
        if (SimvolClient.SUBTITLES != null
                && !SimvolClient.SUBTITLES.isTypingComplete()) {
            SimvolClient.SUBTITLES.skipTyping();
            return;
        }

        // Если нет выборов и ждём авто-перехода — переходим сейчас
        if (waitingForAutoAdvance && !waitingForChoice) {
            advanceAuto();
        }
    }
}
