package com.simvol.mod;

import com.simvol.mod.block.DecorativeBlock;
import com.simvol.mod.block.StateBlock;
import com.simvol.mod.button3d.Button3DEntity;
import com.simvol.mod.button3d.Button3DStart;
import com.simvol.mod.button3d.Button3DChoice;
import com.simvol.mod.entity.*;
import com.simvol.mod.entity.base.BaseNPC;
import com.simvol.mod.item.DirectorWand;
import com.simvol.mod.item.DiaryItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ГЛАВНЫЙ ФАЙЛ РЕГИСТРАЦИЙ МОДА "СИМВОЛ"
 * =========================================
 * Содержит:
 *   - Все декоративные блоки (советская эстетика)
 *   - Все предметы (Палочка Режиссёра, Дневник)
 *   - Все entity (NPC + 3D кнопки)
 *   - Все звуки
 */
public class ModContent {

    // =========================================================
    //  ВСПОМОГАТЕЛЬНЫЕ КОЛЛЕКЦИИ
    // =========================================================

    // Карта всех блоков: id → блок (для авто-регистрации BlockItem)
    private static final Map<String, Block> BLOCKS = new LinkedHashMap<>();
    private static final Map<String, Item>  ITEMS  = new LinkedHashMap<>();

    // =========================================================
    //  БЛОКИ — СОВЕТСКАЯ ЭСТЕТИКА
    // =========================================================

    // ── СТЕНЫ И ПОВЕРХНОСТИ ──────────────────────────────────

    /** Советская керамическая плитка — три состояния */
    public static final Block SOVIET_TILE_NEW       = reg("soviet_tile_new",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.OFF_WHITE)
                    .hardness(1.5f).resistance(6f)
                    .sounds(BlockSoundGroup.STONE)));

    public static final Block SOVIET_TILE_WORN      = reg("soviet_tile_worn",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.LIGHT_GRAY)
                    .hardness(1.2f).resistance(4f)
                    .sounds(BlockSoundGroup.STONE)));

    public static final Block SOVIET_TILE_BROKEN    = reg("soviet_tile_broken",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.GRAY)
                    .hardness(0.8f).resistance(2f)
                    .sounds(BlockSoundGroup.STONE)));

    /** Бетонная панель — три состояния */
    public static final Block CONCRETE_PANEL_NEW    = reg("concrete_panel_new",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.STONE_GRAY)
                    .hardness(2.0f).resistance(8f)
                    .sounds(BlockSoundGroup.STONE)));

    public static final Block CONCRETE_PANEL_WORN   = reg("concrete_panel_worn",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.GRAY)
                    .hardness(1.8f).resistance(6f)
                    .sounds(BlockSoundGroup.STONE)));

    public static final Block CONCRETE_PANEL_BROKEN = reg("concrete_panel_broken",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.DARK_RED)
                    .hardness(1.2f).resistance(3f)
                    .sounds(BlockSoundGroup.STONE)));

    /** Штукатурка — три состояния */
    public static final Block PLASTER_NEW           = reg("plaster_new",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.OFF_WHITE)
                    .hardness(0.5f).sounds(BlockSoundGroup.STONE)));

    public static final Block PLASTER_WORN          = reg("plaster_worn",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.LIGHT_GRAY)
                    .hardness(0.4f).sounds(BlockSoundGroup.STONE)));

    public static final Block PLASTER_BROKEN        = reg("plaster_broken",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.GRAY)
                    .hardness(0.3f).sounds(BlockSoundGroup.STONE)));

    // ── ПОЛ ──────────────────────────────────────────────────

    /** Линолеум советский — три состояния */
    public static final Block LINOLEUM_NEW          = reg("linoleum_new",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
                    .mapColor(MapColor.GREEN)
                    .hardness(0.3f).sounds(BlockSoundGroup.WOOL)));

    public static final Block LINOLEUM_WORN         = reg("linoleum_worn",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
                    .mapColor(MapColor.DULL_GREEN)
                    .hardness(0.2f).sounds(BlockSoundGroup.WOOL)));

    public static final Block LINOLEUM_BROKEN       = reg("linoleum_broken",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
                    .mapColor(MapColor.BROWN)
                    .hardness(0.1f).sounds(BlockSoundGroup.WOOL)));

    /** Паркет деревянный советский */
    public static final Block PARQUET_NEW           = reg("parquet_new",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.OAK_TAN)
                    .hardness(1.0f).sounds(BlockSoundGroup.WOOD)));

    public static final Block PARQUET_WORN          = reg("parquet_worn",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .hardness(0.8f).sounds(BlockSoundGroup.WOOD)));

    public static final Block PARQUET_BROKEN        = reg("parquet_broken",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .hardness(0.5f).sounds(BlockSoundGroup.WOOD)));

    // ── ДЕРЕВО ───────────────────────────────────────────────

    /** Прогнившие доски — три состояния */
    public static final Block ROTTEN_PLANKS_LIGHT   = reg("rotten_planks_light",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.OAK_TAN)
                    .hardness(0.8f).sounds(BlockSoundGroup.WOOD)));

    public static final Block ROTTEN_PLANKS_MEDIUM  = reg("rotten_planks_medium",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .hardness(0.5f).sounds(BlockSoundGroup.WOOD)));

    public static final Block ROTTEN_PLANKS_HEAVY   = reg("rotten_planks_heavy",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.BROWN)
                    .hardness(0.2f).sounds(BlockSoundGroup.WOOD)));

    // ── МЕТАЛЛ ───────────────────────────────────────────────

    /** Ржавая металлическая панель */
    public static final Block RUST_PANEL_LIGHT      = reg("rust_panel_light",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.ORANGE)
                    .hardness(3.0f).resistance(8f)
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block RUST_PANEL_MEDIUM     = reg("rust_panel_medium",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .hardness(2.5f).resistance(6f)
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block RUST_PANEL_HEAVY      = reg("rust_panel_heavy",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.BROWN)
                    .hardness(1.5f).resistance(4f)
                    .sounds(BlockSoundGroup.METAL)));

    /** Металлическая решётка */
    public static final Block METAL_GRATE           = reg("metal_grate",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.GRAY)
                    .hardness(2.5f).resistance(6f)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block METAL_GRATE_RUSTY     = reg("metal_grate_rusty",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .hardness(2.0f).resistance(4f)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.METAL)));

    // ── СТЕКЛО ───────────────────────────────────────────────

    /** Старое советское стекло */
    public static final Block OLD_GLASS             = reg("old_glass",
            new DecorativeBlock(FabricBlockSettings.of(Material.GLASS)
                    .mapColor(MapColor.PALE_YELLOW)
                    .hardness(0.3f)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.GLASS)));

    public static final Block OLD_GLASS_CRACKED     = reg("old_glass_cracked",
            new DecorativeBlock(FabricBlockSettings.of(Material.GLASS)
                    .mapColor(MapColor.PALE_YELLOW)
                    .hardness(0.2f)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.GLASS)));

    /** Стеклянный блок с человеком (финал Акта 1) */
    public static final Block GLASS_TOMB            = reg("glass_tomb",
            new DecorativeBlock(FabricBlockSettings.of(Material.GLASS)
                    .mapColor(MapColor.CYAN)
                    .hardness(5.0f).resistance(1200f)
                    .luminance(s -> 3)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.GLASS)));

    // ── ТРУБЫ ────────────────────────────────────────────────

    /** Металлические трубы */
    public static final Block PIPE_HORIZONTAL       = reg("pipe_horizontal",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.GRAY)
                    .hardness(2.0f).resistance(6f)
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block PIPE_VERTICAL         = reg("pipe_vertical",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.GRAY)
                    .hardness(2.0f).resistance(6f)
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block PIPE_RUSTY_H          = reg("pipe_rusty_horizontal",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .hardness(1.5f).resistance(4f)
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block PIPE_RUSTY_V          = reg("pipe_rusty_vertical",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .hardness(1.5f).resistance(4f)
                    .sounds(BlockSoundGroup.METAL)));

    // ── ОСВЕЩЕНИЕ ────────────────────────────────────────────

    /** Советский плафон (потолочная лампа) */
    public static final Block SOVIET_LAMP           = reg("soviet_lamp",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.PALE_YELLOW)
                    .hardness(0.5f)
                    .luminance(s -> 15)
                    .sounds(BlockSoundGroup.METAL)));

    public static final Block SOVIET_LAMP_BROKEN    = reg("soviet_lamp_broken",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.GRAY)
                    .hardness(0.3f)
                    .luminance(s -> 0)
                    .sounds(BlockSoundGroup.METAL)));

    /** Флуоресцентная лампа (заводская) */
    public static final Block FLUORESCENT_LAMP      = reg("fluorescent_lamp",
            new DecorativeBlock(FabricBlockSettings.of(Material.GLASS)
                    .mapColor(MapColor.OFF_WHITE)
                    .hardness(0.3f)
                    .luminance(s -> 15)
                    .sounds(BlockSoundGroup.GLASS)));

    public static final Block FLUORESCENT_LAMP_OFF  = reg("fluorescent_lamp_off",
            new DecorativeBlock(FabricBlockSettings.of(Material.GLASS)
                    .mapColor(MapColor.LIGHT_GRAY)
                    .hardness(0.3f)
                    .luminance(s -> 0)
                    .sounds(BlockSoundGroup.GLASS)));

    /** Уличный фонарь советский */
    public static final Block STREET_LAMP           = reg("street_lamp",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.GRAY)
                    .hardness(1.5f)
                    .luminance(s -> 14)
                    .sounds(BlockSoundGroup.METAL)));

    // ── МЕБЕЛЬ И ОБОРУДОВАНИЕ ────────────────────────────────

    /** Деревянный ящик (завод) */
    public static final Block WOODEN_CRATE          = reg("wooden_crate",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.OAK_TAN)
                    .hardness(1.5f).sounds(BlockSoundGroup.WOOD)));

    public static final Block WOODEN_CRATE_OPEN     = reg("wooden_crate_open",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.OAK_TAN)
                    .hardness(1.0f).sounds(BlockSoundGroup.WOOD)));

    /** Металлическая бочка */
    public static final Block METAL_BARREL_CLEAN    = reg("metal_barrel_clean",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.GRAY)
                    .hardness(2.5f).sounds(BlockSoundGroup.METAL)));

    public static final Block METAL_BARREL_RUSTY    = reg("metal_barrel_rusty",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .hardness(2.0f).sounds(BlockSoundGroup.METAL)));

    /** Советский стул */
    public static final Block SOVIET_CHAIR          = reg("soviet_chair",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.OAK_TAN)
                    .hardness(1.0f).sounds(BlockSoundGroup.WOOD)));

    /** Советский стол */
    public static final Block SOVIET_DESK           = reg("soviet_desk",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.OAK_TAN)
                    .hardness(1.5f).sounds(BlockSoundGroup.WOOD)));

    /** Советский шкаф */
    public static final Block SOVIET_CABINET        = reg("soviet_cabinet",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .hardness(2.0f).sounds(BlockSoundGroup.WOOD)));

    /** Диван советский */
    public static final Block SOVIET_SOFA           = reg("soviet_sofa",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
                    .mapColor(MapColor.DULL_RED)
                    .hardness(0.8f).sounds(BlockSoundGroup.WOOL)));

    /** Холодильник советский */
    public static final Block SOVIET_FRIDGE         = reg("soviet_fridge",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.OFF_WHITE)
                    .hardness(2.0f).sounds(BlockSoundGroup.METAL)));

    /** Советская плита (кухня) */
    public static final Block SOVIET_STOVE          = reg("soviet_stove",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.OFF_WHITE)
                    .hardness(2.0f).sounds(BlockSoundGroup.METAL)));

    // ── ПЛАКАТЫ И ДЕКОР ──────────────────────────────────────

    /** Доска объявлений */
    public static final Block BULLETIN_BOARD        = reg("bulletin_board",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOD)
                    .mapColor(MapColor.OAK_TAN)
                    .hardness(0.5f).sounds(BlockSoundGroup.WOOD)));

    /** Советский плакат 1 (безопасность труда) */
    public static final Block SOVIET_POSTER_1       = reg("soviet_poster_1",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
                    .mapColor(MapColor.RED)
                    .hardness(0.1f).sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()));

    /** Советский плакат 2 (план пятилетки) */
    public static final Block SOVIET_POSTER_2       = reg("soviet_poster_2",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
                    .mapColor(MapColor.RED)
                    .hardness(0.1f).sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()));

    /** Советский плакат 3 (предупреждение) */
    public static final Block SOVIET_POSTER_3       = reg("soviet_poster_3",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
                    .mapColor(MapColor.DULL_RED)
                    .hardness(0.1f).sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()));

    /** Ковёр советский (настенный) */
    public static final Block SOVIET_CARPET_WALL    = reg("soviet_carpet_wall",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
                    .mapColor(MapColor.DULL_RED)
                    .hardness(0.3f).sounds(BlockSoundGroup.WOOL)
                    .nonOpaque()));

    // ── ЗАВОД СПЕЦИФИКА ──────────────────────────────────────

    /** Тяжёлая металлическая дверь (подвал) */
    public static final Block HEAVY_DOOR            = reg("heavy_door",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.GRAY)
                    .hardness(5.0f).resistance(20f)
                    .sounds(BlockSoundGroup.METAL)));

    /** Решётчатая дверь (клетка) */
    public static final Block CAGE_DOOR             = reg("cage_door",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.GRAY)
                    .hardness(3.0f).resistance(10f)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.METAL)));

    /** Матрас (подвал, финал Акта 1) */
    public static final Block MATTRESS_OLD          = reg("mattress_old",
            new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
                    .mapColor(MapColor.GRAY)
                    .hardness(0.2f).sounds(BlockSoundGroup.WOOL)));

    /** Старый ноутбук (кабинет детектива) */
    public static final Block OLD_LAPTOP            = reg("old_laptop",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.GRAY)
                    .hardness(0.8f).sounds(BlockSoundGroup.METAL)
                    .luminance(s -> 6)));

    /** Домофон советский */
    public static final Block INTERCOM              = reg("intercom",
            new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
                    .mapColor(MapColor.GRAY)
                    .hardness(1.0f).sounds(BlockSoundGroup.METAL)));

    /** Символ (прищуренный глаз) — скрытый маркер */
    public static final Block SYMBOL_MARKER         = reg("symbol_marker",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.BLACK)
                    .hardness(0.1f)
                    .nonOpaque()
                    .luminance(s -> 2)
                    .sounds(BlockSoundGroup.STONE)));

    // ── АРХИТЕКТУРНЫЕ ЭЛЕМЕНТЫ ───────────────────────────────

    /** Советский подоконник */
    public static final Block SOVIET_WINDOWSILL     = reg("soviet_windowsill",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.OFF_WHITE)
                    .hardness(1.0f).sounds(BlockSoundGroup.STONE)));

    /** Советский карниз */
    public static final Block SOVIET_CORNICE        = reg("soviet_cornice",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.OFF_WHITE)
                    .hardness(1.5f).sounds(BlockSoundGroup.STONE)));

    /** Ступенька бетонная */
    public static final Block CONCRETE_STEP         = reg("concrete_step",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.STONE_GRAY)
                    .hardness(2.0f).sounds(BlockSoundGroup.STONE)));

    /** Бордюр советский */
    public static final Block SOVIET_CURB           = reg("soviet_curb",
            new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
                    .mapColor(MapColor.OFF_WHITE)
                    .hardness(2.0f).sounds(BlockSoundGroup.STONE)));

    // =========================================================
    //  ПРЕДМЕТЫ
    // =========================================================

    /** Палочка Режиссёра — главный инструмент */
    public static final Item DIRECTOR_WAND          = regItem("director_wand",
            new DirectorWand(new FabricItemSettings().maxCount(1)));

    /** Дневник улик */
    public static final Item DIARY                  = regItem("diary",
            new DiaryItem(new FabricItemSettings().maxCount(1)));

    // =========================================================
    //  ENTITY TYPES — NPC
    // =========================================================

    public static EntityType<NpcNina>      ENTITY_NINA;
    public static EntityType<NpcRashid>    ENTITY_RASHID;
    public static EntityType<NpcTolya>     ENTITY_TOLYA;
    public static EntityType<NpcSemyonych> ENTITY_SEMYONYCH;
    public static EntityType<NpcGromov>    ENTITY_GROMOV;
    public static EntityType<NpcValeria>   ENTITY_VALERIA;
    public static EntityType<NpcBoss>      ENTITY_BOSS;
    public static EntityType<NpcDetective> ENTITY_DETECTIVE;

    // =========================================================
    //  ENTITY TYPES — 3D КНОПКИ
    // =========================================================

    public static EntityType<Button3DStart>  ENTITY_BUTTON_START;
    public static EntityType<Button3DChoice> ENTITY_BUTTON_CHOICE;

    // =========================================================
    //  ЗВУКИ
    // =========================================================

    // Идентификаторы звуков объявляются здесь,
    // регистрируются через SoundEvents в registerSounds()

    // =========================================================
    //  ГЛАВНЫЙ МЕТОД РЕГИСТРАЦИИ
    // =========================================================

    public static void registerAll() {
        registerBlocks();
        registerItems();
        registerEntities();
        registerSounds();
        Simvol.LOGGER.info("СИМВОЛ: Весь контент зарегистрирован. " +
                "Блоков: " + BLOCKS.size() +
                ", Предметов: " + ITEMS.size());
    }

    // ── Регистрация блоков ────────────────────────────────────

    private static void registerBlocks() {
        BLOCKS.forEach((id, block) -> {
            Registry.register(Registries.BLOCK, new Identifier(Simvol.MOD_ID, id), block);
            // Автоматически создаём BlockItem для каждого блока
            Registry.register(Registries.ITEM, new Identifier(Simvol.MOD_ID, id),
                    new BlockItem(block, new FabricItemSettings()));
        });
    }

    // ── Регистрация предметов ─────────────────────────────────

    private static void registerItems() {
        ITEMS.forEach((id, item) ->
                Registry.register(Registries.ITEM, new Identifier(Simvol.MOD_ID, id), item));
    }

    // ── Регистрация entity ────────────────────────────────────

    private static void registerEntities() {
        // NPC — все имеют размер Стива (0.6 × 1.8)
        ENTITY_NINA = Registry.register(Registries.ENTITY_TYPE,
                new Identifier(Simvol.MOD_ID, "npc_nina"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, NpcNina::new)
                        .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                        .build());

        ENTITY_RASHID = Registry.register(Registries.ENTITY_TYPE,
                new Identifier(Simvol.MOD_ID, "npc_rashid"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, NpcRashid::new)
                        .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                        .build());

        ENTITY_TOLYA = Registry.register(Registries.ENTITY_TYPE,
                new Identifier(Simvol.MOD_ID, "npc_tolya"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, NpcTolya::new)
                        .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                        .build());

        ENTITY_SEMYONYCH = Registry.register(Registries.ENTITY_TYPE,
                new Identifier(Simvol.MOD_ID, "npc_semyonych"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, NpcSemyonych::new)
                        .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                        .build());

        ENTITY_GROMOV = Registry.register(Registries.ENTITY_TYPE,
                new Identifier(Simvol.MOD_ID, "npc_gromov"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, NpcGromov::new)
                        .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                        .build());

        ENTITY_VALERIA = Registry.register(Registries.ENTITY_TYPE,
                new Identifier(Simvol.MOD_ID, "npc_valeria"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, NpcValeria::new)
                        .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                        .build());

        ENTITY_BOSS = Registry.register(Registries.ENTITY_TYPE,
                new Identifier(Simvol.MOD_ID, "npc_boss"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, NpcBoss::new)
                        .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                        .build());

        ENTITY_DETECTIVE = Registry.register(Registries.ENTITY_TYPE,
                new Identifier(Simvol.MOD_ID, "npc_detective"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, NpcDetective::new)
                        .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                        .build());

        // 3D Кнопки
        ENTITY_BUTTON_START = Registry.register(Registries.ENTITY_TYPE,
                new Identifier(Simvol.MOD_ID, "button_start"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, Button3DStart::new)
                        .dimensions(EntityDimensions.fixed(1.5f, 0.5f))
                        .build());

        ENTITY_BUTTON_CHOICE = Registry.register(Registries.ENTITY_TYPE,
                new Identifier(Simvol.MOD_ID, "button_choice"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, Button3DChoice::new)
                        .dimensions(EntityDimensions.fixed(1.2f, 0.4f))
                        .build());
    }

    // ── Регистрация звуков ────────────────────────────────────

    private static void registerSounds() {
        // Звуки регистрируются через sounds.json в resources/assets/simvol/
        // Здесь только лог
        Simvol.LOGGER.info("СИМВОЛ: Звуки загружены из sounds.json");
    }

    // =========================================================
    //  ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // =========================================================

    /** Регистрирует блок и добавляет в карту для авто-BlockItem */
    private static <T extends Block> T reg(String id, T block) {
        BLOCKS.put(id, block);
        return block;
    }

    /** Регистрирует предмет */
    private static <T extends Item> T regItem(String id, T item) {
        ITEMS.put(id, item);
        return item;
    }
}
