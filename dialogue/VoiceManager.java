package com.simvol.mod.dialogue;

import com.simvol.mod.Simvol;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

/**
 * МЕНЕДЖЕР ГОЛОСОВОЙ ОЗВУЧКИ
 * ============================
 * Воспроизводит .ogg файлы из папки:
 *   resources/assets/simvol/sounds/voice/act0/
 *   resources/assets/simvol/sounds/voice/act1/
 *   ... и т.д.
 *
 * ПЛЕЙСХОЛДЕРЫ:
 * Если файл озвучки не найден — воспроизводится тишина,
 * ошибки не выбрасываются. Это позволяет работать с картой
 * пока озвучка ещё не записана.
 *
 * ФОРМАТ ID ГОЛОСОВОЙ ЛИНИИ:
 *   "act0_valeria_intro_1"
 *   → ищет файл: sounds/voice/act0/act0_valeria_intro_1.ogg
 *   → sound event: simvol:voice/act0/act0_valeria_intro_1
 */
public class VoiceManager {

    /** Текущий воспроизводимый звук */
    private String currentVoiceLine = null;

    /** Громкость голоса */
    private float voiceVolume = 1.0f;

    /** Звук заблокирован (например во время катсцены без озвучки) */
    private boolean muted = false;

    // =========================================================
    //  ТИК
    // =========================================================

    public void tick(MinecraftClient client) {
        // Здесь можно добавить логику синхронизации с субтитрами
        // Например: следить за прогрессом воспроизведения
    }

    // =========================================================
    //  ВОСПРОИЗВЕДЕНИЕ
    // =========================================================

    /**
     * Воспроизвести голосовую линию.
     *
     * @param voiceLineId ID линии, например "act0_valeria_intro_1"
     *
     * Метод определяет акт по префиксу (act0_, act1_, и т.д.)
     * и ищет файл в соответствующей папке.
     */
    public void play(String voiceLineId) {
        if (voiceLineId == null || voiceLineId.isEmpty()) return;
        if (muted) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        // Определяем папку по префиксу
        String folder = getFolderForLine(voiceLineId);

        // Sound event ID: simvol:voice/act0/act0_valeria_intro_1
        String soundId = "voice/" + folder + "/" + voiceLineId;
        Identifier id  = new Identifier(Simvol.MOD_ID, soundId);

        // Проверяем что sound event зарегистрирован
        var soundEvent = net.minecraft.registry.Registries.SOUND_EVENT.get(id);
        if (soundEvent == null) {
            // Плейсхолдер — файл ещё не записан, просто пропускаем
            Simvol.LOGGER.info("ГОЛОС: Плейсхолдер (файл не найден): " + soundId);
            return;
        }

        // Воспроизводим
        currentVoiceLine = voiceLineId;
        client.getSoundManager().play(
            PositionedSoundInstance.master(soundEvent, 1.0f, voiceVolume)
        );

        Simvol.LOGGER.info("ГОЛОС: Воспроизводится: " + soundId);
    }

    /**
     * Остановить текущую голосовую линию.
     */
    public void stop() {
        currentVoiceLine = null;
        // SoundManager не позволяет легко остановить конкретный звук
        // В полной реализации нужно хранить SoundInstance и останавливать его
    }

    // ── Утилиты ───────────────────────────────────────────────

    /**
     * Определяет папку по префиксу ID.
     * "act0_..." → "act0"
     * "act1_..." → "act1"
     * и т.д.
     */
    private String getFolderForLine(String id) {
        if (id.startsWith("act0_")) return "act0";
        if (id.startsWith("act1_")) return "act1";
        if (id.startsWith("act2_")) return "act2";
        if (id.startsWith("act3_")) return "act3";
        return "misc";
    }

    // ── Геттеры/Сеттеры ───────────────────────────────────────

    public void setVolume(float volume) {
        voiceVolume = Math.max(0f, Math.min(1f, volume));
    }

    public float getVolume() { return voiceVolume; }

    public void setMuted(boolean muted) { this.muted = muted; }

    public boolean isMuted() { return muted; }

    public String getCurrentVoiceLine() { return currentVoiceLine; }
}
