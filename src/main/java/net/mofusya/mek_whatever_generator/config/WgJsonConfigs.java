package net.mofusya.mek_whatever_generator.config;

import com.google.gson.JsonObject;
import net.mofusya.mek_whatever_generator.C;
import net.mofusya.ornatelib.config.JsonConfig;

public class WgJsonConfigs {
    public static final JsonConfig MAIN_CONFIG = new JsonConfig(C.MOD_ID, () -> {
        JsonObject json = new JsonObject();
        json.addProperty(C.ENERGY_SCRAP_MULTIPLIER, 1f);

        return json;
    });
}
