package com.simvol.mod.entity;

import com.simvol.mod.entity.base.BaseNPC;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * НПС: ГРОМОВ
 * Директор завода 1961-1979.
 * Очень старый. Сидит за столом.
 * Диалог: act2_gromov_main
 * Особая анимация: gromov_reveal (показывает татуировку)
 */
public class NpcGromov extends BaseNPC {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation ANIM_REVEAL =
        RawAnimation.begin().thenPlay("animation.npc.gromov_reveal");

    // Показывает ли Громов татуировку прямо сейчас
    private boolean isRevealing = false;

    public NpcGromov(EntityType<? extends BaseNPC> type, World world) {
        super(type, world);
        this.dialogueId = "act2_gromov_main";
        this.currentAnimState = NPCAnimState.SIT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar reg) {
        reg.add(new AnimationController<>(this, "gromov_ctrl", 8, state -> {
            if (isRevealing) {
                return state.setAndContinue(ANIM_REVEAL);
            }
            return switch (currentAnimState) {
                case TALK -> state.setAndContinue(ANIM_TALK);
                case SIT  -> state.setAndContinue(ANIM_SIT);
                default   -> state.setAndContinue(ANIM_SIT);
            };
        }));
    }

    /** Вызывается в диалоге когда Громов показывает татуировку */
    public void triggerReveal() {
        isRevealing = true;
        // Сбрасываем через 3 секунды
        // В реальности — через scheduler
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }

    public static final net.minecraft.util.Identifier TEXTURE =
        new net.minecraft.util.Identifier("simvol", "textures/entity/gromov.png");
    public static final net.minecraft.util.Identifier MODEL =
        new net.minecraft.util.Identifier("simvol", "geo/npc_base.geo.json");
    public static final net.minecraft.util.Identifier ANIM =
        new net.minecraft.util.Identifier("simvol", "animations/npc_base.animation.json");
}
