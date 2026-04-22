package com.simvol.mod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * Базовый класс всех декоративных блоков мода.
 * Все декоративные блоки наследуют этот класс.
 *
 * Особенности:
 * - Поддерживает полный куб (можно переопределить форму)
 * - При взаимодействии Палочкой Режиссёра открывает меню настройки
 * - Не горит
 * - Не имеет дроп-таблицы по умолчанию (только в Creative)
 */
public class DecorativeBlock extends Block {

    // Форма блока по умолчанию — полный куб
    protected VoxelShape SHAPE = VoxelShapes.fullCube();

    public DecorativeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world,
                                      BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                               PlayerEntity player, Hand hand, BlockHitResult hit) {
        // Если в руке Палочка Режиссёра — открываем меню блока
        if (!world.isClient()) return ActionResult.PASS;

        var stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof com.simvol.mod.item.DirectorWand) {
            // Открытие меню редактирования блока (обрабатывается в DirectorWand)
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    /**
     * Позволяет задать кастомную форму блока (для мебели и т.д.)
     */
    public DecorativeBlock withShape(VoxelShape shape) {
        this.SHAPE = shape;
        return this;
    }
}
