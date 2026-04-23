package com.simvol.mod.entity;

import com.simvol.mod.entity.base.BaseNPC;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * НПС: ТОЛЯ
 * ~60 лет. Пьяница. Самый важный NPC акта 2.
 * Сидит на крыльце с бутылкой.
 * Диалог: act2_tolya_main
 */
public class NpcTolya extends BaseNPC {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Специальная анимация — пьёт из бутылки
    private static final RawAnimation ANIM_DRINK =
        RawAnimation.begin().thenLoop("animation.npc.tolya_drink");

    public NpcTolya(EntityType<? extends BaseNPC> type, World world) {
        super(type, world);
        this.dialogueId = "act2_tolya_main";
        // Толя сидит по умолчанию
        this.currentAnimState = NPCAnimState.SIT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar reg) {
        // Основной контроллер
        reg.add(new AnimationController<>(this, "tolya_ctrl", 5, state ->
            switch (currentAnimState) {
                case TALK -> state.setAndContinue(ANIM_TALK);
                case SIT  -> state.setAndContinue(ANIM_SIT);
                default   -> state.setAndContinue(ANIM_IDLE);
            }
        ));

        // Контроллер питья (работает поверх основного)
        reg.add(new AnimationController<>(this, "drink_ctrl", 5, state -> {
            // Толя периодически пьёт из бутылки
            // Каждые ~200 тиков
            if (this.age % 200 < 80) {
                return state.setAndContinue(ANIM_DRINK);
            }
            return PlayState.STOP;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }

    public static final net.minecraft.util.Identifier TEXTURE =
        new net.minecraft.util.Identifier("simvol", "textures/entity/tolya.png");
    public static final net.minecraft.util.Identifier MODEL =
        new net.minecraft.util.Identifier("simvol", "geo/npc_base.geo.json");
    public static final net.minecraft.util.Identifier ANIM =
        new net.minecraft.util.Identifier("simvol", "animations/npc_base.animation.json");
}
