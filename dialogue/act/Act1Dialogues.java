package com.simvol.mod.dialogue.act;

import com.simvol.mod.Simvol;
import com.simvol.mod.dialogue.DialogueChoice;
import com.simvol.mod.dialogue.DialogueNode;
import com.simvol.mod.story.StoryFlag;
import com.simvol.mod.SimvolClient;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ВСЕ ДИАЛОГИ АКТА 1 — «СТЕКЛО»
 * ================================
 * Все монологи детектива при осмотре завода.
 * Все реплики при находке улик.
 * Диалог с Валерией по телефону.
 * Финальный монолог в комнате со стеклянными гробами.
 */
public class Act1Dialogues {

    public static Map<String, DialogueNode> buildAll() {
        Map<String, DialogueNode> map = new LinkedHashMap<>();

        // ── Прибытие ──────────────────────────────────────────
        map.put("act1_arrive_thoughts",      buildArriveThoughts());
        map.put("act1_new_lock",             buildNewLock());
        map.put("act1_gates_close",          buildGatesClose());

        // ── Двор ──────────────────────────────────────────────
        map.put("act1_barrels",              buildBarrels());
        map.put("act1_forklift",             buildForklift());
        map.put("act1_bulletin_board",       buildBulletinBoard());

        // ── Проходная ─────────────────────────────────────────
        map.put("act1_journal",              buildJournal());
        map.put("act1_guard_photo",          buildGuardPhoto());

        // ── Цех 1 ─────────────────────────────────────────────
        map.put("act1_poster",               buildPoster());
        map.put("act1_lever",                buildLever());
        map.put("act1_glass_form",           buildGlassForm());
        map.put("act1_fresh_tracks",         buildFreshTracks());

        // ── Склад ─────────────────────────────────────────────
        map.put("act1_documents",            buildDocuments());
        map.put("act1_new_torch",            buildNewTorch());

        // ── Коридор ───────────────────────────────────────────
        map.put("act1_dont_go",              buildDontGo());
        map.put("act1_basement_refuse",      buildBasementRefuse());

        // ── Офис директора ────────────────────────────────────
        map.put("act1_director_portrait",    buildDirectorPortrait());
        map.put("act1_safe",                 buildSafe());
        map.put("act1_notebook",             buildNotebook());

        // ── Цех 2 ─────────────────────────────────────────────
        map.put("act1_scheme",               buildScheme());
        map.put("act1_robe",                 buildRobe());

        // ── Подвал ────────────────────────────────────────────
        map.put("act1_mattresses",           buildMattresses());
        map.put("act1_cans",                 buildCans());
        map.put("act1_diary_read",           buildDiaryRead());

        // ── Финальная комната ─────────────────────────────────
        map.put("act1_final_door",           buildFinalDoor());
        map.put("act1_door_choice",          buildDoorChoice());
        map.put("act1_call_valeria",         buildCallValeria());
        map.put("act1_glass_tombs",          buildGlassTombs());
        map.put("act1_inscription",          buildInscription());

        return map;
    }

    // =========================================================
    //  ПРИБЫТИЕ
    // =========================================================

    private static DialogueNode buildArriveThoughts() {

        DialogueNode third = DialogueNode.detective(
            "Замок новый. " +
            "На воротах сорокапятилетней давности."
        )
        .voice("act1_arrive_3")
        .autoAdvance(80)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_FRESH_LOCK))
        .build();

        DialogueNode second = DialogueNode.detective(
            "Значит вот ты какой… " +
            "Закрыт с 1979 года. Сорок пять лет никого."
        )
        .voice("act1_arrive_2")
        .autoAdvance(100)
        .next(third)
        .build();

        return DialogueNode.detective(
            "Стеклозавод номер семь."
        )
        .voice("act1_arrive_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    private static DialogueNode buildNewLock() {
        return DialogueNode.detective(
            "Замок новый. Совсем. " +
            "Кто-то сюда приходит. Регулярно."
        )
        .voice("act1_new_lock")
        .autoAdvance(80)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_FRESH_LOCK))
        .build();
    }

    private static DialogueNode buildGatesClose() {
        return DialogueNode.detective(
            "Отлично."
        )
        .voice("act1_gates_close")
        .autoAdvance(40)
        .build();
    }

    // =========================================================
    //  ДВОР
    // =========================================================

    private static DialogueNode buildBarrels() {

        DialogueNode second = DialogueNode.detective(
            "Химия какая-то. " +
            "На стекольном заводе не должно быть такого запаха."
        )
        .voice("act1_barrels_2")
        .autoAdvance(80)
        .build();

        return DialogueNode.detective(
            "Пусто. Давно пусто…"
        )
        .voice("act1_barrels_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    private static DialogueNode buildForklift() {
        return DialogueNode.detective(
            "Советский ещё… На таких мой дед работал. " +
            "Ключ в замке зажигания. " +
            "Сорок пять лет, а ключ всё ещё здесь."
        )
        .voice("act1_forklift")
        .autoAdvance(120)
        .build();
    }

    private static DialogueNode buildBulletinBoard() {

        DialogueNode second = DialogueNode.detective(
            "«Внеплановое собрание. " +
            "15 марта 1979. Всем рабочим явка обязательна.» " +
            "День закрытия завода."
        )
        .voice("act1_bulletin_2")
        .autoAdvance(120)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_MEETING_NOTICE);
            // Добавляем улику в дневник
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.MEETING_NOTICE
                );
            }
        })
        .build();

        return DialogueNode.detective(
            "Объявления… ничего не разобрать. " +
            "Стоп. Вот это видно."
        )
        .voice("act1_bulletin_1")
        .autoAdvance(80)
        .next(second)
        .build();
    }

    // =========================================================
    //  ПРОХОДНАЯ
    // =========================================================

    private static DialogueNode buildJournal() {

        DialogueNode fourth = DialogueNode.detective(
            "Трое не расписались на выход."
        )
        .voice("act1_journal_4")
        .autoAdvance(80)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_MISSING_3);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.THREE_MISSING
                );
            }
        })
        .build();

        DialogueNode third = DialogueNode.detective(
            "15 марта. " +
            "Приход — восемнадцать человек. " +
            "Уход — пятнадцать."
        )
        .voice("act1_journal_3")
        .autoAdvance(100)
        .next(fourth)
        .build();

        DialogueNode second = DialogueNode.detective(
            "Каждый день. Каждый рабочий. " +
            "Последняя запись — 14 марта 1979."
        )
        .voice("act1_journal_2")
        .autoAdvance(100)
        .next(third)
        .build();

        return DialogueNode.detective(
            "Журнал посещений…"
        )
        .voice("act1_journal_1")
        .autoAdvance(60)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_JOURNAL))
        .next(second)
        .build();
    }

    private static DialogueNode buildGuardPhoto() {

        DialogueNode second = DialogueNode.detective(
            "Интересно, он вышел 15 марта?"
        )
        .voice("act1_guard_2")
        .autoAdvance(60)
        .onEnter(() -> {
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.GUARD_SEMYONYCH
                );
            }
        })
        .build();

        return DialogueNode.detective(
            "Охранник наверное… " +
            "Пожилой мужик. " +
            "На обороте написано: «Семёныч. 31 год службы.»"
        )
        .voice("act1_guard_1")
        .autoAdvance(120)
        .next(second)
        .build();
    }

    // =========================================================
    //  ЦЕХ 1
    // =========================================================

    private static DialogueNode buildPoster() {
        return DialogueNode.detective(
            "Плакат 1976 года. «Стеклозавод №7». " +
            "Значит были и другие заводы…"
        )
        .voice("act1_poster")
        .autoAdvance(100)
        .build();
    }

    private static DialogueNode buildLever() {
        return DialogueNode.detective(
            "Этот рычаг кто-то недавно трогал. " +
            "Пыль стёрта. И следы свежие."
        )
        .voice("act1_lever")
        .autoAdvance(80)
        .build();
    }

    private static DialogueNode buildGlassForm() {

        DialogueNode third = DialogueNode.detective(
            "Это форма для человека."
        )
        .voice("act1_glass_form_3")
        .autoAdvance(80)
        .onEnter(() -> {
            SimvolClient.PARANOIA_FX.tick(
                net.minecraft.client.MinecraftClient.getInstance()
            );
            Simvol.PARANOIA.add(1,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.READ_DIARY);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.GLASS_FORM
                );
            }
        })
        .build();

        DialogueNode second = DialogueNode.detective(
            "Форма для литья… " +
            "Метр восемьдесят в длину. " +
            "Полметра в ширину."
        )
        .voice("act1_glass_form_2")
        .autoAdvance(100)
        .next(third)
        .build();

        return DialogueNode.detective(
            "Что это…"
        )
        .voice("act1_glass_form_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    private static DialogueNode buildFreshTracks() {
        return DialogueNode.detective(
            "Следы совсем свежие. " +
            "Идут через весь цех и уходят в коридор. " +
            "Кто-то здесь регулярно бывает."
        )
        .voice("act1_tracks")
        .autoAdvance(100)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_FRESH_TRACKS))
        .build();
    }

    // =========================================================
    //  СКЛАД
    // =========================================================

    private static DialogueNode buildDocuments() {

        DialogueNode second = DialogueNode.detective(
            "На стекольном заводе не должно быть лаборатории."
        )
        .voice("act1_docs_2")
        .autoAdvance(80)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_COMPOSITION_K7);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.COMPOSITION_K7
                );
            }
        })
        .build();

        return DialogueNode.detective(
            "Накладная на «Специальный состав К-7». " +
            "Получатель — Лаборатория."
        )
        .voice("act1_docs_1")
        .autoAdvance(100)
        .next(second)
        .build();
    }

    private static DialogueNode buildNewTorch() {
        return DialogueNode.detective(
            "Новый. Совсем новый. Батарейки свежие. " +
            "Кто оставил здесь новый фонарик " +
            "на заводе, закрытом сорок пять лет?"
        )
        .voice("act1_torch")
        .autoAdvance(120)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_SPARE_TORCH))
        .build();
    }

    // =========================================================
    //  КОРИДОР — ПОДВАЛ
    // =========================================================

    private static DialogueNode buildDontGo() {

        DialogueNode second = DialogueNode.detective(
            "Кто-то очень не хотел, " +
            "чтобы люди шли в подвал."
        )
        .voice("act1_dont_go_2")
        .autoAdvance(80)
        .build();

        return DialogueNode.detective(
            "«Не надо»…"
        )
        .voice("act1_dont_go_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    /**
     * Если игрок ОТКАЗАЛСЯ идти в подвал.
     * Детектив сам себя убеждает что надо идти.
     */
    private static DialogueNode buildBasementRefuse() {

        DialogueNode third = DialogueNode.detective(
            "Всё равно придётся пойти."
        )
        .voice("act1_refuse_3")
        .autoAdvance(60)
        .onEnter(() -> {
            // Принудительно ведём игрока в подвал
            Simvol.STORY.setFlag(StoryFlag.ACT1_WENT_TO_BASEMENT);
        })
        .build();

        DialogueNode second = DialogueNode.detective(
            "Три человека не вышли с завода. " +
            "Кто-то регулярно сюда приходит. " +
            "Новый замок. Свежие следы."
        )
        .voice("act1_refuse_2")
        .autoAdvance(120)
        .next(third)
        .build();

        return DialogueNode.detective(
            "Даже не знаю…"
        )
        .voice("act1_refuse_1")
        .autoAdvance(60)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT1_REFUSED_BASEMENT))
        .next(second)
        .build();
    }

    // =========================================================
    //  ОФИС ДИРЕКТОРА
    // =========================================================

    private static DialogueNode buildDirectorPortrait() {
        return DialogueNode.detective(
            "Громов Виктор Степанович. " +
            "Директор с 1961 по 1979 год."
        )
        .voice("act1_portrait")
        .autoAdvance(80)
        .onEnter(() -> {
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.DIRECTOR_GROMOV
                );
            }
        })
        .build();
    }

    private static DialogueNode buildSafe() {

        DialogueNode second = DialogueNode.detective(
            "Код не сброшен — 1-9-7-9. " +
            "Год закрытия."
        )
        .voice("act1_safe_2")
        .autoAdvance(80)
        .build();

        return DialogueNode.detective(
            "Сейф открыт. Пустой."
        )
        .voice("act1_safe_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    private static DialogueNode buildNotebook() {

        DialogueNode third = DialogueNode.detective(
            "15 марта — страница полностью пустая."
        )
        .voice("act1_notebook_3")
        .autoAdvance(80)
        .onEnter(() -> {
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.DIRECTOR_NOTEBOOK
                );
            }
        })
        .build();

        DialogueNode second = DialogueNode.detective(
            "14 марта 1979 — " +
            "«Получено разрешение. " +
            "Процедура начнётся завтра.»"
        )
        .voice("act1_notebook_2")
        .autoAdvance(100)
        .next(third)
        .build();

        return DialogueNode.detective(
            "Записная книжка директора…"
        )
        .voice("act1_notebook_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    // =========================================================
    //  ЦЕХ 2
    // =========================================================

    private static DialogueNode buildScheme() {
        return DialogueNode.detective(
            "Здесь снова упоминается состав К-7. " +
            "Это уже не стандартное производство стекла."
        )
        .voice("act1_scheme")
        .autoAdvance(100)
        .build();
    }

    private static DialogueNode buildRobe() {

        DialogueNode second = DialogueNode.detective(
            "«Если найдёшь — иди в подвал. Мы там.»"
        )
        .voice("act1_robe_2")
        .autoAdvance(80)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_ROBE_ALIEVA);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.ALIEVA_ROBE
                );
            }
        })
        .build();

        return DialogueNode.detective(
            "Роба рабочего. " +
            "Алиев. " +
            "На спине написано карандашом:"
        )
        .voice("act1_robe_1")
        .autoAdvance(80)
        .next(second)
        .build();
    }

    // =========================================================
    //  ПОДВАЛ
    // =========================================================

    private static DialogueNode buildMattresses() {

        DialogueNode second = DialogueNode.detective(
            "На стенах царапины — считали дни."
        )
        .voice("act1_mattresses_2")
        .autoAdvance(80)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_MATTRESSES))
        .build();

        return DialogueNode.detective(
            "Три матраса… " +
            "Кто-то жил здесь долго."
        )
        .voice("act1_mattresses_1")
        .autoAdvance(80)
        .next(second)
        .build();
    }

    private static DialogueNode buildCans() {
        return DialogueNode.detective(
            "Советские консервы марта 1979 года. " +
            "Двадцать три банки. " +
            "Трое людей провели здесь около двух недель."
        )
        .voice("act1_cans")
        .autoAdvance(120)
        .build();
    }

    /**
     * Детектив читает дневник вслух.
     * Самая длинная и эмоциональная сцена акта.
     */
    private static DialogueNode buildDiaryRead() {

        // Финал — детектив закрывает дневник
        DialogueNode close = DialogueNode.detective(
            "Не дописал… " +
            "Они видели какую-то комнату. " +
            "И эта комната… она здесь. В подвале."
        )
        .voice("act1_diary_close")
        .autoAdvance(140)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_DIARY);
            Simvol.PARANOIA.add(2,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.READ_DIARY);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.BASEMENT_DIARY
                );
            }
        })
        .build();

        // Страница 5
        DialogueNode page5 = DialogueNode.builder("Дневник",
            "25 марта. " +
            "Сергеев ушёл ночью. " +
            "Алиев ушёл позже. Я один. " +
            "Если кто-то найдёт это…"
        )
        .voice("act1_diary_5")
        .autoAdvance(160)
        .next(close)
        .build();

        // Страница 4
        DialogueNode page4 = DialogueNode.builder("Дневник",
            "21 марта. " +
            "Сергеев говорит надо бежать. " +
            "Алиев молчит уже два дня."
        )
        .voice("act1_diary_4")
        .autoAdvance(120)
        .next(page5)
        .build();

        // Страница 3
        DialogueNode page3 = DialogueNode.builder("Дневник",
            "17 марта. " +
            "Нас трое. Я, Алиев и Сергеев. " +
            "Прячемся в подвале. " +
            "Ночью слышим шаги наверху. " +
            "Ищут нас."
        )
        .voice("act1_diary_3")
        .autoAdvance(160)
        .next(page4)
        .build();

        // Страница 2
        DialogueNode page2 = DialogueNode.builder("Дневник",
            "15 марта 1979. " +
            "Завод закрывают. Нам сказали уходить. " +
            "Но мы не можем. " +
            "Мы видели что здесь делали. Видели комнату. " +
            "Они не могут нас выпустить."
        )
        .voice("act1_diary_2")
        .autoAdvance(200)
        .next(page3)
        .build();

        // Начало — детектив открывает дневник
        return DialogueNode.detective(
            "Что-то под матрасом…"
        )
        .voice("act1_diary_1")
        .autoAdvance(60)
        .next(page2)
        .build();
    }

    // =========================================================
    //  ФИНАЛЬНАЯ КОМНАТА
    // =========================================================

    private static DialogueNode buildFinalDoor() {

        DialogueNode second = DialogueNode.detective(
            "Снова новый замок… " +
            "Кто-то не хочет, чтобы эту дверь открыли. " +
            "Или хочет, чтобы то, что внутри, не вышло наружу."
        )
        .voice("act1_final_door_2")
        .autoAdvance(140)
        .build();

        return DialogueNode.detective(
            "Тяжёлая дверь. Металл."
        )
        .voice("act1_final_door_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    /**
     * Выбор способа открытия двери.
     * Влияет на флаги и следующие диалоги.
     */
    private static DialogueNode buildDoorChoice() {

        // Ветка — взломать
        DialogueNode branchBreak = DialogueNode.detective(
            "Шумно. Но быстро."
        )
        .voice("act1_break_door")
        .autoAdvance(60)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT1_BROKE_DOOR);
        })
        .build();

        // Ветка — найти ключ
        DialogueNode branchKey = DialogueNode.detective(
            "Под третьим матрасом. " +
            "Конечно."
        )
        .voice("act1_found_key")
        .autoAdvance(80)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT1_FOUND_KEY))
        .build();

        // Ветка — позвонить Валерии
        DialogueNode branchCall = DialogueNode.detective(
            "Звоню Валерии."
        )
        .voice("act1_call_start")
        .autoAdvance(40)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT1_CALLED_VALERIA))
        .build();

        return DialogueNode.detective(
            "Как открыть?"
        )
        .choices(
            DialogueChoice.of(
                "Взломать. Шумно, но быстро.",
                branchBreak
            ),
            DialogueChoice.conditional(
                "Поискать ключ под матрасами.",
                branchKey,
                () -> Simvol.STORY.hasFlag(StoryFlag.ACT1_FOUND_MATTRESSES)
            ),
            DialogueChoice.of(
                "Позвонить Валерии.",
                branchCall
            )
        )
        .build();
    }

    private static DialogueNode buildCallValeria() {

        // Валерия даёт подсказку про Громова
        DialogueNode valeriaInfo = DialogueNode.builder("Валерия",
            "Громов. Виктор Степанович. " +
            "Директор. Живёт в деревне за забором. " +
            "Ключи от всех дверей хранил сам."
        )
        .voice("act1_valeria_call_info")
        .autoAdvance(140)
        .build();

        DialogueNode detectiveAsks = DialogueNode.detective(
            "Нашёл финальную комнату в подвале. " +
            "Замок новый. Не могу войти."
        )
        .voice("act1_detective_call")
        .autoAdvance(100)
        .next(valeriaInfo)
        .build();

        DialogueNode valeriaAnswers = DialogueNode.builder("Валерия",
            "Слушаю."
        )
        .voice("act1_valeria_answers")
        .autoAdvance(40)
        .next(detectiveAsks)
        .build();

        return DialogueNode.builder("Валерия",
            "Да?"
        )
        .voice("act1_valeria_picks_up")
        .autoAdvance(30)
        .next(valeriaAnswers)
        .build();
    }

    /**
     * Детектив видит тела в стекле.
     * Самый важный момент акта.
     */
    private static DialogueNode buildGlassTombs() {

        // Финальный вопрос к телам
        DialogueNode question = DialogueNode.detective(
            "Вас убили потому что вы что-то видели. " +
            "Что вы видели?"
        )
        .voice("act1_tombs_question")
        .autoAdvance(120)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT1_SAW_GLASS_TOMBS);
            Simvol.PARANOIA.add(3,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.FOUND_SYMBOL);
            // Белая вспышка при виде тел
            if (SimvolClient.HUD != null) {
                SimvolClient.HUD.flashWhite(8);
            }
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.GLASS_TOMBS
                );
            }
        })
        .build();

        // Осознание
        DialogueNode realize = DialogueNode.detective(
            "Трое. Как трое рабочих из журнала. " +
            "Состав К-7… Вот для чего он был."
        )
        .voice("act1_tombs_realize")
        .autoAdvance(140)
        .next(question)
        .build();

        // Первый шок
        DialogueNode shock = DialogueNode.detective(
            "Это люди… " +
            "Они залиты стеклом."
        )
        .voice("act1_tombs_shock")
        .autoAdvance(100)
        .next(realize)
        .build();

        // Отшатывается
        return DialogueNode.detective(
            "Что…"
        )
        .voice("act1_tombs_first")
        .autoAdvance(60)
        .next(shock)
        .build();
    }

    private static DialogueNode buildInscription() {

        DialogueNode turns = DialogueNode.detective(
            "Деревня. " +
            "Прямо за забором."
        )
        .voice("act1_inscription_turns")
        .autoAdvance(100)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT1_READ_INSCRIPTION);
            Simvol.STORY.setFlag(StoryFlag.ACT1_COMPLETED);
        })
        .build();

        return DialogueNode.detective(
            "«Мы видели деревню»"
        )
        .voice("act1_inscription")
        .autoAdvance(100)
        .next(turns)
        .build();
    }
}
