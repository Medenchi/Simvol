package com.simvol.mod.cutscene;

import com.simvol.mod.story.StoryFlag;
import com.simvol.mod.cutscene.act.Act0Cutscenes;
import com.simvol.mod.cutscene.act.Act1Cutscenes;
import com.simvol.mod.cutscene.act.Act2Cutscenes;
import com.simvol.mod.cutscene.act.Act3Cutscenes;

/**
 * РЕЕСТР ВСЕХ КАТСЦЕН МОДА "СИМВОЛ"
 * ====================================
 * Здесь регистрируются все катсцены.
 * Вызывается один раз при старте клиента.
 *
 * КАК ДОБАВИТЬ НОВУЮ КАТСЦЕНУ:
 * 1. Создай CutsceneDef в нужном файле акта (Act0Cutscenes и т.д.)
 * 2. Зарегистрируй её здесь через engine.register(...)
 * 3. Запускай через: SimvolClient.CUTSCENE.play("твой_id", () -> {...})
 */
public class CutsceneRegistry {

    /**
     * Регистрирует все катсцены в движке.
     * Вызывается из SimvolClient.onInitializeClient()
     *
     * @param engine движок катсцен
     */
    public static void registerAll(CutsceneEngine engine) {

        // ── Нулевой Акт ───────────────────────────────────────
        engine.register(Act0Cutscenes.BUILDING_EXTERIOR);
        engine.register(Act0Cutscenes.FIRED_SCENE);
        engine.register(Act0Cutscenes.PACKING_BOXES);
        engine.register(Act0Cutscenes.WALK_OF_SHAME);
        engine.register(Act0Cutscenes.CITY_WALK);
        engine.register(Act0Cutscenes.NIGHT_WATCH_ARRIVAL);
        engine.register(Act0Cutscenes.HOME_LAPTOP);
        engine.register(Act0Cutscenes.RAGE_MOMENT);
        engine.register(Act0Cutscenes.WALK_TO_PARENTS);
        engine.register(Act0Cutscenes.INTERCOM_SCENE);
        engine.register(Act0Cutscenes.PARENTS_KITCHEN);
        engine.register(Act0Cutscenes.DECISION_MOMENT);

        // ── Акт 1 ─────────────────────────────────────────────
        engine.register(Act1Cutscenes.ARRIVE_AT_FACTORY);
        engine.register(Act1Cutscenes.FACTORY_EXTERIOR);
        engine.register(Act1Cutscenes.ENTER_FACTORY);
        engine.register(Act1Cutscenes.GATES_CLOSE);
        engine.register(Act1Cutscenes.GLASS_FORM_REVEAL);
        engine.register(Act1Cutscenes.DIARY_READ);
        engine.register(Act1Cutscenes.FINAL_ROOM);
        engine.register(Act1Cutscenes.ACT1_END);

        // ── Акт 2 ─────────────────────────────────────────────
        engine.register(Act2Cutscenes.ENTER_VILLAGE);
        engine.register(Act2Cutscenes.VILLAGE_OVERVIEW);
        engine.register(Act2Cutscenes.GROMOV_REVEAL);
        engine.register(Act2Cutscenes.ACT2_END);

        // ── Акт 3 ─────────────────────────────────────────────
        engine.register(Act3Cutscenes.AFTER_GROMOV);
        engine.register(Act3Cutscenes.ENDING_A);
        engine.register(Act3Cutscenes.ENDING_B);
        engine.register(Act3Cutscenes.ENDING_C);
        engine.register(Act3Cutscenes.ENDING_D);
        engine.register(Act3Cutscenes.CREDITS);
    }
}
