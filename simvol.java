package com.simvol.mod;

import com.simvol.mod.story.StoryManager;
import com.simvol.mod.paranoia.ParanoiaSystem;
import com.simvol.mod.diary.DiarySystem;
import com.simvol.mod.director.DirectorSystem;
import com.simvol.mod.data.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Simvol implements ModInitializer {

    public static final String MOD_ID = "simvol";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Главные системы мода
    public static StoryManager STORY;
    public static ParanoiaSystem PARANOIA;
    public static DiarySystem DIARY;
    public static DirectorSystem DIRECTOR;

    @Override
    public void onInitialize() {
        LOGGER.info("=== СИМВОЛ: Инициализация мода ===");

        // 1. Конфиг
        ModConfig.init();

        // 2. Регистрация всего контента (блоки, итемы, entity, звуки)
        ModContent.registerAll();

        // 3. Инициализация игровых систем
        STORY    = new StoryManager();
        PARANOIA = new ParanoiaSystem();
        DIARY    = new DiarySystem();
        DIRECTOR = new DirectorSystem();

        // 4. Серверные события
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            STORY.onServerStart(server);
            LOGGER.info("СИМВОЛ: Сервер запущен, сюжетная система готова.");
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            STORY.tick(server);
            PARANOIA.tickServer(server);
        });

        LOGGER.info("=== СИМВОЛ: Инициализация завершена ===");
    }
}
