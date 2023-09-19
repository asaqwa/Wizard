package ab.model.chat;

public class Message {
    private final String text;
    private final MessageType type;

    public Message(String text, MessageType type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public MessageType getType() {
        return type;
    }
}
