package com.simvol.mod.client.render;

import com.simvol.mod.ModContent;
import com.simvol.mod.entity.*;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * РЕГИСТРАЦИЯ РЕНДЕРЕРОВ ВСЕХ ENTITY
 * Вызывается из SimvolClient.onInitializeClient()
 */
public class ModRenderers {

    public static void registerAll() {
        // ── NPC ───────────────────────────────────────────────
        EntityRendererRegistry.register(ModContent.ENTITY_NINA,
            ctx -> new NpcRenderer<>(ctx, NpcNina.MODEL, NpcNina.TEXTURE, NpcNina.ANIM));

        EntityRendererRegistry.register(ModContent.ENTITY_RASHID,
            ctx -> new NpcRenderer<>(ctx, NpcRashid.MODEL, NpcRashid.TEXTURE, NpcRashid.ANIM));

        EntityRendererRegistry.register(ModContent.ENTITY_TOLYA,
            ctx -> new NpcRenderer<>(ctx, NpcTolya.MODEL, NpcTolya.TEXTURE, NpcTolya.ANIM));

        EntityRendererRegistry.register(ModContent.ENTITY_SEMYONYCH,
            ctx -> new NpcRenderer<>(ctx, NpcSemyonych.MODEL, NpcSemyonych.TEXTURE, NpcSemyonych.ANIM));

        EntityRendererRegistry.register(ModContent.ENTITY_GROMOV,
            ctx -> new NpcRenderer<>(ctx, NpcGromov.MODEL, NpcGromov.TEXTURE, NpcGromov.ANIM));

        EntityRendererRegistry.register(ModContent.ENTITY_VALERIA,
            ctx -> new NpcRenderer<>(ctx, NpcValeria.MODEL, NpcValeria.TEXTURE, NpcValeria.ANIM));

        EntityRendererRegistry.register(ModContent.ENTITY_BOSS,
            ctx -> new NpcRenderer<>(ctx, NpcBoss.MODEL, NpcBoss.TEXTURE, NpcBoss.ANIM));

        EntityRendererRegistry.register(ModContent.ENTITY_DETECTIVE,
            ctx -> new NpcRenderer<>(ctx, NpcDetective.MODEL, NpcDetective.TEXTURE, NpcDetective.ANIM));

        // ── 3D Кнопки ─────────────────────────────────────────
        EntityRendererRegistry.register(ModContent.ENTITY_BUTTON_START,
            ctx -> new ButtonRenderer<>(ctx));

        EntityRendererRegistry.register(ModContent.ENTITY_BUTTON_CHOICE,
            ctx -> new ButtonRenderer<>(ctx));
    }
}
