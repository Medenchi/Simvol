package com.simvol.mod.dialogue.act;

import com.simvol.mod.Simvol;
import com.simvol.mod.SimvolClient;
import com.simvol.mod.dialogue.DialogueChoice;
import com.simvol.mod.dialogue.DialogueNode;
import com.simvol.mod.story.StoryFlag;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ВСЕ ДИАЛОГИ АКТА 2 — «ДЕРЕВНЯ»
 * =================================
 */
public class Act2Dialogues {

    public static Map<String, DialogueNode> buildAll() {
        Map<String, DialogueNode> map = new LinkedHashMap<>();

        map.put("act2_enter_village",     buildEnterVillage());
        map.put("act2_nina_main",         buildNinaMain());
        map.put("act2_rashid_main",       buildRashidMain());
        map.put("act2_tolya_main",        buildTolyaMain());
        map.put("act2_empty_house",       buildEmptyHouse());
        map.put("act2_semyonych_main",    buildSemyonychMain());
        map.put("act2_gromov_main",       buildGromovMain());

        // Находки символов
        map.put("act2_symbol_nina",       buildSymbolNina());
        map.put("act2_symbol_rashid",     buildSymbolRashid());
        map.put("act2_symbol_tolya",      buildSymbolTolya());
        map.put("act2_symbol_empty",      buildSymbolEmpty());
        map.put("act2_symbol_semyonych",  buildSymbolSemyonych());

        // Финал акта
        map.put("act2_village_overview",  buildVillageOverview());

        return map;
    }

    // =========================================================
    //  ВХОД В ДЕРЕВНЮ
    // =========================================================

    private static DialogueNode buildEnterVillage() {

        DialogueNode third = DialogueNode.detective(
            "Что здесь такого страшного, " +
            "чтобы умирать за это знание?"
        )
        .voice("act2_enter_3")
        .autoAdvance(100)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT2_ENTERED_VILLAGE))
        .build();

        DialogueNode second = DialogueNode.detective(
            "Дым из труб, лай собак, " +
            "бельё на верёвках. " +
            "Всё выглядит абсолютно обыденно."
        )
        .voice("act2_enter_2")
        .autoAdvance(120)
        .next(third)
        .build();

        return DialogueNode.detective(
            "Деревня… Прямо за заводом. " +
            "«Мы видели деревню.»"
        )
        .voice("act2_enter_1")
        .autoAdvance(100)
        .next(second)
        .build();
    }

    // =========================================================
    //  НИНА
    // =========================================================

    private static DialogueNode buildNinaMain() {

        // Ответ если спросил про завод
        DialogueNode ninaFactoryAnswer = DialogueNode.builder("Нина",
            "Завод? Давно закрытый. " +
            "Незачем туда ходить. Опасно там. " +
            "Чай будете?"
        )
        .voice("act2_nina_factory")
        .autoAdvance(120)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT2_TALKED_NINA))
        .build();

        // Ответ на другие варианты
        DialogueNode ninaGeneral = DialogueNode.builder("Нина",
            "Места у нас красивые. " +
            "Тихие. Всегда тихие."
        )
        .voice("act2_nina_general")
        .autoAdvance(100)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT2_TALKED_NINA))
        .build();

        // Главный узел с выборами
        return DialogueNode.builder("Нина",
            "Ой, молодой человек! " +
            "Вы к нам? Из города небось?"
        )
        .voice("act2_nina_greet")
        .choices(
            DialogueChoice.of(
                "Да, турист. Красивые места.",
                ninaGeneral
            ),
            DialogueChoice.of(
                "Работаю. Изучаю старые заводы.",
                ninaFactoryAnswer
            ),
            DialogueChoice.of(
                "Ищу кое-кого.",
                ninaGeneral
            )
        )
        .build();
    }

    private static DialogueNode buildSymbolNina() {

        DialogueNode second = DialogueNode.detective(
            "Она не знает. " +
            "Или делает вид, что не знает."
        )
        .voice("act2_symbol_nina_2")
        .autoAdvance(80)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT2_FOUND_SYMBOL_NINA);
            Simvol.PARANOIA.add(1,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.FOUND_SYMBOL);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.SYMBOL_NINA
                );
            }
            checkAllSymbols();
        })
        .build();

        return DialogueNode.detective(
            "Что это… " +
            "Символ."
        )
        .voice("act2_symbol_nina_1")
        .autoAdvance(80)
        .next(second)
        .build();
    }

    // =========================================================
    //  РАШИД
    // =========================================================

    private static DialogueNode buildRashidMain() {

        // Если рассказали про робу — эмоциональная сцена
        DialogueNode rashidEmotional = DialogueNode.builder("Рашид",
            "Значит он не убежал… " +
            "Он пытался… " +
            "Спасибо. Я ждал это сорок пять лет."
        )
        .voice("act2_rashid_emotional")
        .autoAdvance(200)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT2_TOLD_RASHID_ABOUT_ROBE);
            Simvol.PARANOIA.add(1,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.READ_DIARY);
        })
        .build();

        // Рассказать про робу
        DialogueNode detectiveTellsRobe = DialogueNode.detective(
            "Я нашёл робу вашего отца на заводе. " +
            "На спине написано: " +
            "«Если найдёшь — иди в подвал. Мы там.»"
        )
        .voice("act2_detective_robe")
        .autoAdvance(140)
        .next(rashidEmotional)
        .build();

        // Ответ на вопрос про отца
        DialogueNode rashidAboutFather = DialogueNode.builder("Рашид",
            "Мне было десять лет, когда отец исчез. " +
            "Сказали — несчастный случай. " +
            "Тела не показали."
        )
        .voice("act2_rashid_father")
        .autoAdvance(140)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT2_TALKED_RASHID))
        .choices(
            DialogueChoice.conditional(
                "Я нашёл его робу на заводе.",
                detectiveTellsRobe,
                () -> Simvol.STORY.hasFlag(StoryFlag.ACT1_FOUND_ROBE_ALIEVA)
            ),
            DialogueChoice.end(
                "Мне жаль.",
                () -> {}
            )
        )
        .build();

        return DialogueNode.builder("Рашид",
            "Чем могу?"
        )
        .voice("act2_rashid_greet")
        .choices(
            DialogueChoice.of(
                "Ваш отец работал на заводе?",
                rashidAboutFather
            ),
            DialogueChoice.end("Ничего, спасибо.")
        )
        .build();
    }

    private static DialogueNode buildSymbolRashid() {

        DialogueNode second = DialogueNode.detective(
            "Снова он. " +
            "В доме сына человека, которого убили на заводе. " +
            "Они не знают."
        )
        .voice("act2_symbol_rashid_2")
        .autoAdvance(120)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT2_FOUND_SYMBOL_RASHID);
            Simvol.PARANOIA.add(1,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.FOUND_SYMBOL);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.SYMBOL_RASHID
                );
            }
            checkAllSymbols();
        })
        .build();

        return DialogueNode.detective(
            "За батареей…"
        )
        .voice("act2_symbol_rashid_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    // =========================================================
    //  ТОЛЯ
    // =========================================================

    private static DialogueNode buildTolyaMain() {

        // Финал — Толя рисует символ в воздухе
        DialogueNode tolyaFinal = DialogueNode.builder("Толя",
            "Думаешь у меня тоже есть? " +
            "Я искал. Не нашёл. " +
            "Но это ничего не значит… " +
            "Может они меня оставили как наблюдать за самим собой."
        )
        .voice("act2_tolya_final")
        .autoAdvance(180)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT2_TOLYA_REVEALED_DEPT8);
            Simvol.PARANOIA.add(2,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.TALKED_TO_TOLYA);
        })
        .build();

        // Толя рисует символ
        DialogueNode tolyaSymbol = DialogueNode.builder("Толя",
            "Каждый дом. Каждую семью. Сорок лет."
        )
        .voice("act2_tolya_symbol")
        .autoAdvance(100)
        .next(tolyaFinal)
        .build();

        // Основной монолог Толи
        DialogueNode tolyaMain = DialogueNode.builder("Толя",
            "Нас было пятеро. " +
            "Четверо уже умерли. Я остался."
        )
        .voice("act2_tolya_main_2")
        .autoAdvance(120)
        .next(tolyaSymbol)
        .build();

        DialogueNode tolyaTells = DialogueNode.builder("Толя",
            "Я сам там работал. " +
            "Молодой был, дурной. Восьмой отдел. " +
            "Нам говорили — контроль качества. " +
            "Мы маркировали вещи."
        )
        .voice("act2_tolya_tells")
        .autoAdvance(160)
        .next(tolyaMain)
        .build();

        // Если ответить про завод
        DialogueNode tolyaFactory = DialogueNode.builder("Толя",
            "С завода?"
        )
        .voice("act2_tolya_factory")
        .autoAdvance(50)
        .next(tolyaTells)
        .build();

        return DialogueNode.builder("Толя",
            "С завода идёшь?"
        )
        .voice("act2_tolya_greet")
        .choices(
            DialogueChoice.of(
                "Да. Что там можно увидеть?",
                tolyaFactory
            ),
            DialogueChoice.end("Нет, просто прохожу.")
        )
        .build();
    }

    private static DialogueNode buildSymbolTolya() {

        DialogueNode second = DialogueNode.detective(
            "Он маркировал других " +
            "и не знал, что маркируют его."
        )
        .voice("act2_symbol_tolya_2")
        .autoAdvance(100)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT2_FOUND_SYMBOL_TOLYA);
            Simvol.PARANOIA.add(1,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.FOUND_SYMBOL);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.SYMBOL_TOLYA
                );
            }
            checkAllSymbols();
        })
        .build();

        return DialogueNode.detective(
            "Он искал. Сдвигал ковёр. " +
            "Но не поднял полностью. " +
            "Вот он…"
        )
        .voice("act2_symbol_tolya_1")
        .autoAdvance(100)
        .next(second)
        .build();
    }

    // =========================================================
    //  ПУСТОЙ ДОМ
    // =========================================================

    private static DialogueNode buildEmptyHouse() {

        // Детский рисунок
        DialogueNode drawing = DialogueNode.detective(
            "Ребёнок видел символы. " +
            "Для него это было обычным…"
        )
        .voice("act2_drawing")
        .autoAdvance(120)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT2_FOUND_CHILDS_DRAWING);
            Simvol.PARANOIA.add(3,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.FOUND_SYMBOL);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.CHILDS_DRAWING
                );
            }
        })
        .build();

        // Дневник с вырванными страницами
        DialogueNode diary = DialogueNode.builder("Дневник",
            "«Я начинаю замечать знаки. " +
            "На вещах. На посуде. На мебели. " +
            "Один и тот же знак везде. " +
            "Завтра спрошу у Виктора.»"
        )
        .voice("act2_empty_diary")
        .autoAdvance(160)
        .next(drawing)
        .build();

        // Вход в пустой дом
        return DialogueNode.detective(
            "Недопитая чашка чая. " +
            "Открытая книга. Очки. " +
            "Всё осталось как было."
        )
        .voice("act2_empty_house")
        .autoAdvance(120)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT2_ENTERED_EMPTY_HOUSE))
        .next(diary)
        .build();
    }

    private static DialogueNode buildSymbolEmpty() {

        DialogueNode second = DialogueNode.detective(
            "1981 год… " +
            "Они продолжили маркировку " +
            "уже здесь, в деревне."
        )
        .voice("act2_symbol_empty_2")
        .autoAdvance(100)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT2_FOUND_SYMBOL_EMPTY_HOUSE);
            Simvol.PARANOIA.add(1,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.FOUND_SYMBOL);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.SYMBOL_EMPTY_HOUSE
                );
            }
            checkAllSymbols();
        })
        .build();

        return DialogueNode.detective(
            "На обратной стороне иконы…"
        )
        .voice("act2_symbol_empty_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    // =========================================================
    //  СЕМЁНЫЧ
    // =========================================================

    private static DialogueNode buildSemyonychMain() {

        // Семёныч признаётся
        DialogueNode semyonychAdmits = DialogueNode.builder("Семёныч",
            "Мне сказали — они вышли через специальный выход. " +
            "Я поверил. " +
            "Или сделал вид, что поверил."
        )
        .voice("act2_semyonych_admits")
        .autoAdvance(160)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT2_SEMYONYCH_ADMITTED);
            Simvol.PARANOIA.add(2,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.TALKED_TO_SEMYONYCH);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.SEMYONYCH_TESTIMONY
                );
            }
        })
        .build();

        // Про 15 марта
        DialogueNode semyonychMarch = DialogueNode.builder("Семёныч",
            "Я ту запись сам делал. " +
            "Восемнадцать вошли. Пятнадцать вышли. " +
            "Я считал. Я видел."
        )
        .voice("act2_semyonych_march")
        .autoAdvance(160)
        .next(semyonychAdmits)
        .build();

        // Пришёл с завода
        DialogueNode semyonychKnows = DialogueNode.builder("Семёныч",
            "С завода."
        )
        .voice("act2_semyonych_knows")
        .autoAdvance(40)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT2_TALKED_SEMYONYCH))
        .next(semyonychMarch)
        .build();

        return DialogueNode.builder("Семёныч",
            "С завода."
        )
        .voice("act2_semyonych_greet")
        .choices(
            DialogueChoice.of(
                "Про 15 марта 1979 года...",
                semyonychMarch
            ),
            DialogueChoice.end("Просто проходил.")
        )
        .build();
    }

    private static DialogueNode buildSymbolSemyonych() {

        DialogueNode second = DialogueNode.detective(
            "На обратной стороне иконы. " +
            "Дата рядом — «1981»."
        )
        .voice("act2_symbol_semyonych_2")
        .autoAdvance(80)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT2_FOUND_SYMBOL_SEMYONYCH);
            Simvol.PARANOIA.add(1,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.FOUND_SYMBOL);
            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.SYMBOL_SEMYONYCH
                );
            }
            checkAllSymbols();
        })
        .build();

        return DialogueNode.detective(
            "Подождите…"
        )
        .voice("act2_symbol_semyonych_1")
        .autoAdvance(60)
        .next(second)
        .build();
    }

    // =========================================================
    //  ГРОМОВ — ФИНАЛ АКТА
    // =========================================================

    private static DialogueNode buildGromovMain() {

        // Громов показывает татуировку
        DialogueNode tattoo = DialogueNode.builder("Громов",
            "Я и то, и другое. " +
            "Я знаю об эксперименте. " +
            "И я часть эксперимента. " +
            "Это я сам сделал, " +
            "чтобы не забывать, " +
            "что я тоже под наблюдением."
        )
        .voice("act2_gromov_tattoo")
        .autoAdvance(200)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT2_GROMOV_TOLD_TRUTH);
            Simvol.STORY.setFlag(StoryFlag.ACT2_COMPLETED);
        })
        .build();

        // Самое страшное открытие
        DialogueNode gromovMain3 = DialogueNode.builder("Громов",
            "Самое страшное открытие было не в том, " +
            "что за ними следят. " +
            "А в том, что людям это безразлично."
        )
        .voice("act2_gromov_main_3")
        .autoAdvance(160)
        .next(tattoo)
        .build();

        // Суть эксперимента
        DialogueNode gromovMain2 = DialogueNode.builder("Громов",
            "Завод был прикрытием. " +
            "Деревня — эксперимент. " +
            "Мы изучали, как люди живут, " +
            "когда за ними наблюдают, " +
            "и как живут, когда не знают."
        )
        .voice("act2_gromov_main_2")
        .autoAdvance(200)
        .next(gromovMain3)
        .build();

        // Гость ожидаемый
        DialogueNode gromovFirst = DialogueNode.builder("Громов",
            "Я ждал. Долго ждал."
        )
        .voice("act2_gromov_first")
        .autoAdvance(80)
        .next(gromovMain2)
        .build();

        return gromovFirst;
    }

    // =========================================================
    //  ФИНАЛ АКТА — ОБЗОР ДЕРЕВНИ
    // =========================================================

    private static DialogueNode buildVillageOverview() {

        DialogueNode fourth = DialogueNode.detective(
            "И остановить это нельзя. " +
            "По словам человека, который это создал."
        )
        .voice("act2_overview_4")
        .autoAdvance(100)
        .build();

        DialogueNode third = DialogueNode.detective(
            "Дети под наблюдением. " +
            "Дети детей."
        )
        .voice("act2_overview_3")
        .autoAdvance(80)
        .next(fourth)
        .build();

        DialogueNode second = DialogueNode.detective(
            "Они не знают. Никто из них не знает. " +
            "Сорок пять лет под наблюдением."
        )
        .voice("act2_overview_2")
        .autoAdvance(120)
        .next(third)
        .build();

        return DialogueNode.detective(
            "Так что мне делать?"
        )
        .voice("act2_overview_1")
        .autoAdvance(100)
        .next(second)
        .build();
    }

    // =========================================================
    //  УТИЛИТЫ
    // =========================================================

    /**
     * Проверяем — найдены ли все символы.
     * Если да — бонус паранойи и флаг.
     */
    private static void checkAllSymbols() {
        var story = Simvol.STORY;
        if (story.hasFlag(StoryFlag.ACT2_FOUND_SYMBOL_NINA)     &&
            story.hasFlag(StoryFlag.ACT2_FOUND_SYMBOL_RASHID)   &&
            story.hasFlag(StoryFlag.ACT2_FOUND_SYMBOL_TOLYA)    &&
            story.hasFlag(StoryFlag.ACT2_FOUND_SYMBOL_EMPTY_HOUSE) &&
            story.hasFlag(StoryFlag.ACT2_FOUND_SYMBOL_SEMYONYCH)) {

            story.setFlag(StoryFlag.ACT2_FOUND_ALL_SYMBOLS);
            Simvol.PARANOIA.add(2,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.FOUND_ALL_SYMBOLS);

            if (Simvol.DIARY != null) {
                Simvol.DIARY.addEvidence(
                    com.simvol.mod.diary.EvidenceRegistry.ALL_SYMBOLS_FOUND
                );
            }
        }
    }
}
