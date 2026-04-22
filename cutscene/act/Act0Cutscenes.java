package com.simvol.mod.cutscene.act;

import com.simvol.mod.cutscene.CutsceneAction;
import com.simvol.mod.cutscene.CutsceneDef;
import com.simvol.mod.cutscene.CutsceneFrame;
import com.simvol.mod.story.StoryFlag;

/**
 * ВСЕ КАТСЦЕНЫ НУЛЕВОГО АКТА — «УВОЛЬНЕНИЕ»
 * ============================================
 *
 * ВАЖНО ПРО КООРДИНАТЫ:
 * Координаты здесь — это координаты ТВОЕЙ КАРТЫ в Minecraft.
 * Когда будешь строить карту — запиши координаты нужных точек
 * и замени заглушки (999, 64, 999 и т.д.) на реальные.
 *
 * КАК НАЙТИ КООРДИНАТЫ:
 * Встань в нужное место → F3 → смотри XYZ и Facing (это твой yaw/pitch)
 *
 * КОНВЕРТАЦИЯ FACING → YAW:
 *   South (+Z) =   0°
 *   West  (-X) =  90°
 *   North (-Z) = 180° (или -180°)
 *   East  (+X) = -90° (или 270°)
 *
 * Pitch в F3 показывается прямо — используй как есть.
 * (положительный = смотришь вниз, отрицательный = вверх)
 */
public class Act0Cutscenes {

    // =========================================================
    //  СЦЕНА 1 — КАМЕРА ПО ЗДАНИЮ (0:00 – 0:45)
    //  «Величественный подъём по фасаду здания Аргус»
    // =========================================================

    /**
     * Камера медленно поднимается снизу вверх по зданию.
     * Затем плавно влетает внутрь и следует за детективом.
     *
     * Длительность: ~45 секунд (900 тиков)
     *
     * КООРДИНАТЫ: Замени на реальные координаты твоего здания Аргус.
     * Сейчас стоят заглушки вида (ARGUS_X, ARGUS_Y, ARGUS_Z).
     *
     * Пример как читать кадры:
     *   CutsceneFrame.of(
     *       ARGUS_X, ARGUS_Y_BOTTOM, ARGUS_Z_FRONT,  // камера у основания здания
     *       0f,    // смотрит на юг (в сторону здания)
     *      -60f,   // смотрит вверх под 60 градусов
     *       200    // 200 тиков = 10 секунд (камера едет вверх)
     *   )
     */
    public static final CutsceneDef BUILDING_EXTERIOR = CutsceneDef.builder("act0_building_exterior")

        // Кадр 1: Камера у земли, смотрит вверх на здание
        // Начало подъёма снизу — классический кинематографический приём
        .frame(CutsceneFrame.of(
            /* X */ 0,   /* Y */ 62,  /* Z */ 15,   // у подножия здания с фронта
            /* yaw */ 180f,                          // смотрит на север (в сторону входа)
            /* pitch */ -60f,                        // сильно вверх
            /* ticks */ 200,                         // 10 секунд плавного подъёма
            // В начале этого кадра — включаем letterbox и запускаем музыку
            CutsceneAction.chain(
                CutsceneAction.letterbox(true),
                CutsceneAction.sound("simvol:music/tension_intro")
            )
        ))

        // Кадр 2: Камера на уровне середины здания
        // Уже видны отражения неба в стёклах
        .frame(CutsceneFrame.of(
            0,  80,  15,
            180f, -30f,
            160    // 8 секунд
        ))

        // Кадр 3: Камера на уровне крыши
        // Финал подъёма — камера чуть запрокидывается вперёд
        .frame(CutsceneFrame.of(
            0,  95,  15,
            180f, 0f,
            80     // 4 секунды
            // Здесь можно добавить звук ветра: CutsceneAction.sound("simvol:sfx/wind")
        ))

        // Кадр 4: Камера "проваливается" внутрь здания через стеклянную стену
        // Плавный переход снаружи → внутрь
        .frame(CutsceneFrame.of(
            0,  78,   8,   // немного ближе к зданию и ниже
            180f, 5f,
            60
        ))

        // Кадр 5: Камера внутри, в вестибюле
        // Видим светлый дорогой офис
        .frame(CutsceneFrame.of(
            0,  66,   0,
            180f, 10f,
            80,
            CutsceneAction.sound("simvol:sfx/office_ambience") // офисный фон
        ))

        // Кадр 6: Камера находит детектива — следует за ним сзади
        // Детектив идёт по коридору, выглядит измотанным
        .frame(CutsceneFrame.of(
            0,  64,  -5,
            180f, 8f,
            120,
            // Запускаем диалог внутреннего монолога (детектив думает про себя)
            // Это просто субтитры без выборов, авто-прокрутка
            CutsceneAction.dialogue("act0_detective_thoughts_1")
        ))

        .build();


    // =========================================================
    //  СЦЕНА 2 — УВОЛЬНЕНИЕ (0:45 – 3:30)
    // =========================================================

    /**
     * Детектив заходит в кабинет, находит папку, идёт к начальнику,
     * бросает папку ему в лицо, получает увольнение.
     *
     * Длительность: ~2:45 (3300 тиков) — долгая, эмоциональная
     */
    public static final CutsceneDef FIRED_SCENE = CutsceneDef.builder("act0_fired")

        // Кадр 1: Крупный план папки на столе детектива
        // Камера медленно приближается к папке
        .frame(CutsceneFrame.of(
            2,  65,  -8,   // у стола детектива
            -90f,          // смотрит на восток (на стол)
            20f,           // слегка вниз (на папку)
            100,
            CutsceneAction.sound("simvol:sfx/office_quiet") // тишина офиса
        ))

        // Кадр 2: Ещё ближе к папке — крупный план названия
        // "Дело №47 — Стеклянный Собиратель"
        .frame(CutsceneFrame.of(
            1.5,  64.5,  -8,
            -90f, 30f,
            60
        ))

        // Кадр 3: Детектив берёт папку, читает название вслух
        // Здесь первая реплика детектива
        .frame(CutsceneFrame.still(
            2,  65,  -8,
            -90f, 15f,
            80,
            CutsceneAction.dialogue("act0_detective_reads_folder")
            // Диалог: "Стеклянный Собиратель… Опять эта хуйня."
        ))

        // Кадр 4: Детектив встаёт, решительно идёт к кабинету начальника
        // Камера следует сзади-сбоку
        .frame(CutsceneFrame.of(
            4,  64,  -6,
            -45f, 8f,
            80
        ))

        // Кадр 5: Детектив открывает дверь начальника (без стука)
        // Резкий звук открываемой двери
        .frame(CutsceneFrame.of(
            8,  64,  -4,
            -20f, 5f,
            40,
            CutsceneAction.sound("simvol:sfx/door_office_open")
        ))

        // Кадр 6: Камера внутри кабинета — показывает начальника за столом
        // Начальник смотрит холодно
        .frame(CutsceneFrame.of(
            10,  65,  0,
            -90f, 5f,
            60
        ))

        // Кадр 7: Детектив БРОСАЕТ папку — резкая анимация
        // Это кульминационный момент — звук удара папкой
        .frame(CutsceneFrame.still(
            9,  65,  0,
            -70f, 10f,
            30,
            CutsceneAction.chain(
                CutsceneAction.sound("simvol:sfx/folder_slam"),
                CutsceneAction.dialogue("act0_detective_slams")
                // Диалог: "Я не буду это вести. Это не дело. Это бред сумасшедшего."
            )
        ))

        // Кадр 8: Крупный план лица начальника
        // Он медленно кладёт папку. Ледяной взгляд.
        .frame(CutsceneFrame.still(
            12,  66,  0,
            90f, 5f,   // смотрим на начальника с другой стороны
            100
            // Здесь диалог начальника идёт как часть диалогового дерева
            // после диалога детектива (chain в предыдущем action)
        ))

        // Кадр 9: Начальник встаёт — доминирующая поза
        // "Ты уволен."
        .frame(CutsceneFrame.still(
            12,  67,  0,
            90f, 0f,
            160,
            CutsceneAction.chain(
                CutsceneAction.setFlag(StoryFlag.ACT0_FIRED),
                CutsceneAction.dialogue("act0_boss_fires")
                // Диалог с ветками и всей речью начальника
            )
        ))

        // Кадр 10: Детектив разворачивается и уходит
        // Молча. Камера остаётся на начальнике — он смотрит вслед.
        .frame(CutsceneFrame.of(
            9,  65,  0,
            -90f, 5f,
            80
        ))

        .build();


    // =========================================================
    //  СЦЕНА 3 — СБОРЫ И УНИЖЕНИЕ (3:30 – 6:00)
    // =========================================================

    /**
     * Детектив собирает вещи в коробку.
     * Все смотрят. Никто ничего не говорит.
     * Тяжёлая, давящая сцена.
     *
     * Длительность: ~2:30 (3000 тиков)
     */
    public static final CutsceneDef PACKING_BOXES = CutsceneDef.builder("act0_packing")

        // Кадр 1: Детектив за своим столом, начинает собирать вещи
        // Крупный план рук (камера низко, смотрит на стол)
        .frame(CutsceneFrame.of(
            2,  65.5,  -8,
            -90f, 35f,  // сильно вниз — видим стол
            120
        ))

        // Кадр 2: Крупный план — старая фотография родителей
        // Детектив берёт её и кладёт в коробку
        .frame(CutsceneFrame.still(
            2.3,  65.3,  -8.5,
            -110f, 40f,
            80,
            CutsceneAction.sound("simvol:sfx/paper_rustle")
        ))

        // Кадр 3: Крупный план — зажигалка
        .frame(CutsceneFrame.still(
            1.8,  65.3,  -8.3,
            -80f, 42f,
            60,
            CutsceneAction.sound("simvol:sfx/small_object_pickup")
        ))

        // Кадр 4: Крупный план — значок детектива
        // Детектив смотрит на него секунду, затем с презрением бросает в коробку
        .frame(CutsceneFrame.still(
            2.1,  65.4,  -8.1,
            -95f, 38f,
            120,
            CutsceneAction.chain(
                CutsceneAction.dialogue("act0_badge_moment"),
                // Маленький внутренний монолог про значок
                CutsceneAction.sound("simvol:sfx/badge_drop")
            )
        ))

        // Кадр 5: Детектив выходит в открытый офис с коробкой
        // Камера отъезжает — видим весь офис и 15 человек которые смотрят
        .frame(CutsceneFrame.of(
            5,  67,  0,
            180f, 10f,  // смотрим на открытый офис издалека
            100,
            CutsceneAction.sound("simvol:music/walk_of_shame_theme")
        ))

        // Кадр 6: Детектив идёт через офис — медленно
        // Камера следует сбоку, видим взгляды сотрудников
        .frame(CutsceneFrame.of(
            8,  66,  3,
            -160f, 8f,
            160
        ))

        // Кадр 7: Камера смотрит прямо на детектива (спереди)
        // Он идёт с опущенной головой
        .frame(CutsceneFrame.of(
            15,  66,  8,
            0f, 8f,   // смотрим ему навстречу
            120
        ))

        // Кадр 8: Детектив выходит через главную дверь
        // Звук тяжёлой двери
        .frame(CutsceneFrame.of(
            18,  66,  12,
            0f, 5f,
            60,
            CutsceneAction.sound("simvol:sfx/main_door_close")
        ))

        // Кадр 9: Камера остаётся внутри — смотрит через стекло на детектива снаружи
        // Он исчезает за углом. Потом играем следующую катсцену.
        .frame(CutsceneFrame.still(
            17,  66,  11,
            0f, 3f,
            100,
            CutsceneAction.chain(
                CutsceneAction.setFlag(StoryFlag.ACT0_FIRED),
                CutsceneAction.fadeToBlack(40)
            )
        ))

        .build();


    // =========================================================
    //  СЦЕНА 4 — БЛУЖДАНИЕ ПО ГОРОДУ (6:00 – 7:30)
    // =========================================================

    /**
     * Детектив идёт по дождливому городу.
     * Долгие, молчаливые кадры.
     */
    public static final CutsceneDef CITY_WALK = CutsceneDef.builder("act0_city_walk")

        // Кадр 1: Дождливая улица — общий план
        // Детектив маленький фигурка вдалеке
        .frame(CutsceneFrame.of(
            0,  64,  50,
            180f, 5f,
            120,
            CutsceneAction.sound("simvol:sfx/rain_city")
        ))

        // Кадр 2: Камера сзади-сбоку от детектива
        // Видим его спину, мокрый асфальт, отражения неона
        .frame(CutsceneFrame.of(
            -3,  64,  30,
            -150f, 8f,
            160
        ))

        // Кадр 3: Детектив останавливается у стены с объявлениями
        // Камера медленно приближается к стене
        .frame(CutsceneFrame.of(
            -1,  65,  20,
            -170f, 5f,
            100
        ))

        // Кадр 4: Крупный план — объявление "Требуется детектив. Ночной Дозор."
        // Детектив долго смотрит на него
        .frame(CutsceneFrame.still(
            -0.5,  65,  19,
            -180f, 2f,
            140,
            CutsceneAction.dialogue("act0_notice_board")
            // Монолог: "Ночной Дозор... Звучит как дешёвая забегаловка."
        ))

        // Кадр 5: Детектив берёт коробку и идёт по адресу из объявления
        .frame(CutsceneFrame.of(
            -5,  64,  15,
            -130f, 8f,
            80,
            CutsceneAction.fadeToBlack(30)
        ))

        .build();


    // =========================================================
    //  СЦЕНА 5 — БЮРО «НОЧНОЙ ДОЗОР» (7:30 – 10:00)
    // =========================================================

    /**
     * Детектив спускается в подвал, встречает Валерию.
     * Первый диалог с выборами.
     */
    public static final CutsceneDef NIGHT_WATCH_ARRIVAL = CutsceneDef.builder("act0_night_watch")

        // Кадр 1: Внешний вид входа в подвал — облупившаяся дверь
        .frame(CutsceneFrame.of(
            20,  60,  0,
            90f, 5f,
            60,
            CutsceneAction.sound("simvol:sfx/basement_door_open")
        ))

        // Кадр 2: Детектив спускается по ступенькам
        // Камера смотрит снизу вверх — детектив силуэтом на фоне неба
        .frame(CutsceneFrame.of(
            20,  58,  2,
            90f, -40f,   // смотрим вверх на спускающегося детектива
            80
        ))

        // Кадр 3: Интерьер подвала — тесно, запах кофе и сырости
        // Камера на уровне глаз, видим Валерию за столом
        .frame(CutsceneFrame.of(
            22,  58,  4,
            90f, 8f,
            100
        ))

        // Кадр 4: Крупный план Валерии — она поднимает взгляд
        // Долгий взгляд, потом говорит
        .frame(CutsceneFrame.still(
            24,  59,  4,
            90f, 5f,
            200,
            CutsceneAction.chain(
                CutsceneAction.setFlag(StoryFlag.ACT0_JOINED_NIGHT_WATCH),
                CutsceneAction.dialogue("act0_valeria_intro")
                // ДИАЛОГ С ВЫБОРАМИ:
                // Валерия: "Значит ты и есть тот самый «лучший детектив Аргуса»..."
                // Выборы:
                //   1. "Что за дело?"
                //   2. "Почему никто не хочет его брать?"
                //   3. "Какая зарплата?"
                //   4. (Молча кивнуть)
            )
        ))

        .build();


    // =========================================================
    //  СЦЕНА 4.2 — ДОМА ЗА НОУТБУКОМ (параллельно с 5)
    // =========================================================

    /**
     * Детектив дома, гуглит стеклофабрику.
     * Вспышка ярости.
     */
    public static final CutsceneDef HOME_LAPTOP = CutsceneDef.builder("act0_home_laptop")

        // Кадр 1: Тёмная квартира — детектив ставит коробку на стол
        .frame(CutsceneFrame.of(
            30,  64,  10,
            -90f, 10f,
            80,
            CutsceneAction.chain(
                CutsceneAction.sound("simvol:sfx/box_put_down"),
                CutsceneAction.sound("simvol:sfx/apartment_rain_outside")
            )
        ))

        // Кадр 2: Детектив садится за ноутбук, открывает крышку
        .frame(CutsceneFrame.of(
            32,  65,  10,
            -90f, 15f,
            80,
            CutsceneAction.sound("simvol:sfx/laptop_open")
        ))

        // Кадр 3: Крупный план экрана ноутбука
        // Детектив гуглит "стеклофабрика красново"
        .frame(CutsceneFrame.still(
            32,  65.5,  9.5,
            -90f, 20f,
            120,
            CutsceneAction.dialogue("act0_laptop_search")
            // Монолог: "Стеклофабрика красново..." (звук печати)
        ))

        // Кадр 4: Экран показывает — "Результатов не найдено"
        // Пауза. Детектив смотрит на экран.
        .frame(CutsceneFrame.still(
            32,  65.5,  9.5,
            -90f, 20f,
            100
        ))

        // Кадр 5: ВСПЫШКА ЯРОСТИ — детектив бьёт кулаком по столу
        // Камера резко дёргается (эффект удара)
        .frame(CutsceneFrame.still(
            32,  65,  10,
            -85f, 12f,    // камера чуть дёрнулась
            60,
            CutsceneAction.chain(
                CutsceneAction.sound("simvol:sfx/fist_on_table"),
                CutsceneAction.dialogue("act0_detective_rage"),
                // "Да что вообще происходит?! Это же не может быть совпадением!"
                CutsceneAction.paranoia(1)
            )
        ))

        // Кадр 6: Детектив медленно закрывает ноутбук
        // Темнота. Только дождь за окном.
        .frame(CutsceneFrame.still(
            32,  65,  10,
            -90f, 8f,
            120,
            CutsceneAction.chain(
                CutsceneAction.sound("simvol:sfx/laptop_close"),
                CutsceneAction.dialogue("act0_detective_parents_thought")
                // "Пойду к родителям… может, они хоть что-то знают."
            )
        ))

        .build();


    // =========================================================
    //  СЦЕНА — ПУТЬ К РОДИТЕЛЯМ + ДОМОФОН
    // =========================================================

    /**
     * Детектив идёт по улице к пятиэтажкам.
     * Домофон. Голос мамы.
     */
    public static final CutsceneDef WALK_TO_PARENTS = CutsceneDef.builder("act0_walk_to_parents")

        // Кадр 1: Вечерняя улица — постсоветские пятиэтажки на горизонте
        // Камера сзади детектива, широкий план
        .frame(CutsceneFrame.of(
            50,  64,  0,
            -180f, 5f,
            120,
            CutsceneAction.sound("simvol:sfx/evening_city_ambience")
        ))

        // Кадр 2: Детектив идёт — пятиэтажки ближе
        // Фонари загораются
        .frame(CutsceneFrame.of(
            50,  64,  -20,
            -180f, 6f,
            100
        ))

        // Кадр 3: Камера темнеет (через 3 секунды)
        // Fade to black — переход к следующей сцене
        .frame(CutsceneFrame.still(
            50,  64,  -30,
            -180f, 5f,
            60,
            CutsceneAction.fadeToBlack(40)
        ))

        .build();

    /**
     * Сцена у домофона.
     * Легендарный звук домофона. Голос мамы с озвучкой.
     */
    public static final CutsceneDef INTERCOM_SCENE = CutsceneDef.builder("act0_intercom")

        // Кадр 1: Детектив у входа в подъезд — вечер
        // Крупный план домофона
        .frame(CutsceneFrame.of(
            60,  64,  0,
            -90f, 5f,
            60,
            CutsceneAction.sound("simvol:sfx/intercom_button_press")
        ))

        // Кадр 2: Пауза — ждём ответа
        .frame(CutsceneFrame.still(
            60,  64,  0,
            -90f, 5f,
            40
        ))

        // Кадр 3: Звук домофона + голос мамы "Кто?"
        // Легендарный советский звук домофона (трещание)
        .frame(CutsceneFrame.still(
            60,  64,  0,
            -90f, 3f,
            60,
            CutsceneAction.chain(
                CutsceneAction.sound("simvol:sfx/intercom_static"),
                CutsceneAction.dialogue("act0_intercom_mama_who")
                // Диалог: Мама (через помехи): "Кто?"
                //         Детектив: "Это я, мам."
                //         Мама: "Сейчас открою!" (щелчок замка)
            )
        ))

        // Кадр 4: Звук открывающегося замка и двери подъезда
        .frame(CutsceneFrame.still(
            60,  64,  0,
            -90f, 3f,
            40,
            CutsceneAction.chain(
                CutsceneAction.sound("simvol:sfx/intercom_buzz"),
                CutsceneAction.sound("simvol:sfx/entrance_door_open")
            )
        ))

        .build();


    // =========================================================
    //  СЦЕНА 5 — КУХНЯ РОДИТЕЛЕЙ (8:00 – 11:30)
    // =========================================================

    /**
     * Разговор с мамой и отцом.
     * Полная свобода вопросов через диалоговые выборы.
     * Отец молчит всю сцену, в конце произносит одну фразу.
     * Твист про завод раскрывается постепенно.
     */
    public static final CutsceneDef PARENTS_KITCHEN = CutsceneDef.builder("act0_parents_kitchen")

        // Кадр 1: Советская кухня — общий план
        // Мама открывает дверь, обнимает детектива
        .frame(CutsceneFrame.of(
            65,  66,  0,
            90f, 8f,
            120,
            CutsceneAction.chain(
                CutsceneAction.sound("simvol:sfx/apartment_door_open"),
                CutsceneAction.sound("simvol:sfx/kitchen_kettle_background")
            )
        ))

        // Кадр 2: Объятие с мамой
        // Тёплый момент в контрасте с предыдущими холодными сценами
        .frame(CutsceneFrame.still(
            66,  66,  1,
            100f, 5f,
            100,
            CutsceneAction.dialogue("act0_mama_greets")
            // Диалог: Мама обнимает, что-то говорит тепло
        ))

        // Кадр 3: Все сидят за кухонным столом, пьют чай
        // Отец сидит в дальнем углу, молча смотрит в окно
        .frame(CutsceneFrame.still(
            68,  67,  2,
            120f, 10f,
            200,
            CutsceneAction.chain(
                CutsceneAction.setFlag(StoryFlag.ACT0_VISITED_PARENTS),
                CutsceneAction.dialogue("act0_parents_main")
                // ГЛАВНЫЙ ДИАЛОГ АКТА — разветвлённый
                // Все возможные вопросы про завод, отца, маму
                // Этот диалог обрабатывается через DialogueEngine
                // с ветками по StoryFlag
            )
        ))

        // Кадр 4 (опциональный): Крупный план отца
        // Только если игрок спросил про завод — отец произносит свою фразу
        // "Не надо тебе туда ходить."
        // Реализуется через условие в DialogueEngine

        // Кадр 5: Детектив уходит
        .frame(CutsceneFrame.of(
            65,  66,  0,
            -90f, 5f,
            80,
            CutsceneAction.sound("simvol:sfx/apartment_door_close")
        ))

        .build();


    // =========================================================
    //  СЦЕНА 6 — РЕШЕНИЕ (11:30 – 12:00)
    // =========================================================

    /**
     * Детектив на лестнице. Его осеняет. Финальная фраза.
     * Надпись "АКТ 1 — СТЕКЛО".
     *
     * Длительность: ~30 секунд (600 тиков)
     */
    public static final CutsceneDef DECISION_MOMENT = CutsceneDef.builder("act0_decision")

        // Кадр 1: Детектив поднимается по лестнице
        .frame(CutsceneFrame.of(
            70,  68,  5,
            -90f, 8f,
            80,
            CutsceneAction.sound("simvol:sfx/stairwell_steps")
        ))

        // Кадр 2: Детектив резко останавливается
        // Камера останавливается тоже — момент озарения
        .frame(CutsceneFrame.still(
            70,  69,  5,
            -90f, 5f,
            60,
            CutsceneAction.sound("simvol:sfx/tension_sting")
        ))

        // Кадр 3: Детектив разворачивается и быстро спускается вниз
        // Камера следует сзади
        .frame(CutsceneFrame.of(
            70,  67,  5,
            90f, 8f,
            60
        ))

        // Кадр 4: Финальная фраза за кадром
        // Экран темнеет
        .frame(CutsceneFrame.still(
            70,  67,  5,
            90f, 5f,
            120,
            CutsceneAction.chain(
                CutsceneAction.dialogue("act0_final_decision"),
                // "Я еду на этот завод завтра."
                CutsceneAction.setFlag(StoryFlag.ACT0_DECIDED_TO_GO),
                CutsceneAction.delayed(80, CutsceneAction.chain(
                    CutsceneAction.fadeToBlack(40),
                    CutsceneAction.delayed(50, CutsceneAction.chain(
                        CutsceneAction.showTitle("АКТ 1"),
                        CutsceneAction.showSubtitle("СТЕКЛО")
                    ))
                ))
            )
        ))

        .build();

    // =========================================================
    //  ОСТАЛЬНЫЕ КАТСЦЕНЫ (заглушки — заполняешь по мере постройки карты)
    // =========================================================

    public static final CutsceneDef WALK_OF_SHAME = CutsceneDef.builder("act0_walk_of_shame")
        .frame(CutsceneFrame.of(0, 64, 0,  0f, 0f, 20))
        .build();

    public static final CutsceneDef RAGE_MOMENT = CutsceneDef.builder("act0_rage")
        .frame(CutsceneFrame.of(0, 64, 0,  0f, 0f, 20))
        .build();
}
