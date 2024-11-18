package example.bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Фейковый класс бота, который сохраняет сообщения предназначенные пользователю
 */
class FakeBot implements Bot {
    /**
     * Список "отправленных" сообщений
     */
    private final List<String> messages = new ArrayList<>();

    /**
     * Возвращает историю "отправленных" сообщений
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Возвращает последнее сообщение "отправленное" пользователю
     */
    public String lastMessageSent() {
        return messages.getLast();
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(message);
    }
}
