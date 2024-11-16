package example.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Тестирование класса верхнеуровневой логики бота
 */
class BotLogicTest {

    /**
     * Фейковый бот
     */
    private FakeBot fakeBot;

    /**
     * Пользователь по умолчанию
     */
    private final User user = new User(10L);

    /**
     * Верхнеуровневая логика бота
     */
    private BotLogic botLogic;

    /**
     * Возвращает последнее сообщение "отправленное" пользователю
     */
    private String lastMessageSent() {
        return fakeBot.getMessages().getLast();
    }

    /**
     * Начальное определение полей
     */
    @BeforeEach
    void initBotLogic() {
        fakeBot = new FakeBot();
        botLogic = new BotLogic(fakeBot);
    }

    /**
     * Тест для команды тестирования.
     * Если пользователь дает правильный ответ
     */
    @Test
    void testCommandTestCorrectAnswers() {
        botLogic.processCommand(user, "/test");

        Assertions.assertEquals("Вычислите степень: 10^2", lastMessageSent());

        botLogic.processCommand(user, "100");

        Assertions.assertEquals("Правильный ответ!", fakeBot.getMessages().get(1));
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", lastMessageSent());
    }

    /**
     * Тест для команды тестирования.
     * Если пользователь дает неправильный ответ
     */
    @Test
    void testCommandTestUnCorrectAnswers() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "10");

        Assertions.assertEquals("Вы ошиблись, верный ответ: 100", fakeBot.getMessages().get(1));
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", lastMessageSent());
    }

    /**
     * Тест для команды тестирования.
     * Пройдены все вопросы
     */
    @Test
    void testCommandTestEndTest() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "10");
        botLogic.processCommand(user, "6");

        Assertions.assertEquals("Тест завершен", lastMessageSent());
    }

    /**
     * Имитирует неправильные ответы на вопросы в тесте
     */
    void userAnswersIncorrectlyInTest() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "10");
        botLogic.processCommand(user, "10");
    }

    /**
     * Тест для команды повторения.
     * Есть вопросы для повторения, пользователь отвечает верно.
     */
    @Test
    void testRepeatCorrectAnswers() {
        userAnswersIncorrectlyInTest();

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Вычислите степень: 10^2", lastMessageSent());
        botLogic.processCommand(user, "100");

        Assertions.assertEquals("Правильный ответ!",
                fakeBot.getMessages().get(fakeBot.getMessages().size() - 2));

        botLogic.processCommand(user, "/stop");

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", lastMessageSent());
    }

    /**
     * Тест для команды повторения.
     * Есть вопросы для повторения, пользователь отвечает неверно.
     */
    @Test
    void testRepeatUnCorrectAnswers() {
        userAnswersIncorrectlyInTest();

        botLogic.processCommand(user, "/repeat");
        botLogic.processCommand(user, "10");

        Assertions.assertEquals("Вы ошиблись, верный ответ: 100",
                fakeBot.getMessages().get(fakeBot.getMessages().size() - 2));

        botLogic.processCommand(user, "/stop");

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Вычислите степень: 10^2", lastMessageSent());
    }

    /**
     * Тест для команды повторения.
     * Пройдены все вопросы
     */
    @Test
    void testRepeatEndRepeat() {
        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Нет вопросов для повторения", lastMessageSent());

        userAnswersIncorrectlyInTest();
        botLogic.processCommand(user, "/repeat");
        botLogic.processCommand(user, "100");
        botLogic.processCommand(user, "6");

        Assertions.assertEquals("Тест завершен", lastMessageSent());
    }


    /**
     * Тест команды уведомления
     * Неверный формат задержки и одно уведомление
     */
    @Test
    void testOneNotifyUnCorrectDelay() throws InterruptedException {
        botLogic.processCommand(user, "/notify");

        botLogic.processCommand(user, "напоминание");
        botLogic.processCommand(user, "3.5");

        Assertions.assertEquals("Пожалуйста, введите целое число", lastMessageSent());

        botLogic.processCommand(user, "1");
        Assertions.assertEquals("Напоминание установлено", lastMessageSent());
        Assertions.assertEquals(4, fakeBot.getMessages().size());
        Thread.sleep(1020);
        Assertions.assertEquals("Сработало напоминание: 'напоминание'", lastMessageSent());
    }

    /**
     * Тест команды уведомления
     * Несколько напоминаний
     */
    @Test
    void testFewNotify() throws InterruptedException {
        botLogic.processCommand(user, "/notify");
        botLogic.processCommand(user, "долгое");
        botLogic.processCommand(user, "3");

        botLogic.processCommand(user, "/notify");
        botLogic.processCommand(user, "быстрое");
        botLogic.processCommand(user, "1");

        Thread.sleep(1020);
        Assertions.assertEquals("Сработало напоминание: 'быстрое'", lastMessageSent());

        Thread.sleep(3020);
        Assertions.assertEquals("Сработало напоминание: 'долгое'", lastMessageSent());
    }

}