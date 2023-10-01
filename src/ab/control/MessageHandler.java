package ab.control;

class MessageHandler extends Thread {
    private final MessageController messageController;
    public MessageHandler(MessageController messageController) {
        super("Message Handler");
        this.messageController = messageController;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            messageController.sort(messageController.get());
        }
    }
}