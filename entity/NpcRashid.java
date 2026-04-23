package com.simvol.mod.entity;

import com.simvol.mod.entity.base.BaseNPC;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * НПС: РАШИД
 * 55 лет. Сын Алиева с завода.
 * Стоит у дома или колет дрова.
 * Диалог: act2_rashid_main
 */
public class NpcRashid extends BaseNPC {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NpcRashid(EntityType<? extends BaseNPC> type, World world) {
        super(type, world);
        this.dialogueId = "act2_rashid_main";
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar reg) {
        reg.add(new AnimationController<>(this, "rashid_ctrl", 5, state ->
            switch (currentAnimState) {
                case TALK            -> state.setAndContinue(ANIM_TALK);
                case REACT_POSITIVE  -> state.setAndContinue(ANIM_REACT_POSITIVE);
                case REACT_NEGATIVE  -> state.setAndContinue(ANIM_REACT_NEGATIVE);
                default              -> state.setAndContinue(ANIM_IDLE);
            }
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }

    public static final net.minecraft.util.Identifier TEXTURE =
        new net.minecraft.util.Identifier("simvol", "textures/entity/rashid.png");
    public static final net.minecraft.util.Identifier MODEL =
        new net.minecraft.util.Identifier("simvol", "geo/npc_base.geo.json");
    public static final net.minecraft.util.Identifier ANIM =
        new net.minecraft.util.Identifier("simvol", "animations/npc_base.animation.json");
}
