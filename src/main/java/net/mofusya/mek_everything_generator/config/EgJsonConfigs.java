package net.mofusya.mek_everything_generator.config;

import com.google.gson.JsonObject;
import net.mofusya.mek_everything_generator.C;
import net.mofusya.ornatelib.config.JsonConfig;

public class EgJsonConfigs {
    public static final JsonConfig MAIN_CONFIG = new JsonConfig(C.MOD_ID, () -> {
        JsonObject json = new JsonObject();
        json.addProperty(C.ENERGY_SCRAP_MULTIPLIER, 1f);

        return json;
    });
}
