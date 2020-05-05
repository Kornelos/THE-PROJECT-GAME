package pl.mini.messages;

public interface JsonMessage {

    String toJsonString();

    /**
     * @return "server" - if target is a server
     * playerUUID - if player is a target
     * "gm" - if gamemaster
     * "all" - if everyone
     */
    MessageAction getAction();

    String getTarget();

}
