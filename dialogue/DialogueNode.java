package com.simvol.mod.dialogue;

import java.util.List;

/**
 * Один узел диалогового дерева — одна реплика персонажа.
 *
 * ПОЛЯ:
 * speakerName      — имя говорящего (отображается золотым в субтитрах)
 * text             — текст реплики
 * voiceLine        — ID звукового файла озвучки (или null)
 * choices          — варианты ответа игрока (пусто = авто-переход)
 * next             — следующий узел (если нет choices)
 * autoAdvanceTicks — тиков до авто-перехода (если нет choices)
 * onEnter          — действие при входе в этот узел
 *
 * ПРИМЕР СОЗДАНИЯ:
 *
 *   // Простая реплика без выборов (авто-переход через 3 сек)
 *   DialogueNode node = DialogueNode.builder("Валерия",
 *           "Значит ты и есть тот самый детектив Аргуса?")
 *       .voice("act0_valeria_intro_1")
 *       .autoAdvance(60)
 *       .next(следующийУзел)
 *       .build();
 *
 *   // Реплика с выборами
 *   DialogueNode node = DialogueNode.builder("Валерия",
 *           "Нам всё равно кого брать. Дело тяжёлое.")
 *       .voice("act0_valeria_intro_2")
 *       .choices(
 *           DialogueChoice.of("Что за дело?",           узелПроДело),
 *           DialogueChoice.of("Почему никто не берёт?", узелПроОтказ),
 *           DialogueChoice.of("Какая зарплата?",        узелПроДеньги),
 *           DialogueChoice.silent(                       узелМолчание)
 *       )
 *       .build();
 */
public final class DialogueNode {

    private final String              speakerName;
    private final String              text;
    private final String              voiceLine;
    private final List<DialogueChoice> choices;
    private final DialogueNode        next;
    private final int                 autoAdvanceTicks;
    private final Runnable            onEnter;

    private DialogueNode(Builder b) {
        this.speakerName      = b.speakerName;
        this.text             = b.text;
        this.voiceLine        = b.voiceLine;
        this.choices          = b.choices != null ? List.copyOf(b.choices) : List.of();
        this.next             = b.next;
        this.autoAdvanceTicks = b.autoAdvanceTicks;
        this.onEnter          = b.onEnter;
    }

    // ── Геттеры ───────────────────────────────────────────────

    public String              speakerName()      { return speakerName;      }
    public String              text()             { return text;             }
    public String              voiceLine()        { return voiceLine;        }
    public List<DialogueChoice> choices()         { return choices;          }
    public DialogueNode        next()             { return next;             }
    public int                 autoAdvanceTicks() { return autoAdvanceTicks; }
    public Runnable            onEnter()          { return onEnter;          }

    // ── Билдер ────────────────────────────────────────────────

    public static Builder builder(String speakerName, String text) {
        return new Builder(speakerName, text);
    }

    /** Монолог без имени говорящего */
    public static Builder monologue(String text) {
        return new Builder("", text);
    }

    /** Внутренний монолог детектива (курсив в субтитрах) */
    public static Builder detective(String text) {
        return new Builder("Детектив", text);
    }

    public static final class Builder {

        private final String speakerName;
        private final String text;
        private String              voiceLine        = null;
        private List<DialogueChoice> choices         = null;
        private DialogueNode        next             = null;
        private int                 autoAdvanceTicks = 80;
        private Runnable            onEnter          = null;

        public Builder(String speakerName, String text) {
            this.speakerName = speakerName;
            this.text        = text;
        }

        /** ID файла озвучки */
        public Builder voice(String voiceLine) {
            this.voiceLine = voiceLine;
            return this;
        }

        /** Варианты выбора (если есть — авто-переход отключается) */
        public Builder choices(DialogueChoice... choices) {
            this.choices = List.of(choices);
            return this;
        }

        /** Следующий узел (если нет выборов) */
        public Builder next(DialogueNode next) {
            this.next = next;
            return this;
        }

        /**
         * Тиков до авто-перехода к следующему узлу.
         * 20 тиков = 1 секунда.
         * По умолчанию 80 тиков (4 секунды).
         */
        public Builder autoAdvance(int ticks) {
            this.autoAdvanceTicks = ticks;
            return this;
        }

        /** Действие при входе в этот узел (установка флага и т.д.) */
        public Builder onEnter(Runnable action) {
            this.onEnter = action;
            return this;
        }

        public DialogueNode build() {
            return new DialogueNode(this);
        }
    }
}
