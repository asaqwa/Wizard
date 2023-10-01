package ab.control;

import ab.model.chat.Message;
import ab.model.chat.MessageType;

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
            System.out.println("wait");
            try {
                wait(3000);
                messages.add(new Message(MessageType.SERVER_FOUND, "new server"));
            } catch (Exception e) {
                System.out.println("message received");
                return new Message(MessageType.SERVER_FOUND, "new server");
//                throw new RuntimeException(e);
            }
        }
        return messages.poll();
    }

    void sort(Message message) {
        System.out.println("for sort");
        switch (message.getType()) {
            case SERVER_FOUND:
                System.out.println("sort");
                controller.viewController.newServer(message.getData());

        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
        new MessageHandler(this).start();
    }
}
