package com.simvol.mod.dialogue;

/**
 * Вариант ответа игрока в диалоге.
 * Отображается как 3D кнопка парящая в воздухе.
 *
 * ПРИМЕРЫ:
 *
 *   // Обычный выбор с переходом к следующему узлу
 *   DialogueChoice.of("Что за дело?", узелПроДело)
 *
 *   // Выбор с действием (установка флага)
 *   DialogueChoice.of("Расскажу про робу", узелПроРобу, () -> {
 *       Simvol.STORY.setFlag(StoryFlag.ACT2_TOLD_RASHID_ABOUT_ROBE);
 *   })
 *
 *   // Выбор заканчивающий диалог
 *   DialogueChoice.end("Понятно, спасибо.")
 *
 *   // Молчаливый выбор (без текста — отображается как "...")
 *   DialogueChoice.silent(следующийУзел)
 *
 *   // Выбор доступный только при определённом флаге
 *   DialogueChoice.conditional(
 *       "Я знаю о вашем отце...",
 *       узелПроОтца,
 *       () -> Simvol.STORY.hasFlag(StoryFlag.ACT1_FOUND_ROBE_ALIEVA)
 *   )
 */
public final class DialogueChoice {

    private final String       text;
    private final DialogueNode next;
    private final Runnable     onSelect;

    /**
     * Условие доступности выбора.
     * Если null — выбор всегда доступен.
     * Если возвращает false — кнопка не показывается.
     */
    private final java.util.function.BooleanSupplier condition;

    // ── Приватный конструктор ─────────────────────────────────

    private DialogueChoice(String text, DialogueNode next,
                            Runnable onSelect,
                            java.util.function.BooleanSupplier condition) {
        this.text      = text;
        this.next      = next;
        this.onSelect  = onSelect;
        this.condition = condition;
    }

    // ── Фабричные методы ──────────────────────────────────────

    /** Обычный выбор */
    public static DialogueChoice of(String text, DialogueNode next) {
        return new DialogueChoice(text, next, null, null);
    }

    /** Выбор с действием */
    public static DialogueChoice of(String text, DialogueNode next,
                                     Runnable onSelect) {
        return new DialogueChoice(text, next, onSelect, null);
    }

    /** Выбор завершающий диалог */
    public static DialogueChoice end(String text) {
        return new DialogueChoice(text, null, null, null);
    }

    /** Выбор завершающий диалог с действием */
    public static DialogueChoice end(String text, Runnable onSelect) {
        return new DialogueChoice(text, null, onSelect, null);
    }

    /** Молчаливый выбор (текст "...") */
    public static DialogueChoice silent(DialogueNode next) {
        return new DialogueChoice("...", next, null, null);
    }

    /** Молчаливый выбор с действием */
    public static DialogueChoice silent(DialogueNode next, Runnable onSelect) {
        return new DialogueChoice("...", next, onSelect, null);
    }

    /**
     * Условный выбор — показывается только если condition возвращает true.
     *
     * Пример:
     *   DialogueChoice.conditional(
     *       "Я нашёл дневник в подвале",
     *       узелПроДневник,
     *       () -> Simvol.STORY.hasFlag(StoryFlag.ACT1_FOUND_DIARY)
     *   )
     */
    public static DialogueChoice conditional(String text, DialogueNode next,
                                              java.util.function.BooleanSupplier condition) {
        return new DialogueChoice(text, next, null, condition);
    }

    public static DialogueChoice conditional(String text, DialogueNode next,
                                              Runnable onSelect,
                                              java.util.function.BooleanSupplier condition) {
        return new DialogueChoice(text, next, onSelect, condition);
    }

    // ── Геттеры ───────────────────────────────────────────────

    public String       text()     { return text;     }
    public DialogueNode next()     { return next;     }
    public Runnable     onSelect() { return onSelect; }

    /**
     * Доступен ли этот выбор (условие выполнено).
     */
    public boolean isAvailable() {
        if (condition == null) return true;
        return condition.getAsBoolean();
    }
}
