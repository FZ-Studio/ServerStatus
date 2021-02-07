package xyz.fcidd.serverstatus.translate;

public enum TranslatableKey {
    BASE("serverstatus"), 
    COMMAND(BASE.getKey() + ".command"), 
    FAILED(COMMAND.getKey() + ".failed"),
    SUCCEEDED(COMMAND.getKey() + ".succeeded"), 
    WRONG_FORMAT(COMMAND.getKey() + ".wrongformat"),
    HELP(COMMAND.getKey() + ".help", 1), 
    SET_HOST_SUCCEEDED(SUCCEEDED.getKey() + ".sethost", 1),
    SET_HOST_WRONG_FORMAT(WRONG_FORMAT.getKey() + ".sethost", 0),
    DEFAULT_PORT_SUCCEEDED(SUCCEEDED.getKey() + ".defaultport", 1),
    DEFAULT_HOST_SUCCEEDED(SUCCEEDED.getKey() + ".defaulthost", 1),
    SET_PORT_SUCCEEDED(SUCCEEDED.getKey() + ".setport", 1), 
    RELOAD_SUCCEEDED(SUCCEEDED.getKey() + ".reload", 1),
    SEND_SUCCEEDED(SUCCEEDED.getKey() + ".message", 1), 
    SEND_FAILED(FAILED.getKey() + ".message", 0),
    CONNECT_FAILED(FAILED.getKey() + ".connect", 0);

    private String key;
    private int returnNum = -1;

    TranslatableKey(String key) {
        this.key = key;
    }

    TranslatableKey(String key, int returnNum) {
        this.key = key;
        this.returnNum = returnNum;
    }

    /**
     * 获取可翻译的key
     * 
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * 获取命令返回值
     * 
     * @return 返回值
     */
    public int getReturnNum() {
        return returnNum;
    }
}
