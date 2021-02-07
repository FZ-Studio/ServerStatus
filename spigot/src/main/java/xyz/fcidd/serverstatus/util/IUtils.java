package xyz.fcidd.serverstatus.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IUtils {

    /**
     * 字符串是否为纯数字(int)
     * 
     * @param str 被检测的字符串
     * @return 是true否false
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 数字(int)是否为端口号(0-65535)
     * 
     * @param num 被检测的数字
     * @return 是true否false
     */
    public static boolean isPort(int num) {
        return num >= 0 && num <= 65535;
    }

    /**
     * 字符串是否为端口号(0-65535)
     * 
     * @param str 被检测的字符串
     * @return 是true否false
     */
    public static boolean isPort(String str) {
        if (!isNumeric(str)) {
            return false;
        }
        return isPort(Integer.parseInt(str));
    }

    /**
     * 字符串是否为ip地址
     * 
     * @param str 被检测的字符串
     * @return 是true否false
     */
    public static boolean isHost(String str) {
        if (str.equals("localhost")) {
            return true;
        }
        String[] strs = str.split("\\.");
        if (strs.length != 4) {
            return false;
        }
        for (String string : strs) {
            if (!isNumeric(string)) {
                int num = Integer.parseInt(string);
                if (num < 0 || num > 255) {
                    return false;
                }
            }
        }
        return true;
    }
}
