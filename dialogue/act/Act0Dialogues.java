package com.simvol.mod.dialogue.act;

import com.simvol.mod.Simvol;
import com.simvol.mod.dialogue.DialogueChoice;
import com.simvol.mod.dialogue.DialogueNode;
import com.simvol.mod.diary.EvidenceRegistry;
import com.simvol.mod.story.StoryFlag;

import java.util.LinkedHashMap;
import java.util.Map;

public class Act0Dialogues {

    public static Map<String, DialogueNode> buildAll() {
        Map<String, DialogueNode> map = new LinkedHashMap<>();

        map.put("act0_detective_thoughts_1",     buildDetectiveThoughts1());
        map.put("act0_detective_reads_folder",    buildDetectiveReadsFolder());
        map.put("act0_detective_slams",           buildDetectiveSlams());
        map.put("act0_boss_fires",                buildBossFires());
        map.put("act0_badge_moment",              buildBadgeMoment());
        map.put("act0_notice_board",              buildNoticeBoard());
        map.put("act0_valeria_intro",             buildValeriaIntro());
        map.put("act0_laptop_search",             buildLaptopSearch());
        map.put("act0_laptop_no_results",         buildLaptopNoResults());
        map.put("act0_detective_rage",            buildDetectiveRage());
        map.put("act0_detective_parents_thought", buildParentsThought());
        map.put("act0_intercom_mama_who",         buildIntercomScene());
        map.put("act0_mama_greets",               buildMamaGreets());
        map.put("act0_parents_main",              buildParentsMain());
        map.put("act0_final_decision",            buildFinalDecision());

        return map;
    }

    // =========================================================
    //  ВНУТРЕННИЕ МОНОЛОГИ
    // =========================================================

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

    private static DialogueNode buildDetectiveReadsFolder() {
        return DialogueNode.detective(
            "Стеклянный Собиратель… Опять эта хуйня."
        )
        .voice("act0_detective_reads_folder")
        .autoAdvance(60)
        // Улика: папка с делом
        .onEnter(() -> {
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(EvidenceRegistry.CASE_FOLDER);
            }
        })
        .build();
    }

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
    //  НАЧАЛЬНИК
    // =========================================================

    private static DialogueNode buildBossFires() {

        DialogueNode end = DialogueNode.builder("Начальник",
            "Убирайся из моего агентства."
        )
        .voice("act0_boss_end")
        .autoAdvance(80)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT0_FIRED))
        .build();

        DialogueNode third = DialogueNode.builder("Начальник",
            "Твои срывы, паранойя и отказы " +
            "от нормальных дел достали всех. " +
            "Собирай вещи."
        )
        .voice("act0_boss_third")
        .autoAdvance(100)
        .next(end)
        .build();

        DialogueNode second = DialogueNode.builder("Начальник",
            "Ты уже полгода только мешаешь."
        )
        .voice("act0_boss_second")
        .autoAdvance(60)
        .next(third)
        .build();

        return DialogueNode.builder("Начальник",
            "Ты уволен."
        )
        .voice("act0_boss_fired")
        .autoAdvance(80)
        .next(second)
        .build();
    }

    // =========================================================
    //  ЗНАЧОК
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
    //  ОБЪЯВЛЕНИЕ
    // =========================================================

    private static DialogueNode buildNoticeBoard() {

        DialogueNode second = DialogueNode.detective(
            "«Требуется детектив. Ночной Дозор. " +
            "Большая зарплата.» " +
            "Звучит как дешёвая забегаловка."
        )
        .voice("act0_notice_2")
        .autoAdvance(100)
        // Улика: объявление Ночного Дозора
        .onEnter(() -> {
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(EvidenceRegistry.NIGHT_WATCH_AD);
            }
        })
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
    //  ВАЛЕРИЯ
    // =========================================================

    private static DialogueNode buildValeriaIntro() {

        DialogueNode finale = DialogueNode.builder("Валерия",
            "Хорошо. Тогда не будем терять времени. " +
            "Дело называется «Стекло»."
        )
        .voice("act0_valeria_finale")
        .autoAdvance(100)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT0_JOINED_NIGHT_WATCH))
        .build();

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

        DialogueNode answerSilent = DialogueNode.detective("...")
        .autoAdvance(40)
        .next(finale)
        .build();

        DialogueNode second = DialogueNode.builder("Валерия",
            "Нам всё равно кого брать. " +
            "Дело тяжёлое. Никто не хочет браться. " +
            "Вопросы есть?"
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

        return DialogueNode.builder("Валерия",
            "Значит ты и есть тот самый " +
            "«лучший детектив Аргуса», которого все выгнали?"
        )
        .voice("act0_valeria_first")
        .autoAdvance(100)
        .next(second)
        .build();
    }

    // =========================================================
    //  НОУТБУК
    // =========================================================

    private static DialogueNode buildLaptopSearch() {
        return DialogueNode.detective(
            "Стеклофабрика Красново..."
        )
        .voice("act0_laptop_search")
        .autoAdvance(80)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT0_SEARCHED_INTERNET))
        .build();
    }

    /**
     * Отдельный узел — результат «не найдено».
     * Добавляет улику поиска.
     */
    private static DialogueNode buildLaptopNoResults() {
        return DialogueNode.detective(
            "Результатов не найдено. " +
            "Ни одного упоминания. " +
            "Как будто этого места не существует."
        )
        .voice("act0_laptop_no_results")
        .autoAdvance(100)
        // Улика: поиск без результатов
        .onEnter(() -> {
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(EvidenceRegistry.SEARCH_NO_RESULTS);
            }
            Simvol.PARANOIA.add(1,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.READ_DIARY);
        })
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

        // ── Ветка: отец говорит ───────────────────────────────

        DialogueNode fatherSpeaks = DialogueNode.builder("Отец",
            "Не надо тебе туда ходить."
        )
        .voice("act0_father_warning")
        .autoAdvance(140)
        // Улика: предупреждение отца
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT0_LEARN_FATHER_WORKED);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(EvidenceRegistry.FATHER_WARNING);
            }
        })
        .build();

        // ── Ветка: мама шепчет (после ухода отца) ────────────

        DialogueNode mamaWhisper = DialogueNode.builder("Мама",
            "Лучше не спрашивай его об этом… " +
            "После того года он сильно изменился. " +
            "Стал… другим. " +
            "Пожалуйста, не лезь туда, сынок."
        )
        .voice("act0_mama_whisper")
        .autoAdvance(180)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT0_MOTHER_WARNED))
        .build();

        // ── Ветка: мама про завод ─────────────────────────────

        DialogueNode mamaAboutFactory = DialogueNode.builder("Мама",
            "Завод закрылся в 1979 году… " +
            "Мне тогда было 18. " +
            "Твой отец там работал."
        )
        .voice("act0_mama_factory")
        .autoAdvance(120)
        // Улика: слова мамы
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT0_LEARNED_FATHER_WORKED);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(EvidenceRegistry.MOTHER_TESTIMONY);
            }
        })
        .next(fatherSpeaks)
        .build();

        // ── Ветка: почему закрылся ────────────────────────────

        DialogueNode answerWhyClosed = DialogueNode.builder("Мама",
            "Не знаю, сынок. Никто особо не говорил. " +
            "Просто однажды закрыли — и всё."
        )
        .voice("act0_mama_why_closed")
        .autoAdvance(100)
        .next(mamaWhisper)
        .build();

        // ── Ветка: спросить отца напрямую ────────────────────

        DialogueNode detectiveAsksFather = DialogueNode.detective(
            "Пап, ты помнишь завод?"
        )
        .voice("act0_detective_ask_father")
        .autoAdvance(40)
        .onEnter(() ->
            Simvol.STORY.setFlag(StoryFlag.ACT0_ASKED_FATHER_DIRECTLY))
        .next(fatherSpeaks)
        .build();

        // ── Ветка: работа ─────────────────────────────────────

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
            // Всегда доступен
            DialogueChoice.of(
                "Расскажи про завод рядом с деревней.",
                mamaAboutFactory,
                () -> Simvol.STORY.setFlag(StoryFlag.ACT0_ASKED_ABOUT_FACTORY)
            ),

            // Доступен только после того как узнали про завод
            DialogueChoice.conditional(
                "Почему он закрылся?",
                answerWhyClosed,
                () -> Simvol.STORY.hasFlag(StoryFlag.ACT0_LEARNED_FATHER_WORKED)
            ),

            // Доступен только после того как узнали про завод
            DialogueChoice.conditional(
                "Спросить отца напрямую.",
                detectiveAsksFather,
                () -> Simvol.STORY.hasFlag(StoryFlag.ACT0_LEARNED_FATHER_WORKED)
            ),

            // Всегда доступен
            DialogueChoice.of(
                "Потерял работу.",
                answerJob
            )
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
        .onEnter(() ->
            Simvol.STORY.setFlag(StoryFlag.ACT0_DECIDED_TO_GO))
        .build();
    }
}
