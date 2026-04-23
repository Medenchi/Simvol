package com.simvol.mod.entity;

import com.simvol.mod.entity.base.BaseNPC;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * НПС: НАЧАЛЬНИК АРГУСА
 * 55 лет. Холодный, доминирующий.
 * Сидит за столом в кабинете.
 * Диалог: act0_boss_fires
 */
public class NpcBoss extends BaseNPC {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NpcBoss(EntityType<? extends BaseNPC> type, World world) {
        super(type, world);
        this.dialogueId = "act0_boss_fires";
        this.currentAnimState = NPCAnimState.SIT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar reg) {
        reg.add(new AnimationController<>(this, "boss_ctrl", 5, state ->
            switch (currentAnimState) {
                case TALK            -> state.setAndContinue(ANIM_TALK);
                case REACT_NEGATIVE  -> state.setAndContinue(ANIM_REACT_NEGATIVE);
                case SIT             -> state.setAndContinue(ANIM_SIT);
                default              -> state.setAndContinue(ANIM_SIT);
            }
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }

    public static final net.minecraft.util.Identifier TEXTURE =
        new net.minecraft.util.Identifier("simvol", "textures/entity/boss.png");
    public static final net.minecraft.util.Identifier MODEL =
        new net.minecraft.util.Identifier("simvol", "geo/npc_base.geo.json");
    public static final net.minecraft.util.Identifier ANIM =
        new net.minecraft.util.Identifier("simvol", "animations/npc_base.animation.json");
}
