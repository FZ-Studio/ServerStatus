package xyz.fcidd.serverstatus.translate;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public enum Langs {
    ZH_CN("/assets/serverstatus/lang/zh_cn.json");

    HashMap<String, String> jsonMap;

    Langs(String path) {
        Gson gson = new Gson();

        InputStream in = this.getClass().getResourceAsStream(path);
        Scanner scanner = new Scanner(in, "UTF-8");
        String json = scanner.useDelimiter("\\A").next();
        scanner.close();
        this.jsonMap = gson.fromJson(json, new TypeToken<HashMap<String, String>>() {
        }.getType());
    }

    public Map<String, String> getJsonMap() {
        return jsonMap;
    }

    public String getTranslated(String key, String... args) {
        String translated = jsonMap.get(key);
        translated = translated.replace("\\%", "§");
        for (String arg : args) {
            translated = translated.replaceFirst("%s", arg);
        }
        translated = translated.replace("§", "%");
        return translated;
    }

    public static Map<String, Langs> getLangsMap() {
        Map<String, Langs> langsMap = new HashMap<>();
        Langs[] langs = Langs.values();
        for (Langs lang : langs) {
            langsMap.put(lang.name().toLowerCase(), lang);
        }
        return langsMap;
    }
}
