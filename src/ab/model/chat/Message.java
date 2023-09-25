package ab.model.chat;

public class Message {
    private final String text;
    private final MessageType type;
    public static final Message NAME_REQUEST = new Message(MessageType.NAME_REQUEST, null);
    public static final Message NAME_ACCEPTED = new Message(MessageType.NAME_ACCEPTED, null);

    public Message( MessageType type, String text) {
        this.text = text;
        this.type = type;
    }

    public String getData() {
        return text;
    }

    public MessageType getType() {
        return type;
    }
}
