package ab.control;

class MessageHandler extends Thread {
    private final MessageController messageController;
    private final boolean log;
    public MessageHandler(MessageController messageController, boolean log) {
        super("Message Handler");
        this.messageController = messageController;
        setDaemon(true);
        this.log = log;
    }

    @Override
    public void run() {
        while (true) {

            messageController.sort(messageController.get());
        }
    }
}