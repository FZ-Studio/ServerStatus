package xyz.fcidd.serverstatus.translate;

public enum TranslatableKey {
    COMMAND_FAILED_DEFAULT("serverstatus.command.failed"),
    COMMAND_SUCCEED_DEFAULT("serverstatus.command.succeeded"),
    COMMAND_WRONG_FORMAT_DEFAULT("serverstatus.command.wrongformat"),
    COMMAND_HELP("serverstatus.command.help"),
    COMMAND_SETLANG_SUCCEEDED("serverstatus.command.succeeded.setlang"),
    COMMAND_DEFAULT_LANG_SUCCEEDED("serverstatus.command.succeeded.defaultlang"),
    COMMAND_SETHOST_SUCCEEDED("serverstatus.command.succeeded.sethost"),
    COMMAND_DEFAULT_HOST_SUCCEEDED("serverstatus.command.succeeded.defaulthost"),
    COMMAND_SETPORT_SUCCEEDED("serverstatus.command.succeeded.setport"),
    COMMAND_DEFAULT_PORT_SUCCEEDED("serverstatus.command.succeeded.defaultport"),
    COMMAND_RELOAD_SUCCEEDED("serverstatus.command.succeeded.reload"),
    COMMAND_SETLANG_WRONG_FORMAT("serverstatus.command.wrongformat.setlang"),
    COMMAND_SETHOST_WRONG_FORMAT("serverstatus.command.wrongformat.sethost"),
    SEND_FAILED("serverstatus.send.failed"),
    SEND_SUCCEEDED("serverstatus.send.succeeded"),
    CONNECT_FAILED("serverstatus.connect.failed");

    private String key;

    TranslatableKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}