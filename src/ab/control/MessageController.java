package ab.control;

import ab.model.chat.Message;
import ab.model.chat.ServerFoundMessage;

import java.util.ArrayDeque;
import java.util.Queue;

public class MessageController {
    Controller controller;
    private Queue<Message> messages = new ArrayDeque<>();



    public synchronized void add(Message message) {
        messages.add(message);
        notify();
    }

    public synchronized Message get() {
        while (messages.isEmpty()) {
            try {
                wait(3000);
//                messages.add(new Message(MessageType.SERVER_FOUND, "new server"));
            } catch (Exception e) {
//                return new Message(MessageType.SERVER_FOUND, "new server");
//                throw new RuntimeException(e);
            }
        }
        return messages.poll();
    }

    void sort(Message message) {
        switch (message.getType()) {
            case SERVER_FOUND:
                controller.viewController.newServerRegistration((ServerFoundMessage) message);

        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
        new MessageHandler(this, controller.log).start();
    }
}
