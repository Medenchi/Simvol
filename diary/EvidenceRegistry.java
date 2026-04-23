package com.simvol.mod.diary;

import java.util.List;

/**
 * ВСЕ УЛИКИ МОДА "СИМВОЛ"
 * =========================
 * Каждая улика — статический объект Evidence.
 * Добавляется в DiarySystem через addEvidence().
 */
public class EvidenceRegistry {

    // =========================================================
    //  АКТ 1 — ЗАВОД
    // =========================================================

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
        "Уход — 15. Трое не расписались на выход.",
        "textures/gui/evidence/journal_page.png"
    );

    public static final Evidence GUARD_SEMYONYCH = new Evidence(
        "guard_semyonych", 1,
        "Охранник Семёныч",
        "Охранник завода. 31 год службы. " +
        "Видел 15 марта. Считал всех на входе и выходе.",
        "textures/gui/evidence/guard_photo.png"
    );

    public static final Evidence GLASS_FORM = new Evidence(
        "glass_form", 1,
        "Форма для литья",
        "Форма 1.8 м в длину, 0.5 м в ширину. " +
        "Это форма для человека. Не для стекла.",
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

    public static final Evidence DIRECTOR_GROMOV = new Evidence(
        "director_gromov", 1,
        "Директор Громов",
        "Громов Виктор Степанович. " +
        "Директор завода с 1961 по 1979 год.",
        "textures/gui/evidence/portrait_gromov.png"
    );

    public static final Evidence DIRECTOR_NOTEBOOK = new Evidence(
        "director_notebook", 1,
        "Записная книжка директора",
        "14 марта 1979 — «Получено разрешение. " +
        "Процедура начнётся завтра.» " +
        "15 марта — страница пустая.",
        "textures/gui/evidence/notebook.png"
    );

    public static final Evidence ALIEVA_ROBE = new Evidence(
        "alieva_robe", 1,
        "Роба Алиева",
        "На спине написано карандашом: " +
        "«Если найдёшь — иди в подвал. Мы там.»",
        "textures/gui/evidence/robe.png"
    );

    public static final Evidence BASEMENT_DIARY = new Evidence(
        "basement_diary", 1,
        "Дневник из подвала",
        "Трое рабочих прятались в подвале " +
        "после 15 марта 1979 года. " +
        "Они видели комнату. " +
        "Запись обрывается.",
        "textures/gui/evidence/diary.png"
    );

    public static final Evidence GLASS_TOMBS = new Evidence(
        "glass_tombs", 1,
        "Тела в стекле",
        "Три человека, залитые составом К-7. " +
        "Это трое рабочих из журнала. " +
        "На стене выцарапано: «МЫ ВИДЕЛИ ДЕРЕВНЮ».",
        "textures/gui/evidence/glass_tombs.png"
    );

    // =========================================================
    //  АКТ 2 — ДЕРЕВНЯ
    // =========================================================

    public static final Evidence SYMBOL_NINA = new Evidence(
        "symbol_nina", 2,
        "Символ у Нины",
        "Прищуренный глаз на донышке кружки. " +
        "Нина не знает или притворяется.",
        "textures/gui/evidence/symbol.png"
    );

    public static final Evidence SYMBOL_RASHID = new Evidence(
        "symbol_rashid", 2,
        "Символ у Рашида",
        "Символ за батареей в прихожей. " +
        "В доме сына убитого рабочего.",
        "textures/gui/evidence/symbol.png"
    );

    public static final Evidence SYMBOL_TOLYA = new Evidence(
        "symbol_tolya", 2,
        "Символ у Толи",
        "Под ковром в прихожей. " +
        "Толя сам искал, но не там.",
        "textures/gui/evidence/symbol.png"
    );

    public static final Evidence SYMBOL_EMPTY_HOUSE = new Evidence(
        "symbol_empty_house", 2,
        "Символ в пустом доме",
        "На обратной стороне иконы. " +
        "Дата — 1981. Маркировка продолжилась в деревне.",
        "textures/gui/evidence/symbol.png"
    );

    public static final Evidence SYMBOL_SEMYONYCH = new Evidence(
        "symbol_semyonych", 2,
        "Символ у Семёныча",
        "За большой иконой. " +
        "Семёныч видел всё. И молчал.",
        "textures/gui/evidence/symbol.png"
    );

    public static final Evidence CHILDS_DRAWING = new Evidence(
        "childs_drawing", 2,
        "Детский рисунок",
        "Ребёнок нарисовал деревню " +
        "и символ над каждым домом. " +
        "Для него это было обычным.",
        "textures/gui/evidence/drawing.png"
    );

    public static final Evidence SEMYONYCH_TESTIMONY = new Evidence(
        "semyonych_testimony", 2,
        "Показания Семёныча",
        "Восемнадцать вошли. Пятнадцать вышли. " +
        "Он видел. Ему сказали — специальный выход. " +
        "Он поверил. Или сделал вид.",
        "textures/gui/evidence/testimony.png"
    );

    public static final Evidence ALL_SYMBOLS_FOUND = new Evidence(
        "all_symbols_found", 2,
        "Все символы найдены",
        "Пять символов в пяти домах. " +
        "Систематически. Намеренно. " +
        "Это не случайность.",
        "textures/gui/evidence/all_symbols.png"
    );

    // =========================================================
    //  ПОЛНЫЙ СПИСОК (для подсчёта прогресса)
    // =========================================================

    public static final List<Evidence> ALL = List.of(
        MEETING_NOTICE, THREE_MISSING, GUARD_SEMYONYCH,
        GLASS_FORM, COMPOSITION_K7, DIRECTOR_GROMOV,
        DIRECTOR_NOTEBOOK, ALIEVA_ROBE, BASEMENT_DIARY,
        GLASS_TOMBS,
        SYMBOL_NINA, SYMBOL_RASHID, SYMBOL_TOLYA,
        SYMBOL_EMPTY_HOUSE, SYMBOL_SEMYONYCH,
        CHILDS_DRAWING, SEMYONYCH_TESTIMONY, ALL_SYMBOLS_FOUND
    );
}
