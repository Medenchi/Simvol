package com.simvol.mod.entity.base;

import com.simvol.mod.SimvolClient;
import com.simvol.mod.dialogue.DialogueEngine;
import com.simvol.mod.story.StoryManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Базовый класс для всех NPC мода "Символ".
 *
 * Все NPC:
 * - Используют GeckoLib для анимации
 * - Стоят неподвижно, пока игрок не подходит
 * - При взаимодействии запускают диалог
 * - Не атакуют и не убегают
 * - Имеют анимации: idle, talk, react_positive, react_negative, sit
 */
public abstract class BaseNPC extends MobEntity implements GeoEntity {

    // ── GeckoLib ──────────────────────────────────────────────

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    // Стандартные анимации (имена должны совпадать с JSON)
    protected static final RawAnimation ANIM_IDLE            = RawAnimation.begin().thenLoop("animation.npc.idle");
    protected static final RawAnimation ANIM_TALK            = RawAnimation.begin().thenLoop("animation.npc.talk");
    protected static final RawAnimation ANIM_REACT_POSITIVE  = RawAnimation.begin().thenPlay("animation.npc.react_positive");
    protected static final RawAnimation ANIM_REACT_NEGATIVE  = RawAnimation.begin().thenPlay("animation.npc.react_negative");
    protected static final RawAnimation ANIM_SIT             = RawAnimation.begin().thenLoop("animation.npc.sit");
    protected static final RawAnimation ANIM_WALK            = RawAnimation.begin().thenLoop("animation.npc.walk");

    // Текущее состояние анимации
    protected NPCAnimState currentAnimState = NPCAnimState.IDLE;

    // ID диалога этого NPC (задаётся в подклассе)
    protected String dialogueId = "";

    // Смотреть ли на игрока при диалоге
    protected boolean lookAtPlayer = true;

    public enum NPCAnimState {
        IDLE, TALK, REACT_POSITIVE, REACT_NEGATIVE, SIT, WALK
    }

    // ── Конструктор ───────────────────────────────────────────

    public BaseNPC(EntityType<? extends MobEntity> type, World world) {
        super(type, world);
        // NPC не двигаются сами
        this.setAiDisabled(true);
    }

    // ── Атрибуты ──────────────────────────────────────────────

    public static DefaultAttributeContainer.Builder createNPCAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0)  // Не двигается
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0); // Не отбрасывается
    }

    // ── Взаимодействие ────────────────────────────────────────

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (world.isClient()) {
            // Запускаем диалог только на клиенте
            if (!dialogueId.isEmpty()) {
                DialogueEngine engine = SimvolClient.CUTSCENE.getDialogueEngine();
                if (engine != null) {
                    engine.startDialogue(dialogueId, this, player);
                    setAnimState(NPCAnimState.TALK);
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    // ── Тик ───────────────────────────────────────────────────

    @Override
    public void tick() {
        super.tick();
        // Смотрим на ближайшего игрока
        if (lookAtPlayer && !world.isClient()) {
            PlayerEntity nearest = world.getClosestPlayer(this, 8.0);
            if (nearest != null) {
                this.getLookControl().lookAt(nearest, 30f, 30f);
            }
        }
    }

    // ── GeckoLib анимации ─────────────────────────────────────

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "main_controller", 5, this::animController));
    }

    protected PlayState animController(AnimationState<BaseNPC> state) {
        return switch (currentAnimState) {
            case IDLE            -> state.setAndContinue(ANIM_IDLE);
            case TALK            -> state.setAndContinue(ANIM_TALK);
            case REACT_POSITIVE  -> state.setAndContinue(ANIM_REACT_POSITIVE);
            case REACT_NEGATIVE  -> state.setAndContinue(ANIM_REACT_NEGATIVE);
            case SIT             -> state.setAndContinue(ANIM_SIT);
            case WALK            -> state.setAndContinue(ANIM_WALK);
        };
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    // ── Утилиты ───────────────────────────────────────────────

    public void setAnimState(NPCAnimState state) {
        this.currentAnimState = state;
    }

    public void setDialogueId(String id) {
        this.dialogueId = id;
    }

    /** Защита от урона — NPC неуязвимы */
    @Override
    public boolean isInvulnerableTo(net.minecraft.entity.damage.DamageSource damageSource) {
        return true;
    }

    /** NPC не тонут, не горят */
    @Override
    public boolean canBreatheInWater() { return true; }

    @Override
    protected boolean isFireImmune() { return true; }
}
