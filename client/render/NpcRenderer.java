package com.simvol.mod.client.render;

import com.simvol.mod.entity.base.BaseNPC;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * Универсальный рендерер для всех NPC.
 * Принимает пути к модели, текстуре и анимациям.
 */
public class NpcRenderer<T extends BaseNPC>
        extends GeoEntityRenderer<T> {

    private final Identifier texture;
    private final Identifier model;
    private final Identifier animation;

    public NpcRenderer(EntityRendererFactory.Context ctx,
                        Identifier model,
                        Identifier texture,
                        Identifier animation) {
        super(ctx, new DefaultedEntityGeoModel<>(model));
        this.model     = model;
        this.texture   = texture;
        this.animation = animation;
    }

    @Override
    public Identifier getTextureLocation(T entity) {
        return texture;
    }

    // Масштаб — точно как Стив (1.0)
    @Override
    protected float getDeathMaxRotation(T entity) {
        return 0f; // NPC не падают при "смерти"
    }
}
