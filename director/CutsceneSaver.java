package com.simvol.mod.director;

import com.google.gson.*;
import com.simvol.mod.Simvol;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * СОХРАНЕНИЕ И ЗАГРУЗКА КАТСЦЕН
 * ================================
 * Катсцены сохраняются в JSON файлы в папке:
 *   .minecraft/simvol_cutscenes/<id>.json
 *
 * Формат файла:
 * {
 *   "id": "act0_fired",
 *   "frames": [
 *     {
 *       "x": 100.5, "y": 64.0, "z": -45.2,
 *       "yaw": 180.0, "pitch": 10.0,
 *       "duration": 60,
 *       "action_id": "dialogue",
 *       "action_arg": "act0_fired_dialogue"
 *     },
 *     ...
 *   ]
 * }
 */
public class CutsceneSaver {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /** Папка для сохранения катсцен */
    private static Path getSaveDir() {
        return FabricLoader.getInstance()
                .getGameDir()
                .resolve("simvol_cutscenes");
    }

    // =========================================================
    //  СОХРАНЕНИЕ
    // =========================================================

    /**
     * Сохранить катсцену в JSON файл.
     * Файл: .minecraft/simvol_cutscenes/<id>.json
     */
    public static void save(String id, List<RecordedFrame> frames) {
        try {
            Path dir = getSaveDir();
            Files.createDirectories(dir);

            Path file = dir.resolve(id + ".json");

            // Строим JSON
            JsonObject root = new JsonObject();
            root.addProperty("id", id);

            JsonArray framesArr = new JsonArray();
            for (RecordedFrame frame : frames) {
                JsonObject fObj = new JsonObject();
                fObj.addProperty("x",          frame.x);
                fObj.addProperty("y",          frame.y);
                fObj.addProperty("z",          frame.z);
                fObj.addProperty("yaw",        frame.yaw);
                fObj.addProperty("pitch",      frame.pitch);
                fObj.addProperty("duration",   frame.getDurationTicks());
                fObj.addProperty("action_id",  frame.getActionId());
                fObj.addProperty("action_arg", frame.getActionArg());
                framesArr.add(fObj);
            }
            root.add("frames", framesArr);

            // Записываем
            try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }

            Simvol.LOGGER.info("РЕЖИССЁР: Сохранено → " + file.toAbsolutePath());

        } catch (IOException e) {
            Simvol.LOGGER.error("РЕЖИССЁР: Ошибка сохранения катсцены '" + id + "': " + e.getMessage());
        }
    }

    // =========================================================
    //  ЗАГРУЗКА
    // =========================================================

    /**
     * Загрузить все катсцены из папки simvol_cutscenes.
     * @return map: id → список кадров
     */
    public static Map<String, List<RecordedFrame>> loadAll() {
        Map<String, List<RecordedFrame>> result = new LinkedHashMap<>();

        Path dir = getSaveDir();
        if (!Files.exists(dir)) return result;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.json")) {
            for (Path file : stream) {
                try {
                    String id = file.getFileName().toString().replace(".json", "");
                    List<RecordedFrame> frames = loadFile(file);
                    if (frames != null && !frames.isEmpty()) {
                        result.put(id, frames);
                        Simvol.LOGGER.info("РЕЖИССЁР: Загружена катсцена '" + id +
                            "' (" + frames.size() + " кадров)");
                    }
                } catch (Exception e) {
                    Simvol.LOGGER.error("РЕЖИССЁР: Ошибка загрузки файла " +
                        file.getFileName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            Simvol.LOGGER.error("РЕЖИССЁР: Ошибка чтения папки катсцен: " + e.getMessage());
        }

        return result;
    }

    /**
     * Загрузить одну катсцену из файла.
     */
    private static List<RecordedFrame> loadFile(Path file) throws IOException {
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null) return null;

            JsonArray framesArr = root.getAsJsonArray("frames");
            if (framesArr == null) return null;

            List<RecordedFrame> frames = new ArrayList<>();
            for (JsonElement el : framesArr) {
                JsonObject fObj = el.getAsJsonObject();

                double x         = fObj.get("x").getAsDouble();
                double y         = fObj.get("y").getAsDouble();
                double z         = fObj.get("z").getAsDouble();
                float  yaw       = fObj.get("yaw").getAsFloat();
                float  pitch     = fObj.get("pitch").getAsFloat();
                int    duration  = fObj.get("duration").getAsInt();
                String actionId  = fObj.has("action_id")  ? fObj.get("action_id").getAsString()  : "";
                String actionArg = fObj.has("action_arg") ? fObj.get("action_arg").getAsString() : "";

                frames.add(new RecordedFrame(x, y, z, yaw, pitch, duration, actionId, actionArg));
            }

            return frames;
        }
    }

    /**
     * Удалить файл катсцены.
     */
    public static boolean delete(String id) {
        Path file = getSaveDir().resolve(id + ".json");
        try {
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            Simvol.LOGGER.error("РЕЖИССЁР: Ошибка удаления катсцены '" + id + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Список всех сохранённых ID катсцен.
     */
    public static List<String> listSaved() {
        List<String> ids = new ArrayList<>();
        Path dir = getSaveDir();
        if (!Files.exists(dir)) return ids;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.json")) {
            for (Path file : stream) {
                ids.add(file.getFileName().toString().replace(".json", ""));
            }
        } catch (IOException e) {
            Simvol.LOGGER.error("РЕЖИССЁР: Ошибка списка катсцен: " + e.getMessage());
        }

        Collections.sort(ids);
        return ids;
    }
}
