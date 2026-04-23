package com.simvol.mod.dialogue.act;

import com.simvol.mod.Simvol;
import com.simvol.mod.SimvolClient;
import com.simvol.mod.dialogue.DialogueChoice;
import com.simvol.mod.dialogue.DialogueNode;
import com.simvol.mod.story.StoryFlag;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ВСЕ ДИАЛОГИ АКТА 3 — «НАБЛЮДАТЕЛИ»
 * ======================================
 * Все четыре концовки.
 * Финальные монологи.
 * Титры.
 */
public class Act3Dialogues {

    public static Map<String, DialogueNode> buildAll() {
        Map<String, DialogueNode> map = new LinkedHashMap<>();

        map.put("act3_main_choice",    buildMainChoice());
        map.put("act3_ending_a",       buildEndingA());
        map.put("act3_ending_b",       buildEndingB());
        map.put("act3_ending_c",       buildEndingC());
        map.put("act3_ending_d",       buildEndingD());
        map.put("act3_gromov_passing", buildGromovPassing());
        map.put("act3_credits_text",   buildCreditsText());

        return map;
    }

    // =========================================================
    //  ГЛАВНЫЙ ВЫБОР
    // =========================================================

    private static DialogueNode buildMainChoice() {

        // Ветки концовок
        DialogueNode branchCall = DialogueNode.detective(
            "Звоню."
        )
        .voice("act3_branch_call")
        .autoAdvance(40)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT3_CALLED_POLICE))
        .build();

        DialogueNode branchLeave = DialogueNode.detective(
            "Ухожу."
        )
        .voice("act3_branch_leave")
        .autoAdvance(40)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT3_LEFT))
        .build();

        DialogueNode branchStay = DialogueNode.detective(
            "Нужно подумать."
        )
        .voice("act3_branch_stay")
        .autoAdvance(40)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT3_STAYED))
        .build();

        return DialogueNode.detective(
            "Так что мне делать?"
        )
        .choices(
            DialogueChoice.of(
                "Вызвать полицию.",
                branchCall
            ),
            DialogueChoice.of(
                "Уйти. Просто уйти.",
                branchLeave
            ),
            DialogueChoice.of(
                "Остаться. Подумать.",
                branchStay
            )
        )
        .build();
    }

    // =========================================================
    //  КОНЦОВКА A — «ПРАВДА»
    // =========================================================

    private static DialogueNode buildEndingA() {

        // Финальный текст
        DialogueNode finalText = DialogueNode.builder("",
            "Папа позвонил сегодня. Первый раз сам. " +
            "Спросил, как дела. Я сказал — хорошо. " +
            "Мы оба знали, что говорим не только об этом."
        )
        .voice("act3_ending_a_final")
        .autoAdvance(200)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ENDING_A);
            if (SimvolClient.HUD != null) {
                SimvolClient.HUD.actTransition(
                    "КОНЕЦ", "Правда"
                );
            }
        })
        .build();

        // Валерия
        DialogueNode valeria = DialogueNode.builder("Валерия",
            "Дело закрыто."
        )
        .voice("act3_valeria_closed")
        .autoAdvance(60)
        .next(finalText)
        .build();

        DialogueNode detectiveAnswer = DialogueNode.detective(
            "Нет. Формально — закрыто."
        )
        .voice("act3_detective_no")
        .autoAdvance(80)
        .next(valeria)
        .build();

        // Громов проходит мимо
        DialogueNode gromovPass = DialogueNode.builder("Громов",
            "Эксперимент не закончится."
        )
        .voice("act3_gromov_pass_a")
        .autoAdvance(100)
        .build();

        return gromovPass;
    }

    // =========================================================
    //  КОНЦОВКА B — «ЗАМЕНА»
    // =========================================================

    private static DialogueNode buildEndingB() {

        // Финальный текст
        DialogueNode finalText = DialogueNode.builder("",
            "Дело №47 закрыто. " +
            "Детектив вышел на пенсию через три месяца. " +
            "Переехал в деревню Стеклово."
        )
        .voice("act3_ending_b_final")
        .autoAdvance(200)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ENDING_B);
            if (SimvolClient.HUD != null) {
                SimvolClient.HUD.actTransition("КОНЕЦ", "Замена");
            }
        })
        .build();

        // Детектив рисует символ
        DialogueNode drawing = DialogueNode.detective(
            "..."
        )
        .autoAdvance(160)
        .next(finalText)
        .build();

        // Нина не отвечает на вопрос
        DialogueNode ninaSmile = DialogueNode.builder("Нина",
            "..."
        )
        .autoAdvance(100)
        .next(drawing)
        .build();

        return DialogueNode.detective(
            "Давно это здесь?"
        )
        .voice("act3_detective_cup_b")
        .autoAdvance(80)
        .next(ninaSmile)
        .build();
    }

    // =========================================================
    //  КОНЦОВКА C — «ОТРИЦАНИЕ»
    // =========================================================

    private static DialogueNode buildEndingC() {

        // Последний кадр — символ на донышке кружки
        DialogueNode cupSymbol = DialogueNode.builder("",
            "Восьмой отдел учёл новое " +
            "местонахождение субъекта для наблюдения."
        )
        .voice("act3_ending_c_final")
        .autoAdvance(200)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ENDING_C);
            if (SimvolClient.HUD != null) {
                SimvolClient.HUD.flashRed(15);
                SimvolClient.HUD.actTransition("КОНЕЦ", "Отрицание");
            }
        })
        .build();

        // Валерия хвалит
        DialogueNode valeriaGood = DialogueNode.builder("Валерия",
            "Молодец. Быстро управился."
        )
        .voice("act3_valeria_good_c")
        .autoAdvance(80)
        .next(cupSymbol)
        .build();

        return DialogueNode.detective(
            "Серийный убийца — Громов В.С. " +
            "Тела найдены. Дело закрыто."
        )
        .voice("act3_detective_report_c")
        .autoAdvance(120)
        .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ENDING_C))
        .next(valeriaGood)
        .build();
    }

    // =========================================================
    //  КОНЦОВКА D — «НАБЛЮДАТЕЛЬ»
    // =========================================================

    private static DialogueNode buildEndingD() {

        // Финальный текст
        DialogueNode finalText = DialogueNode.builder("",
            "Наблюдение продолжается."
        )
        .voice("act3_ending_d_final")
        .autoAdvance(200)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ENDING_D);
            if (SimvolClient.HUD != null) {
                SimvolClient.HUD.actTransition("КОНЕЦ", "Наблюдатель");
            }
        })
        .build();

        // Детектив встаёт в ряд
        DialogueNode join = DialogueNode.detective(
            "..."
        )
        .autoAdvance(120)
        .next(finalText)
        .build();

        // Молчание — правильный выбор
        DialogueNode silence = DialogueNode.detective(
            "..."
        )
        .autoAdvance(80)
        .onEnter(() -> {
            Simvol.STORY.setFlag(StoryFlag.ACT3_JOINED_WATCHERS);
        })
        .next(join)
        .build();

        // Громов спрашивает
        DialogueNode gromovAsks = DialogueNode.builder("Громов",
            "И что ты решил?"
        )
        .voice("act3_gromov_asks_d")
        .choices(
            DialogueChoice.of(
                "Я ухожу.",
                // Переходим к концовке C
                DialogueNode.detective("Ухожу.")
                    .autoAdvance(40)
                    .onEnter(() -> Simvol.STORY.setFlag(StoryFlag.ACT3_LEFT))
                    .build()
            ),
            DialogueChoice.silent(silence)
        )
        .build();

        return DialogueNode.builder("Громов",
            "Ты нашёл всё."
        )
        .voice("act3_gromov_found_all")
        .autoAdvance(80)
        .next(gromovAsks)
        .build();
    }

    // =========================================================
    //  ГРОМОВ ПРОХОДИТ МИМО (концовка A)
    // =========================================================

    private static DialogueNode buildGromovPassing() {

        DialogueNode detectiveReply = DialogueNode.detective(
            "Я знаю."
        )
        .voice("act3_detective_knows")
        .autoAdvance(60)
        .build();

        return DialogueNode.builder("Громов",
            "Эксперимент не закончится."
        )
        .voice("act3_gromov_passing")
        .autoAdvance(80)
        .next(detectiveReply)
        .build();
    }

    // =========================================================
    //  ПОСТ-ТИТРЫ
    // =========================================================

    private static DialogueNode buildCreditsText() {

        DialogueNode second = DialogueNode.builder("",
            "Стеклозаводы №1–6 не были найдены."
        )
        .voice("act3_credits_2")
        .autoAdvance(160)
        .build();

        return DialogueNode.builder("",
            "Стеклозавод №7 был закрыт."
        )
        .voice("act3_credits_1")
        .autoAdvance(140)
        .next(second)
        .build();
    }
}
