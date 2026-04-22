package com.simvol.mod.cutscene;

import net.minecraft.client.MinecraftClient;

/**
 * Действие выполняемое в начале keyframe-а катсцены.
 *
 * Это функциональный интерфейс — можно писать как лямбду:
 *
 *   CutsceneAction playSound = (client, engine) -> {
 *       client.getSoundManager().play(...);
 *   };
 *
 *   CutsceneAction startDialogue = (client, engine) -> {
 *       engine.getDialogueEngine().startDialogue("boss_speaks", null, client.player);
 *   };
 *
 * ГОТОВЫЕ ФАБРИЧНЫЕ МЕТОДЫ (используй их, не пиши лямбды вручную):
 *
 *   CutsceneAction.dialogue("dialogue_id")
 *   CutsceneAction.sound("simvol:sfx/door_heavy")
 *   CutsceneAction.npcAnim(npcEntity, "talk")
 *   CutsceneAction.showTitle("АКТ 1 — СТЕКЛО")
 *   CutsceneAction.setFlag(StoryFlag.ACT0_FIRED)
 *   CutsceneAction.paranoia(2)
 *   CutsceneAction.end()
 *   CutsceneAction.chain(action1, action2, action3)   ← несколько сразу
 */
@FunctionalInterface
public interface CutsceneAction {

    /**
     * Выполнить действие.
     *
     * @param client  клиент Minecraft (доступ к игроку, звукам, экрану)
     * @param engine  движок катсцен (можно остановить, запустить диалог)
     */
    void execute(MinecraftClient client, CutsceneEngine engine);

    // =========================================================
    //  ГОТОВЫЕ ФАБРИЧНЫЕ МЕТОДЫ
    //  Используй их в кадрах вместо анонимных лямбд
    // =========================================================

    /**
     * Запустить диалог по ID.
     *
     * Пример:
     *   CutsceneFrame.of(x, y, z, yaw, pitch, 80,
     *       CutsceneAction.dialogue("boss_fires_detective"))
     */
    static CutsceneAction dialogue(String dialogueId) {
        return (client, engine) -> {
            if (client.player == null) return;
            engine.getDialogueEngine()
                  .startDialogue(dialogueId, null, client.player);
        };
    }

    /**
     * Воспроизвести звук.
     * soundId формат: "simvol:sfx/door_heavy"
     *
     * Пример:
     *   CutsceneAction.sound("simvol:sfx/door_heavy")
     */
    static CutsceneAction sound(String soundId) {
        return (client, engine) -> {
            if (client.player == null) return;
            var id = new net.minecraft.util.Identifier(soundId);
            var event = net.minecraft.registry.Registries.SOUND_EVENT.get(id);
            if (event != null) {
                client.player.playSound(event, 1.0f, 1.0f);
            }
        };
    }

    /**
     * Показать заголовок на экране (как в конце акта).
     *
     * Пример:
     *   CutsceneAction.showTitle("АКТ 1 — СТЕКЛО")
     *
     * Текст появится в центре экрана большими буквами,
     * как стандартный /title в Minecraft.
     */
    static CutsceneAction showTitle(String text) {
        return (client, engine) -> {
            if (client.player == null) return;
            // Отправляем title через встроенный механизм
            client.inGameHud.setTitle(
                net.minecraft.text.Text.literal(text)
            );
            client.inGameHud.setTitleTicks(10, 60, 20); // fadeIn, stay, fadeOut
        };
    }

    /**
     * Показать подзаголовок.
     *
     * Пример:
     *   CutsceneAction.showTitle("АКТ 1")
     *   CutsceneAction.showSubtitle("СТЕКЛО")
     */
    static CutsceneAction showSubtitle(String text) {
        return (client, engine) -> {
            if (client.player == null) return;
            client.inGameHud.setSubtitle(
                net.minecraft.text.Text.literal(text)
            );
        };
    }

    /**
     * Установить флаг сюжета.
     *
     * Пример:
     *   CutsceneAction.setFlag(StoryFlag.ACT0_FIRED)
     */
    static CutsceneAction setFlag(com.simvol.mod.story.StoryFlag flag) {
        return (client, engine) -> {
            com.simvol.mod.Simvol.STORY.setFlag(flag);
        };
    }

    /**
     * Добавить уровень паранойи.
     *
     * Пример:
     *   CutsceneAction.paranoia(2)  ← добавляет 2 уровня паранойи
     */
    static CutsceneAction paranoia(int amount) {
        return (client, engine) -> {
            com.simvol.mod.Simvol.PARANOIA.add(
                amount,
                com.simvol.mod.paranoia.ParanoiaSystem.Source.READ_DIARY
            );
        };
    }

    /**
     * Остановить катсцену досрочно (например после выбора в диалоге).
     */
    static CutsceneAction end() {
        return (client, engine) -> engine.stop();
    }

    /**
     * Включить/выключить letterbox.
     *
     * Пример:
     *   CutsceneAction.letterbox(true)   ← включить
     *   CutsceneAction.letterbox(false)  ← выключить
     */
    static CutsceneAction letterbox(boolean enable) {
        return (client, engine) -> engine.enableLetterbox(enable);
    }

    /**
     * Затемнение экрана (fade to black).
     * duration = тиков затемнения
     */
    static CutsceneAction fadeToBlack(int durationTicks) {
        return (client, engine) -> {
            com.simvol.mod.SimvolClient.HUD.startFadeToBlack(durationTicks);
        };
    }

    /**
     * Проиграть несколько действий СРАЗУ в одном кадре.
     *
     * Пример — начать диалог И воспроизвести звук одновременно:
     *   CutsceneAction.chain(
     *       CutsceneAction.sound("simvol:sfx/door_heavy"),
     *       CutsceneAction.dialogue("detective_enters")
     *   )
     */
    static CutsceneAction chain(CutsceneAction... actions) {
        return (client, engine) -> {
            for (CutsceneAction action : actions) {
                action.execute(client, engine);
            }
        };
    }

    /**
     * Выполнить действие с задержкой N тиков внутри кадра.
     * Полезно когда нужно: начать движение камеры, а через 1 секунду — звук.
     *
     * ВНИМАНИЕ: Это планирует действие, оно выполнится через N тиков
     * после начала кадра, не сразу.
     *
     * @param delayTicks задержка в тиках
     * @param action     действие
     */
    static CutsceneAction delayed(int delayTicks, CutsceneAction action) {
        // Реализуется через внутренний scheduler движка
        return (client, engine) -> {
            engine.scheduleAction(delayTicks, action);
        };
    }
}
