package com.simvol.mod;

import com.simvol.mod.client.hud.HudOverlay;
import com.simvol.mod.client.hud.SubtitleOverlay;
import com.simvol.mod.client.render.ModRenderers;
import com.simvol.mod.client.screen.ModScreens;
import com.simvol.mod.cutscene.CutsceneEngine;
import com.simvol.mod.paranoia.ParanoiaEffects;
import com.simvol.mod.dialogue.VoiceManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

@Environment(EnvType.CLIENT)
public class SimvolClient implements ClientModInitializer {

    // Клиентские системы
    public static CutsceneEngine CUTSCENE;
    public static ParanoiaEffects PARANOIA_FX;
    public static VoiceManager VOICE;
    public static HudOverlay HUD;
    public static SubtitleOverlay SUBTITLES;

    @Override
    public void onInitializeClient() {
        Simvol.LOGGER.info("СИМВОЛ: Инициализация клиента...");

        // Инициализация клиентских систем
        CUTSCENE    = new CutsceneEngine();
        PARANOIA_FX = new ParanoiaEffects();
        VOICE       = new VoiceManager();
        HUD         = new HudOverlay();
        SUBTITLES   = new SubtitleOverlay();

        // Регистрация рендереров entity (NPC + кнопки)
        ModRenderers.registerAll();

        // Регистрация экранов GUI
        ModScreens.registerAll();

        // HUD: letterbox + паранойя + субтитры
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            HUD.render(drawContext, tickDelta);
            SUBTITLES.render(drawContext, tickDelta);
            PARANOIA_FX.renderHud(drawContext, tickDelta);
        });

        // Тик клиента
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            CUTSCENE.tick(client);
            PARANOIA_FX.tick(client);
            VOICE.tick(client);
        });

        Simvol.LOGGER.info("СИМВОЛ: Клиент готов.");
    }
}
