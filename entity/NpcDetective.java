package com.simvol.mod.entity;

import com.simvol.mod.entity.base.BaseNPC;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * НПС: ДЕТЕКТИВ (для катсцен)
 * Используется когда нужно показать детектива
 * от третьего лица во время катсцены.
 * Игрок в это время невидим.
 */
public class NpcDetective extends BaseNPC {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Специальные анимации детектива
    private static final RawAnimation ANIM_THROW_FOLDER =
        RawAnimation.begin().thenPlay("animation.npc.detective_throw_folder");
    private static final RawAnimation ANIM_RAGE =
        RawAnimation.begin().thenPlay("animation.npc.detective_rage");
    private static final RawAnimation ANIM_PACK_BOX =
        RawAnimation.begin().thenLoop("animation.npc.detective_pack_box");

    public enum DetectiveAnim {
        IDLE, WALK, THROW_FOLDER, RAGE, PACK_BOX, SIT
    }

    private DetectiveAnim detectiveAnim = DetectiveAnim.IDLE;

    public NpcDetective(EntityType<? extends BaseNPC> type, World world) {
        super(type, world);
        // Детектив не открывает диалог при клике
        this.dialogueId = "";
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar reg) {
        reg.add(new AnimationController<>(this, "detective_ctrl", 3, state ->
            switch (detectiveAnim) {
                case WALK         -> state.setAndContinue(ANIM_WALK);
                case THROW_FOLDER -> state.setAndContinue(ANIM_THROW_FOLDER);
                case RAGE         -> state.setAndContinue(ANIM_RAGE);
                case PACK_BOX     -> state.setAndContinue(ANIM_PACK_BOX);
                case SIT          -> state.setAndContinue(ANIM_SIT);
                default           -> state.setAndContinue(ANIM_IDLE);
            }
        ));
    }

    public void setDetectiveAnim(DetectiveAnim anim) {
        this.detectiveAnim = anim;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }

    public static final net.minecraft.util.Identifier TEXTURE =
        new net.minecraft.util.Identifier("simvol", "textures/entity/detective.png");
    public static final net.minecraft.util.Identifier MODEL =
        new net.minecraft.util.Identifier("simvol", "geo/npc_base.geo.json");
    public static final net.minecraft.util.Identifier ANIM =
        new net.minecraft.util.Identifier("simvol", "animations/npc_base.animation.json");
}
