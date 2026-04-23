package com.simvol.mod.entity;

import com.simvol.mod.ModContent;
import com.simvol.mod.entity.base.BaseNPC;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * НПС: НИНА
 * Бабушка ~75 лет. Очень добрая внешне.
 * Сидит на лавочке или развешивает бельё.
 * Диалог: act2_nina_main
 */
public class NpcNina extends BaseNPC {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NpcNina(EntityType<? extends BaseNPC> type, World world) {
        super(type, world);
        this.dialogueId = "act2_nina_main";
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar reg) {
        reg.add(new AnimationController<>(this, "nina_ctrl", 5, state -> {
            return switch (currentAnimState) {
                case IDLE   -> state.setAndContinue(ANIM_IDLE);
                case TALK   -> state.setAndContinue(ANIM_TALK);
                case SIT    -> state.setAndContinue(ANIM_SIT);
                default     -> state.setAndContinue(ANIM_IDLE);
            };
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }

    // Текстура и модель
    public static final net.minecraft.util.Identifier TEXTURE =
        new net.minecraft.util.Identifier("simvol", "textures/entity/nina.png");
    public static final net.minecraft.util.Identifier MODEL =
        new net.minecraft.util.Identifier("simvol", "geo/npc_base.geo.json");
    public static final net.minecraft.util.Identifier ANIM =
        new net.minecraft.util.Identifier("simvol", "animations/npc_base.animation.json");
}
