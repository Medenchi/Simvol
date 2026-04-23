package com.simvol.mod.dialogue.act;

import com.simvol.mod.Simvol;
import com.simvol.mod.dialogue.DialogueChoice;
import com.simvol.mod.dialogue.DialogueNode;
import com.simvol.mod.story.StoryFlag;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * ВСЕ ДИАЛОГИ НУЛЕВОГО АКТА — «УВОЛЬНЕНИЕ»
 * ===========================================
 * Каждый диалог — дерево DialogueNode.
 * Регистрируются в DialogueEngine при старте.
 */
public class Act0Dialogues {

    /**
     * Возвращает все диалоги акта для регистрации.
     */
    public static Map<String, DialogueNode> buildAll() {
        Map<String, DialogueNode> map = new LinkedHashMap<>();

        map.put("act0_detective_thoughts_1",  buildDetectiveThoughts1());
        map.put("act0_detective_reads_folder", buildDetectiveReadsFolder());
        map.put("act0_detective_slams",        buildDetectiveSlams());
        map.put("act0_boss_fires",             buildBossFires());
        map.put("act0_badge_moment",           buildBadgeMoment());
        map.put("act0_notice_board",           buildNoticeBoard());
        map.put("act0_valeria_intro",          buildValeriaIntro());
        map.put("act0_laptop_search",          buildLaptopSearch());
        map.put("act0_detective_rage",         buildDetectiveRage());
        map.put("act0_detective_parents_thought", buildParentsThought());
        map.put("act0_intercom_mama_who",      buildIntercomScene());
        map.put("act0_mama_greets",            buildMamaGreets());
        map.put("act0_parents_main",           buildParentsMain());
        map.put("act0_final_decision",         buildFinalDecision());

        return map;
    }

    // =========================================================
    //  ВНУТРЕННИЕ МОНОЛОГИ ДЕТЕКТИВА
    // =========================================================

    /**
     * Детектив идёт по офису — внутренние мысли.
     * Без выборов, авто-прокрутка.
     */
    private static DialogueNode buildDetectiveThoughts1() {
        return DialogueNode.detective(
            "Третья ночь без сна. Седьмое дело за месяц. " +
            "И каждый раз одно и то же — «Собиратель». " +
            "Этого не существует."
        )
        .voice("act0_detective_thoughts_1")
        .autoAdvance(120)
        .build();
    }

    /**
     * Детектив читает название папки вслух.
     */
    private static DialogueNode buildDetectiveReadsFolder() {
        return DialogueNode.detective(
            "Стеклянный Собиратель… Опять эта хуйня."
        )
        .voice("act0_detective_reads_folder")
        .autoAdvance(60)
        .build();
    }

    /**
     * Детектив бросает папку начальнику.
     */
    private static DialogueNode buildDetectiveSlams() {
        return DialogueNode.detective(
            "Я не буду это вести. " +
            "Это не дело. Это бред сумасшедшего."
        )
        .voice("act0_detective_slams")
        .autoAdvance(80)
        .build();
    }

    // =========================================================
    //  НАЧАЛЬНИК — УВОЛЬНЕНИЕ
    // =========================================================

    private static DialogueNode buildBossFires() {

        // Финальная реплика начальника — конец диалога
        DialogueNode end = DialogueNode.builder("Начальник",
            "Убирайся из моего агентства."
        )
        .voice("act0_boss_end")
        .autoAdvance(80)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT0_FIRED))
        .build();

        // Третья реплика
        DialogueNode third = DialogueNode.builder("Начальник",
            "Твои срывы, паранойя и отказы от нормальных дел " +
            "достали всех. Собирай вещи."
        )
        .voice("act0_boss_third")
        .autoAdvance(100)
        .next(end)
        .build();

        // Вторая реплика — встаёт
        DialogueNode second = DialogueNode.builder("Начальник",
            "Ты уже полгода только мешаешь."
        )
        .voice("act0_boss_second")
        .autoAdvance(60)
        .next(third)
        .build();

        // Первая реплика — главная
        return DialogueNode.builder("Начальник",
            "Ты уволен."
        )
        .voice("act0_boss_fired")
        .autoAdvance(80)
        .next(second)
        .build();
    }

    // =========================================================
    //  СБОРЫ — ЗНАЧОК
    // =========================================================

    private static DialogueNode buildBadgeMoment() {
        return DialogueNode.detective(
            "Детектив третьего класса. " +
            "Шесть лет. И вот так."
        )
        .voice("act0_badge_moment")
        .autoAdvance(80)
        .build();
    }

    // =========================================================
    //  ОБЪЯВЛЕНИЕ НА СТЕНЕ
    // =========================================================

    private static DialogueNode buildNoticeBoard() {

        DialogueNode second = DialogueNode.detective(
            "«Требуется детектив. Ночной Дозор. " +
            "Большая зарплата.» " +
            "Звучит как дешёвая забегаловка."
        )
        .voice("act0_notice_2")
        .autoAdvance(100)
        .build();

        return DialogueNode.detective(
            "Стена объявлений. Старая. " +
            "Дождь размыл половину."
        )
        .voice("act0_notice_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    // =========================================================
    //  ВАЛЕРИЯ — НОЧНОЙ ДОЗОР
    // =========================================================

    private static DialogueNode buildValeriaIntro() {

        // Финал — независимо от выбора
        DialogueNode finale = DialogueNode.builder("Валерия",
            "Хорошо. Тогда не будем терять времени. " +
            "Дело называется «Стекло»."
        )
        .voice("act0_valeria_finale")
        .autoAdvance(100)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT0_JOINED_NIGHT_WATCH))
        .build();

        // Ответы на каждый выбор
        DialogueNode answerDeal = DialogueNode.builder("Валерия",
            "Старый завод. Закрытый. " +
            "Там что-то происходит. Нужно выяснить что."
        )
        .voice("act0_valeria_answer_deal")
        .autoAdvance(80)
        .next(finale)
        .build();

        DialogueNode answerWhy = DialogueNode.builder("Валерия",
            "Потому что два детектива уже отказались. " +
            "Третий просто не вернулся."
        )
        .voice("act0_valeria_answer_why")
        .autoAdvance(100)
        .next(finale)
        .build();

        DialogueNode answerMoney = DialogueNode.builder("Валерия",
            "Больше чем в Аргусе. " +
            "Если справишься."
        )
        .voice("act0_valeria_answer_money")
        .autoAdvance(70)
        .next(finale)
        .build();

        DialogueNode answerSilent = DialogueNode.detective(
            "..."
        )
        .autoAdvance(40)
        .next(finale)
        .build();

        // Главная реплика с выборами
        DialogueNode second = DialogueNode.builder("Валерия",
            "Нам всё равно кого брать. " +
            "Дело тяжёлое. Никто не хочет браться. " +
            "Справляешься — остаёшься. Вопросы есть?"
        )
        .voice("act0_valeria_second")
        .choices(
            DialogueChoice.of("Что за дело?",
                answerDeal,
                () -> Simvol.STORY.setFlag(StoryFlag.ACT0_ASKED_ABOUT_FACTORY)),
            DialogueChoice.of("Почему никто не берётся?",
                answerWhy),
            DialogueChoice.of("Какая зарплата?",
                answerMoney),
            DialogueChoice.silent(answerSilent)
        )
        .build();

        // Первая реплика
        return DialogueNode.builder("Валерия",
            "Значит ты и есть тот самый «лучший детектив Аргуса», " +
            "которого все выгнали?"
        )
        .voice("act0_valeria_first")
        .autoAdvance(100)
        .next(second)
        .build();
    }

    // =========================================================
    //  НОУТБУК — ПОИСК
    // =========================================================

    private static DialogueNode buildLaptopSearch() {
        return DialogueNode.detective(
            "Стеклофабрика красново..."
        )
        .voice("act0_laptop_search")
        .autoAdvance(80)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT0_SEARCHED_INTERNET))
        .build();
    }

    private static DialogueNode buildDetectiveRage() {
        return DialogueNode.detective(
            "Да что вообще происходит?! " +
            "Это же не может быть совпадением!"
        )
        .voice("act0_detective_rage")
        .autoAdvance(80)
        .build();
    }

    private static DialogueNode buildParentsThought() {
        return DialogueNode.detective(
            "Пойду к родителям… " +
            "Может, они хоть что-то знают."
        )
        .voice("act0_parents_thought")
        .autoAdvance(80)
        .build();
    }

    // =========================================================
    //  ДОМОФОН
    // =========================================================

    private static DialogueNode buildIntercomScene() {

        DialogueNode detectiveAnswer = DialogueNode.detective(
            "Это я, мам."
        )
        .voice("act0_intercom_detective")
        .autoAdvance(40)
        .build();

        return DialogueNode.builder("Мама",
            "Кто?"
        )
        .voice("act0_intercom_mama_who")
        .autoAdvance(50)
        .next(detectiveAnswer)
        .build();
    }

    // =========================================================
    //  РОДИТЕЛИ — КУХНЯ
    // =========================================================

    private static DialogueNode buildMamaGreets() {

        DialogueNode second = DialogueNode.builder("Мама",
            "Совсем похудел... Садись, я чай поставлю."
        )
        .voice("act0_mama_greets_2")
        .autoAdvance(80)
        .build();

        return DialogueNode.builder("Мама",
            "Сыночек! Вот неожиданность!"
        )
        .voice("act0_mama_greets_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    private static DialogueNode buildParentsMain() {

        // ── Ветка про завод ───────────────────────────────────

        DialogueNode fatherSpeaks = DialogueNode.builder("Отец",
            "Не надо тебе туда ходить."
        )
        .voice("act0_father_warning")
        .autoAdvance(120)
        .onEnter(() -> {
            // Только если спросили про завод
            if (Simvol.STORY.hasFlag(StoryFlag.ACT0_ASKED_FATHER_DIRECTLY)) {
                Simvol.STORY.setFlag(StoryFlag.ACT0_LEARN_FATHER_WORKED);
            }
        })
        .build();

        DialogueNode mamaWhisper = DialogueNode.builder("Мама",
            "Лучше не спрашивай его об этом… " +
            "После того года он сильно изменился. " +
            "Стал… другим. Пожалуйста, не лезь туда, сынок."
        )
        .voice("act0_mama_whisper")
        .autoAdvance(160)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT0_MOTHER_WARNED))
        .build();

        DialogueNode mamaAboutFactory = DialogueNode.builder("Мама",
            "Завод закрылся в 1979 году… " +
            "Мне тогда было 18. Твой отец там работал."
        )
        .voice("act0_mama_factory")
        .autoAdvance(120)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT0_LEARNED_FATHER_WORKED))
        .next(fatherSpeaks)
        .build();

        // ── Ответы на выборы ──────────────────────────────────

        DialogueNode answerFactory = mamaAboutFactory;

        DialogueNode answerWhyClosed = DialogueNode.builder("Мама",
            "Не знаю, сынок. Никто особо не говорил. " +
            "Просто однажды закрыли — и всё."
        )
        .voice("act0_mama_why_closed")
        .autoAdvance(100)
        .next(mamaWhisper)
        .build();

        DialogueNode answerFather = DialogueNode.detective(
            "Пап, ты помнишь завод?"
        )
        .voice("act0_detective_ask_father")
        .autoAdvance(40)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT0_ASKED_FATHER_DIRECTLY))
        .next(fatherSpeaks)
        .build();

        DialogueNode answerJob = DialogueNode.builder("Мама",
            "Ничего, найдёшь новое место. " +
            "Ты умный, справишься."
        )
        .voice("act0_mama_job")
        .autoAdvance(80)
        .build();

        // ── Главный узел с выборами ───────────────────────────

        return DialogueNode.builder("Мама",
            "Ну рассказывай, как дела? " +
            "Давно не заходил."
        )
        .voice("act0_mama_main")
        .choices(
            DialogueChoice.of("Расскажи про завод рядом с деревней.",
                answerFactory,
                () -> Simvol.STORY.setFlag(StoryFlag.ACT0_ASKED_ABOUT_FACTORY)),

            DialogueChoice.conditional(
                "Почему он закрылся?",
                answerWhyClosed,
                () -> Simvol.STORY.hasFlag(StoryFlag.ACT0_LEARNED_FATHER_WORKED)
            ),

            DialogueChoice.conditional(
                "Спросить отца напрямую.",
                answerFather,
                () -> Simvol.STORY.hasFlag(StoryFlag.ACT0_LEARNED_FATHER_WORKED)
            ),

            DialogueChoice.of("Потерял работу.",
                answerJob)
        )
        .build();
    }

    // =========================================================
    //  ФИНАЛЬНОЕ РЕШЕНИЕ
    // =========================================================

    private static DialogueNode buildFinalDecision() {
        return DialogueNode.detective(
            "Я еду на этот завод завтра."
        )
        .voice("act0_final_decision")
        .autoAdvance(80)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT0_DECIDED_TO_GO))
        .build();
    }
}
