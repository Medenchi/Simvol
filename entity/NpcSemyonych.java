package com.simvol.mod.entity;

import com.simvol.mod.entity.base.BaseNPC;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * НПС: СЕМЁНЫЧ
 * ~85 лет. Бывший охранник завода.
 * Сидит дома в кресле.
 * Диалог: act2_semyonych_main
 */
public class NpcSemyonych extends BaseNPC {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NpcSemyonych(EntityType<? extends BaseNPC> type, World world) {
        super(type, world);
        this.dialogueId = "act2_semyonych_main";
        this.currentAnimState = NPCAnimState.SIT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar reg) {
        reg.add(new AnimationController<>(this, "semyonych_ctrl", 8, state ->
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
        new net.minecraft.util.Identifier("simvol", "textures/entity/semyonych.png");
    public static final net.minecraft.util.Identifier MODEL =
        new net.minecraft.util.Identifier("simvol", "geo/npc_base.geo.json");
    public static final net.minecraft.util.Identifier ANIM =
        new net.minecraft.util.Identifier("simvol", "animations/npc_base.animation.json");
}
