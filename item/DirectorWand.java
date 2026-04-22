package com.simvol.mod.item;

import com.simvol.mod.SimvolClient;
import com.simvol.mod.director.DirectorSystem;
import com.simvol.mod.Simvol;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

/**
 * ПАЛОЧКА РЕЖИССЁРА
 * ==================
 * Главный инструмент для создания карты.
 *
 * УПРАВЛЕНИЕ:
 *   ПКМ              → открыть главное меню палочки
 *   ПКМ + SHIFT      → записать текущую позицию как кадр катсцены
 *   ЛКМ              → удалить последний записанный кадр
 *   ПКМ на entity    → выбрать entity (NPC / кнопку) для настройки
 *   ПКМ на блок      → выбрать блок для настройки
 */
public class DirectorWand extends Item {

    public DirectorWand(Settings settings) {
        super(settings);
    }

    // ── ПКМ ───────────────────────────────────────────────────

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient()) return TypedActionResult.pass(player.getStackInHand(hand));

        openWandMenu(player);
        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Environment(EnvType.CLIENT)
    private void openWandMenu(PlayerEntity player) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        // Если зажат Shift — записываем кадр напрямую без меню
        if (client.options.sneakKey.isPressed()) {
            SimvolClient.DIRECTOR_GUI.recordFrameNow(client);
            return;
        }

        // Открываем главное меню палочки
        client.setScreen(new com.simvol.mod.client.screen.WandScreen());
    }

    // ── Подсказки ─────────────────────────────────────────────

    @Override
    public void appendTooltip(ItemStack stack, World world,
                               java.util.List<net.minecraft.text.Text> tooltip,
                               net.minecraft.item.tooltip.TooltipType type) {
        tooltip.add(Text.literal("ПКМ")
                .formatted(Formatting.YELLOW)
                .append(Text.literal(" — открыть меню").formatted(Formatting.GRAY)));
        tooltip.add(Text.literal("SHIFT + ПКМ")
                .formatted(Formatting.YELLOW)
                .append(Text.literal(" — записать кадр").formatted(Formatting.GRAY)));
        tooltip.add(Text.literal("ЛКМ")
                .formatted(Formatting.YELLOW)
                .append(Text.literal(" — удалить последний кадр").formatted(Formatting.GRAY)));
    }
}
