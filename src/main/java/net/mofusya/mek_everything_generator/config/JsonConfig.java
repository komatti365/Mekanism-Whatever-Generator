package net.mofusya.mek_everything_generator.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;

public class JsonConfig {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final String name;
    private final Supplier<JsonObject> defaultSupplier;
    private JsonObject config;

    public JsonConfig(String name, Supplier<JsonObject> defaultSupplier) {
        this.name = name;
        this.defaultSupplier = defaultSupplier;
    }

    public void load() {
        Path configPath = FMLPaths.CONFIGDIR.get().resolve(this.name + ".json");
        JsonObject defaults = this.defaultSupplier.get();

        if (!Files.exists(configPath)) {
            this.config = defaults;
            this.save(configPath, defaults);
            return;
        }

        try (Reader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
            JsonElement parsed = JsonParser.parseReader(reader);
            if (parsed != null && parsed.isJsonObject()) {
                JsonObject loaded = parsed.getAsJsonObject();
                boolean modified = false;
                for (Map.Entry<String, JsonElement> entry : defaults.entrySet()) {
                    if (!loaded.has(entry.getKey())) {
                        loaded.add(entry.getKey(), entry.getValue());
                        modified = true;
                    }
                }
                this.config = loaded;
                if (modified) {
                    this.save(configPath, loaded);
                }
                return;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load config file {}. Using defaults.", configPath, e);
        }

        this.config = defaults;
    }

    public JsonObject get() {
        if (this.config == null) {
            this.load();
        }
        return this.config;
    }

    private void save(Path configPath, JsonObject json) {
        try {
            Files.createDirectories(configPath.getParent());
            try (Writer writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)) {
                GSON.toJson(json, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save config file {}.", configPath, e);
        }
    }
}
