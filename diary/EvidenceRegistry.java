package com.simvol.mod.diary;

import java.util.List;

public class EvidenceRegistry {

    // =========================================================
    //  АКТ 0 — УВОЛЬНЕНИЕ
    // =========================================================

    public static final Evidence CASE_FOLDER = new Evidence(
        "case_folder", 0,
        "Дело №47 — Стеклянный Собиратель",
        "Папка из агентства Аргус. " +
        "Детектив отказался вести это дело. " +
        "Именно из-за неё его уволили.",
        "textures/gui/evidence/case_folder.png"
    );

    public static final Evidence SEARCH_NO_RESULTS = new Evidence(
        "search_no_results", 0,
        "Поиск без результатов",
        "«Стеклофабрика Красново» — результатов не найдено. " +
        "Завод существовал, но в интернете его нет. " +
        "Намеренно.",
        "textures/gui/evidence/laptop_screen.png"
    );

    public static final Evidence NIGHT_WATCH_AD = new Evidence(
        "night_watch_ad", 0,
        "Объявление Ночного Дозора",
        "«Требуется детектив. Бюро Ночной Дозор. " +
        "Большая зарплата.» " +
        "Объявление на мокрой стене. " +
        "Это не случайность.",
        "textures/gui/evidence/notice_board.png"
    );

    public static final Evidence FATHER_WARNING = new Evidence(
        "father_warning", 0,
        "Предупреждение отца",
        "Отец сказал только одну фразу: " +
        "«Не надо тебе туда ходить.» " +
        "И ушёл в другую комнату. " +
        "Он знает что-то.",
        "textures/gui/evidence/father_silhouette.png"
    );

    public static final Evidence MOTHER_TESTIMONY = new Evidence(
        "mother_testimony", 0,
        "Слова мамы",
        "Завод закрылся в 1979 году. " +
        "Отец там работал. " +
        "После закрытия сильно изменился. " +
        "Стал другим.",
        "textures/gui/evidence/tea_cup.png"
    );

    // =========================================================
    //  АКТ 1 — ЗАВОД
    // =========================================================

    public static final Evidence NEW_LOCK = new Evidence(
        "new_lock", 1,
        "Новый замок",
        "На воротах завода, закрытого 45 лет назад — " +
        "новый современный замок. " +
        "Кто-то сюда приходит.",
        "textures/gui/evidence/new_lock.png"
    );

    public static final Evidence MEETING_NOTICE = new Evidence(
        "meeting_notice", 1,
        "Объявление о собрании",
        "Внеплановое собрание. 15 марта 1979. " +
        "Всем рабочим явка обязательна. " +
        "Это дата закрытия завода.",
        "textures/gui/evidence/meeting_notice.png"
    );

    public static final Evidence THREE_MISSING = new Evidence(
        "three_missing", 1,
        "Трое не вышли",
        "15 марта 1979. Приход — 18 человек. " +
        "Уход — 15. " +
        "Трое не расписались на выход.",
        "textures/gui/evidence/journal_page.png"
    );

    public static final Evidence GUARD_SEMYONYCH = new Evidence(
        "guard_semyonych", 1,
        "Охранник Семёныч",
        "Охранник завода. 31 год службы. " +
        "Видел 15 марта. " +
        "Считал всех на входе и выходе.",
        "textures/gui/evidence/guard_photo.png"
    );

    public static final Evidence SAFETY_POSTER = new Evidence(
        "safety_poster", 1,
        "Плакат 1976 года",
        "«Стеклозавод №7». " +
        "Значит существовали заводы №1-6. " +
        "Где они?",
        "textures/gui/evidence/poster.png"
    );

    public static final Evidence FORKLIFT_KEY = new Evidence(
        "forklift_key", 1,
        "Ключ в погрузчике",
        "Советский погрузчик. " +
        "Ключ в замке зажигания 45 лет. " +
        "Завод закрылся внезапно. " +
        "Никто не успел забрать вещи.",
        "textures/gui/evidence/key.png"
    );

    public static final Evidence WIPED_LEVER = new Evidence(
        "wiped_lever", 1,
        "Рычаг со стёртой пылью",
        "Пыль стёрта с рычага. " +
        "Следы свежие. " +
        "Кто-то недавно трогал этот механизм.",
        "textures/gui/evidence/lever.png"
    );

    public static final Evidence FRESH_TRACKS = new Evidence(
        "fresh_tracks", 1,
        "Свежие следы",
        "Следы через весь цех. " +
        "Уходят в коридор к подвалу. " +
        "Кто-то здесь регулярно бывает.",
        "textures/gui/evidence/footprints.png"
    );

    public static final Evidence GLASS_FORM = new Evidence(
        "glass_form", 1,
        "Форма для литья",
        "180 см в длину, 50 см в ширину. " +
        "Это форма для человека. " +
        "Не для стекла.",
        "textures/gui/evidence/glass_form.png"
    );

    public static final Evidence COMPOSITION_K7 = new Evidence(
        "composition_k7", 1,
        "Состав К-7",
        "Специальный состав К-7. " +
        "Получатель — Лаборатория. " +
        "На стекольном заводе не должно быть лаборатории.",
        "textures/gui/evidence/document_k7.png"
    );

    public static final Evidence NEW_TORCH = new Evidence(
        "new_torch", 1,
        "Новый фонарик",
        "Совсем новый. Батарейки свежие. " +
        "Оставлен на заводе, закрытом 45 лет назад. " +
        "Кем и зачем?",
        "textures/gui/evidence/torch.png"
    );

    public static final Evidence DIRECTOR_GROMOV = new Evidence(
        "director_gromov", 1,
        "Директор Громов",
        "Громов Виктор Степанович. " +
        "Директор завода с 1961 по 1979 год.",
        "textures/gui/evidence/portrait_gromov.png"
    );

    public static final Evidence OPEN_SAFE = new Evidence(
        "open_safe", 1,
        "Открытый сейф",
        "Сейф открыт. Пустой. " +
        "Код не сброшен — 1979. " +
        "Год закрытия завода.",
        "textures/gui/evidence/safe.png"
    );

    public static final Evidence DIRECTOR_NOTEBOOK = new Evidence(
        "director_notebook", 1,
        "Записная книжка директора",
        "14 марта 1979 — " +
        "«Получено разрешение. Процедура начнётся завтра.» " +
        "15 марта — страница пустая.",
        "textures/gui/evidence/notebook.png"
    );

    public static final Evidence TECH_SCHEME = new Evidence(
        "tech_scheme", 1,
        "Технологическая схема",
        "Схема производства из цеха №2. " +
        "Снова упоминается состав К-7. " +
        "Это не стандартное стекольное производство.",
        "textures/gui/evidence/scheme.png"
    );

    public static final Evidence ALIEVA_ROBE = new Evidence(
        "alieva_robe", 1,
        "Роба Алиева",
        "На спине написано карандашом: " +
        "«Если найдёшь — иди в подвал. Мы там.»",
        "textures/gui/evidence/robe.png"
    );

    public static final Evidence THREE_MATTRESSES = new Evidence(
        "three_mattresses", 1,
        "Три матраса",
        "Трое людей жили здесь долго. " +
        "На стенах царапины — считали дни.",
        "textures/gui/evidence/mattress.png"
    );

    public static final Evidence CANNED_FOOD = new Evidence(
        "canned_food", 1,
        "Консервы марта 1979",
        "23 банки советских консервов. " +
        "Трое людей провели в подвале около двух недель.",
        "textures/gui/evidence/cans.png"
    );

    public static final Evidence BASEMENT_DIARY = new Evidence(
        "basement_diary", 1,
        "Дневник из подвала",
        "Трое рабочих прятались после 15 марта 1979. " +
        "Они видели комнату. " +
        "Запись обрывается.",
        "textures/gui/evidence/diary.png"
    );

    public static final Evidence GLASS_TOMBS = new Evidence(
        "glass_tombs", 1,
        "Тела в стекле",
        "Три человека, залитые составом К-7. " +
        "Трое рабочих из журнала. " +
        "На стене: «МЫ ВИДЕЛИ ДЕРЕВНЮ».",
        "textures/gui/evidence/glass_tombs.png"
    );

    // =========================================================
    //  АКТ 2 — ДЕРЕВНЯ
    // =========================================================

    public static final Evidence SYMBOL_NINA = new Evidence(
        "symbol_nina", 2,
        "Символ у Нины",
        "Прищуренный глаз на донышке кружки. " +
        "Нина не знает. Или притворяется.",
        "textures/gui/evidence/symbol.png"
    );

    public static final Evidence SYMBOL_RASHID = new Evidence(
        "symbol_rashid", 2,
        "Символ у Рашида",
        "За батареей в прихожей. " +
        "В доме сына убитого рабочего.",
        "textures/gui/evidence/symbol.png"
    );

    public static final Evidence SYMBOL_TOLYA = new Evidence(
        "symbol_tolya", 2,
        "Символ у Толи",
        "Под ковром в прихожей. " +
        "Толя сам искал — но не там.",
        "textures/gui/evidence/symbol.png"
    );

    public static final Evidence SYMBOL_EMPTY_HOUSE = new Evidence(
        "symbol_empty_house", 2,
        "Символ в пустом доме",
        "На обратной стороне иконы. " +
        "Дата — 1981. " +
        "Маркировка продолжилась в деревне после завода.",
        "textures/gui/evidence/symbol.png"
    );

    public static final Evidence SYMBOL_SEMYONYCH = new Evidence(
        "symbol_semyonych", 2,
        "Символ у Семёныча",
        "За большой иконой на стене. " +
        "Семёныч видел всё. И молчал.",
        "textures/gui/evidence/symbol.png"
    );

    public static final Evidence EMPTY_HOUSE_DIARY = new Evidence(
        "empty_house_diary", 2,
        "Дневник из пустого дома",
        "Вырванные страницы. Осталась одна запись: " +
        "«Я начинаю замечать знаки... " +
        "Завтра спрошу у Виктора.» " +
        "Человек исчез после этой записи.",
        "textures/gui/evidence/diary_torn.png"
    );

    public static final Evidence CHILDS_DRAWING = new Evidence(
        "childs_drawing", 2,
        "Детский рисунок",
        "Ребёнок нарисовал деревню " +
        "и символ над каждым домом. " +
        "Для него это было обычным.",
        "textures/gui/evidence/drawing.png"
    );

    public static final Evidence TOLYA_TESTIMONY = new Evidence(
        "tolya_testimony", 2,
        "Показания Толи",
        "Восьмой отдел. Контроль качества. " +
        "Они маркировали вещи в каждом доме. " +
        "Каждую семью. 40 лет. " +
        "Толя последний выживший из пятерых.",
        "textures/gui/evidence/testimony_tolya.png"
    );

    public static final Evidence RASHID_TESTIMONY = new Evidence(
        "rashid_testimony", 2,
        "Показания Рашида",
        "Алиев-старший исчез 15 марта 1979. " +
        "Сказали — несчастный случай. " +
        "Тела не показали. " +
        "Рашид ждал правды 45 лет.",
        "textures/gui/evidence/testimony_rashid.png"
    );

    public static final Evidence SEMYONYCH_TESTIMONY = new Evidence(
        "semyonych_testimony", 2,
        "Показания Семёныча",
        "18 вошли. 15 вышли. " +
        "Он видел. Ему сказали — специальный выход. " +
        "Он поверил. Или сделал вид.",
        "textures/gui/evidence/testimony.png"
    );

    public static final Evidence GROMOV_TATTOO = new Evidence(
        "gromov_tattoo", 2,
        "Татуировка Громова",
        "Символ прищуренного глаза на запястье. " +
        "Громов сам поставил себе метку. " +
        "Чтобы не забывать: он тоже под наблюдением.",
        "textures/gui/evidence/gromov_tattoo.png"
    );

    public static final Evidence GROMOV_CONFESSION = new Evidence(
        "gromov_confession", 2,
        "Признание Громова",
        "Завод был прикрытием. " +
        "Деревня — эксперимент по наблюдению. " +
        "Самое страшное: людям это безразлично.",
        "textures/gui/evidence/gromov_confession.png"
    );

    public static final Evidence ALL_SYMBOLS_FOUND = new Evidence(
        "all_symbols_found", 2,
        "Все символы найдены",
        "Пять символов в пяти домах. " +
        "Систематически. Намеренно. " +
        "Это не случайность — это система.",
        "textures/gui/evidence/all_symbols.png"
    );

    // =========================================================
    //  ПОЛНЫЙ СПИСОК
    // =========================================================

    public static final List<Evidence> ALL = List.of(
        // Акт 0
        CASE_FOLDER,
        SEARCH_NO_RESULTS,
        NIGHT_WATCH_AD,
        FATHER_WARNING,
        MOTHER_TESTIMONY,

        // Акт 1
        NEW_LOCK,
        MEETING_NOTICE,
        THREE_MISSING,
        GUARD_SEMYONYCH,
        SAFETY_POSTER,
        FORKLIFT_KEY,
        WIPED_LEVER,
        FRESH_TRACKS,
        GLASS_FORM,
        COMPOSITION_K7,
        NEW_TORCH,
        DIRECTOR_GROMOV,
        OPEN_SAFE,
        DIRECTOR_NOTEBOOK,
        TECH_SCHEME,
        ALIEVA_ROBE,
        THREE_MATTRESSES,
        CANNED_FOOD,
        BASEMENT_DIARY,
        GLASS_TOMBS,

        // Акт 2
        SYMBOL_NINA,
        SYMBOL_RASHID,
        SYMBOL_TOLYA,
        SYMBOL_EMPTY_HOUSE,
        SYMBOL_SEMYONYCH,
        EMPTY_HOUSE_DIARY,
        CHILDS_DRAWING,
        TOLYA_TESTIMONY,
        RASHID_TESTIMONY,
        SEMYONYCH_TESTIMONY,
        GROMOV_TATTOO,
        GROMOV_CONFESSION,
        ALL_SYMBOLS_FOUND
    );

    // =========================================================
    //  УТИЛИТЫ
    // =========================================================

    /** Все улики конкретного акта */
    public static List<Evidence> forAct(int act) {
        return ALL.stream()
            .filter(e -> e.act() == act)
            .toList();
    }

    /** Найти улику по ID */
    public static Evidence byId(String id) {
        return ALL.stream()
            .filter(e -> e.id().equals(id))
            .findFirst()
            .orElse(null);
    }

    /** Сколько улик в каждом акте */
    public static int countForAct(int act) {
        return (int) ALL.stream()
            .filter(e -> e.act() == act)
            .count();
    }
}
