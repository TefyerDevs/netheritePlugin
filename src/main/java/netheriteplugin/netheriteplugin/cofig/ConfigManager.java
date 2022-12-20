package netheriteplugin.netheriteplugin.cofig;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import netheriteplugin.netheriteplugin.dynamic.*;
import netheriteplugin.netheriteplugin.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigManager {
    private static Map<String, DynamicTypeValue> config = new LinkedHashMap();
    public static final String NATURAL_CAUSE_HEART_LOSS = "NaturalCauseHeartLoss";
    public static final String HEART_GAIN_RATE = "HeartGainRate";
    public static final String HEART_LOST_RATE = "HeartLostRate";
    public static final String HAS_HEART_LIMIT = "HasHeartLimit";
    public static final String HEART_LIMIT = "HeartLimit";
    public static final String REVIVAL_HEARTS = "RevivalHearts";
    public static final String DEFAULT_PERMISSIONS = "DefaultPermissions";
    public static final String BROADCAST_EVENTS = "BroadcastEvents";
    public static final String COPY_OLD_HEALTH = "CopyOldHealth";

    public ConfigManager() {
    }

    public static void initConfig() {
        config.put("NaturalCauseHeartLoss", new BooleanTypeValue(false));
        config.put("HeartGainRate", new DoubleTypeValue(2.0));
        config.put("HeartLostRate", new DoubleTypeValue(2.0));
        config.put("HasHeartLimit", new BooleanTypeValue(true));
        config.put("HeartLimit", new DoubleTypeValue(100.0));
        config.put("RevivalHearts", new DoubleTypeValue(10.0));
        config.put("DefaultPermissions", new BooleanTypeValue(true));
        config.put("BroadcastEvents", new BooleanTypeValue(true));
        config.put("CopyOldHealth", new BooleanTypeValue(true));
    }

    public static Map<String, DynamicTypeValue> getConfig() {
        return config;
    }

    public static void save() throws IOException {
        Gson gson = new Gson();
        File file = Utils.getDataFile("config.json");
        file.getParentFile().mkdir();
        file.getParentFile().getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);
        gson.toJson(config, writer);
        writer.flush();
        writer.close();
        Utils.log("Saved Config Data");
    }

    public static void load() throws IOException {
        File file = Utils.getDataFile("config.json");
        if (!file.exists()) {
            initConfig();
        } else {
            String fileContent = Files.readString(file.toPath());
            JsonObject jsonObject = JsonParser.parseString(fileContent).getAsJsonObject();
            Iterator var3 = jsonObject.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry<String, JsonElement> set = (Map.Entry)var3.next();
                String key = (String)set.getKey();
                String value = "";
                JsonObject valueObject = ((JsonElement)set.getValue()).getAsJsonObject();

                Map.Entry s;
                for(Iterator var8 = valueObject.entrySet().iterator(); var8.hasNext(); value = ((JsonElement)s.getValue()).toString()) {
                    s = (Map.Entry)var8.next();
                }

                config.put(key, Utils.stringToDynamicValue(value));
            }

        }
    }

    public static Object getField(String fieldName) {
        DynamicTypeValue field = getConfig().getOrDefault((Object) fieldName, (DynamicTypeValue)null);
        return field != null ? field.getValue() : null;
    }
}
