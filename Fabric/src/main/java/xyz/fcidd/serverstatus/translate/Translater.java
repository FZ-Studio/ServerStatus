package xyz.fcidd.serverstatus.translate;

import xyz.fcidd.serverstatus.ServerStatus;

public class Translater {
    public static final String COMMAND_FAILED_DEFAULT = "serverstatus.command.failed";
    public static final String COMMAND_SUCCEED_DEFAULT = "serverstatus.command.succeeded";
    public static final String COMMAND_WRONG_FORMAT_DEFAULT = "serverstatus.command.wrongformat";
    public static final String COMMAND_HELP = "serverstatus.command.help";
    public static final String COMMAND_SETLANG_SUCCEEDED = "serverstatus.command.succeeded.setlang";
    public static final String COMMAND_DEFAULT_LANG_SUCCEEDED = "serverstatus.command.succeeded.defaultlang";
    public static final String COMMAND_SETHOST_SUCCEEDED = "serverstatus.command.succeeded.sethost";
    public static final String COMMAND_DEFAULT_HOST_SUCCEEDED = "serverstatus.command.succeeded.defaulthost";
    public static final String COMMAND_SETPORT_SUCCEEDED = "serverstatus.command.succeeded.setport";
    public static final String COMMAND_DEFAULT_PORT_SUCCEEDED = "serverstatus.command.succeeded.defaultport";
    public static final String COMMAND_RELOAD_SUCCEEDED = "serverstatus.command.succeeded.reload";
    public static final String COMMAND_SETLANG_WRONG_FORMAT = "serverstatus.command.wrongformat.setlang";
    public static final String COMMAND_SETHOST_WRONG_FORMAT = "serverstatus.command.wrongformat.sethost";
    public static final String SEND_FAILED = "serverstatus.send.failed";
    public static final String SEND_SUCCEEDED = "serverstatus.send.succeeded";
    public static final String CONNECT_FAILED = "serverstatus.connect.failed";

    public static String getTranslated(String key, String... args) {
        return ServerStatus.getConfig().getLang().getTranslated(key, args);
    }
}
