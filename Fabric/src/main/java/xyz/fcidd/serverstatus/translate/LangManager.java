package xyz.fcidd.serverstatus.translate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import xyz.fcidd.serverstatus.ServerStatus;

public class LangManager {
    private static final String PATH = "/assets/serverstatus/lang/";

    private static List<String> langsList;
    private static HashMap<String, String> jsonMap;

    public static void loadLangs() {
        Gson gson = new Gson();
        InputStream in = LangManager.class.getClass()
                .getResourceAsStream(PATH + ServerStatus.getConfig().getLang() + ".json");
        Scanner scanner = new Scanner(in, "UTF-8");
        String json = scanner.useDelimiter("\\A").next();
        scanner.close();
        jsonMap = gson.fromJson(json, new TypeToken<HashMap<String, String>>() {
        }.getType());

        // 从jar中加载语言文件
        langsList = new ArrayList<>();
        String langPath = ServerStatus.getInstance().getClass().getProtectionDomain().getCodeSource().getLocation()
                .getPath();
        if (System.getProperty("os.name").contains("dows")) {
            langPath = langPath.substring(1, langPath.length());
        }
        try {
            langPath = URLDecoder.decode(langPath, "UTF-8");
            try (JarFile langFiles = new JarFile(langPath)) {
                Enumeration<JarEntry> enume = langFiles.entries();
                while (enume.hasMoreElements()) {
                    String langFileString = enume.nextElement().getName();
                    if (langFileString.startsWith(PATH.substring(1)) && langFileString.endsWith(".json")) {
                        // 获取语言id
                        langsList.add(langFileString.substring(25, langFileString.lastIndexOf(".")));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getJsonMap() {
        return jsonMap;
    }

    public static String getTranslated(TranslatableKey key, Object... args) {
        String translated = jsonMap.get(key.getKey());
        return String.format(translated, args);
    }

    public static List<String> getLangsList() {
        return langsList;
    }
}
