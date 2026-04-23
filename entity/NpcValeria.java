package com.simvol.mod.entity;

import com.simvol.mod.entity.base.BaseNPC;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * НПС: ВАЛЕРИЯ
 * 42 года. Бывшая оперативница.
 * Сидит за столом в Ночном Дозоре, курит.
 * Диалог: act0_valeria_intro
 */
public class NpcValeria extends BaseNPC {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NpcValeria(EntityType<? extends BaseNPC> type, World world) {
        super(type, world);
        this.dialogueId = "act0_valeria_intro";
        this.currentAnimState = NPCAnimState.SIT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar reg) {
        reg.add(new AnimationController<>(this, "valeria_ctrl", 5, state ->
            switch (currentAnimState) {
                case TALK -> state.setAndContinue(ANIM_TALK);
                case SIT  -> state.setAndContinue(ANIM_SIT);
                default   -> state.setAndContinue(ANIM_SIT);
            }
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }

    public static final net.minecraft.util.Identifier TEXTURE =
        new net.minecraft.util.Identifier("simvol", "textures/entity/valeria.png");
    public static final net.minecraft.util.Identifier MODEL =
        new net.minecraft.util.Identifier("simvol", "geo/npc_base.geo.json");
    public static final net.minecraft.util.Identifier ANIM =
        new net.minecraft.util.Identifier("simvol", "animations/npc_base.animation.json");
}
