package xyz.fcidd.serverstatus.translate;

public class TranslatableMessage {
    private static final String BASE = "serverstatus";
    public static final TranslatableMessage COMMAND_FAILED = getFullKey(0, KEYS.COMMAND, KEYS.FAILED);
    public static final TranslatableMessage COMMAND_SUCCEEDED = getFullKey(1, KEYS.COMMAND, KEYS.SUCCEEDED);
    public static final TranslatableMessage COMMAND_WRONG_FORMAT = getFullKey(0, KEYS.COMMAND, KEYS.WRONG_FORMAT);
    public static final TranslatableMessage COMMAND_HELP = getFullKey(1, KEYS.COMMAND, KEYS.HELP);
    public static final TranslatableMessage COMMAND_SUCCEEDED_SET_HOST = getFullKey(1, KEYS.COMMAND, KEYS.SUCCEEDED,
            KEYS.SET_HOST);
    public static final TranslatableMessage COMMAND_SUCCEEDED_SET_PORT = getFullKey(1, KEYS.COMMAND, KEYS.SUCCEEDED,
            KEYS.SET_PORT);
    public static final TranslatableMessage COMMAND_SUCCEEDED_DEFAULT_HOST = getFullKey(1, KEYS.COMMAND, KEYS.SUCCEEDED,
            KEYS.DEFAULT_HOST);
    public static final TranslatableMessage COMMAND_SUCCEEDED_DEFAULT_PORT = getFullKey(1, KEYS.COMMAND, KEYS.SUCCEEDED,
            KEYS.DEFAULT_PORT);
    public static final TranslatableMessage COMMAND_SUCCEEDED_RELOAD = getFullKey(1, KEYS.COMMAND, KEYS.SUCCEEDED,
            KEYS.RELOAD);
    public static final TranslatableMessage COMMAND_WRONG_FORMAT_SET_HOST = getFullKey(0, KEYS.COMMAND,
            KEYS.WRONG_FORMAT, KEYS.SET_HOST);
    public static final TranslatableMessage SEND_FAILED = getFullKey(KEYS.SEND, KEYS.FAILED);
    public static final TranslatableMessage SEND_SUCCEEDED = getFullKey(KEYS.SEND, KEYS.SUCCEEDED);
    public static final TranslatableMessage CONNECT_FAILED = getFullKey(KEYS.CONNECT, KEYS.FAILED);

    private KEYS[] keys;
    private int returnNum;

    /**
     * 获取命令返回值，如果不是命令返回-1
     * 
     * @return 命令返回值
     */
    public int getReturnNum() {
        return returnNum;
    }

    /**
     * 获取key字符串
     * 
     * @return key字符串
     */
    public String getStringKey() {
        StringBuilder stringBuilder = new StringBuilder();
        for (KEYS key : keys) {
            stringBuilder.append(key.getKey());
        }
        return BASE + stringBuilder.toString();
    }

    /**
     * 获取可翻译的key
     * 
     * @return key
     */
    private static TranslatableMessage getFullKey(KEYS... keys) {
        return new TranslatableMessage(keys);
    }

    /**
     * 获取可翻译的key
     * 
     * @return key
     */
    private static TranslatableMessage getFullKey(int returnNum, KEYS... keys) {
        return new TranslatableMessage(returnNum, keys);
    }

    private TranslatableMessage(KEYS... keys) {
        this(-1, keys);
    }

    private TranslatableMessage(int returnNum, KEYS... keys) {
        this.keys = keys;
        this.returnNum = returnNum;
    }

    private enum KEYS {

        COMMAND("command"), SEND("send"), CONNECT("connect"),

        FAILED("failed"), SUCCEEDED("succeeded"), WRONG_FORMAT("wrong_format"), HELP("help"),

        SET_HOST("sethost"), SET_PORT("setport"), DEFAULT_HOST("defaulthost"), DEFAULT_PORT("defaultport"),
        RELOAD("reload");

        private String key;

        KEYS(String key) {
            this.key = key;
        }

        /**
         * 获取追加的key
         * 
         * @return 追加的key
         */
        public String getKey() {
            return "." + key;
        }
    }
}
