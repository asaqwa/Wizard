package ab.control;

import ab.model.chat.Message;

import java.util.ArrayDeque;
import java.util.Queue;

public class MessageController {
    private Queue<Message> messages = new ArrayDeque<>();

    public synchronized void add(Message message) {
        messages.add(message);
    }

}
