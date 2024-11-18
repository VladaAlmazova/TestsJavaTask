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

        Assertions.assertEquals("Вычислите степень: 10^2", fakeBot.lastMessageSent());

        botLogic.processCommand(user, "100");

        Assertions.assertEquals("Правильный ответ!", fakeBot.getMessages().get(1));
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", fakeBot.lastMessageSent());
        botLogic.processCommand(user, "6");
        Assertions.assertEquals("Тест завершен", fakeBot.lastMessageSent());
    }

    /**
     * Тест для команды тестирования.
     * с неправильным ответом
     */
    @Test
    void testCommandTestUnCorrectAnswers() {
        botLogic.processCommand(user, "/test");
        Assertions.assertEquals("Вычислите степень: 10^2", fakeBot.lastMessageSent());
        botLogic.processCommand(user, "10");

        Assertions.assertEquals("Вы ошиблись, верный ответ: 100", fakeBot.getMessages().get(1));
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", fakeBot.lastMessageSent());
    }

    /**
     * Проверяет, что в повторение попадают только вопросы,
     * на которые пользователь ответил неверно
     */
    @Test
    void testRepeatCorrectCompilation() {
        botLogic.processCommand(user, "/test");
        if (fakeBot.lastMessageSent().equals("Вычислите степень: 10^2"))
            botLogic.processCommand(user, "100");
        if (fakeBot.lastMessageSent().equals("Сколько будет 2 + 2 * 2"))
            botLogic.processCommand(user, "10");

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", fakeBot.lastMessageSent());
        botLogic.processCommand(user, "10");
        Assertions.assertEquals("Тест завершен", fakeBot.lastMessageSent());
    }

    /**
     * Тест для команды повторения.
     * Есть вопросы для повторения, пользователь отвечает верно.
     */
    @Test
    void testRepeatCorrectAnswers() {
        botLogic.processCommand(user, "/test");
        if (fakeBot.lastMessageSent().equals("Вычислите степень: 10^2"))
            botLogic.processCommand(user, "10");
        if (fakeBot.lastMessageSent().equals("Сколько будет 2 + 2 * 2"))
            botLogic.processCommand(user, "10");

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Вычислите степень: 10^2", fakeBot.lastMessageSent());
        botLogic.processCommand(user, "100");

        Assertions.assertEquals("Правильный ответ!",
                fakeBot.getMessages().get(fakeBot.getMessages().size() - 2));

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", fakeBot.lastMessageSent());
    }

    /**
     * Тест для команды повторения.
     * Есть вопросы для повторения, пользователь отвечает неверно.
     */
    @Test
    void testRepeatUnCorrectAnswers() {
        botLogic.processCommand(user, "/test");
        if (fakeBot.lastMessageSent().equals("Вычислите степень: 10^2"))
            botLogic.processCommand(user, "10");

        botLogic.processCommand(user, "/repeat");
        if (fakeBot.lastMessageSent().equals("Вычислите степень: 10^2"))
            botLogic.processCommand(user, "10");

        Assertions.assertEquals("Вы ошиблись, верный ответ: 100",
                fakeBot.getMessages().get(fakeBot.getMessages().size() - 2));

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Вычислите степень: 10^2", fakeBot.lastMessageSent());
    }

    /**
     * Тест для команды повторения.
     * Нет вопросов
     */
    @Test
    void testRepeatNoRepeat() {
        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Нет вопросов для повторения", fakeBot.lastMessageSent());
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

        Assertions.assertEquals("Пожалуйста, введите целое число", fakeBot.lastMessageSent());

        botLogic.processCommand(user, "1");
        Assertions.assertEquals("Напоминание установлено", fakeBot.lastMessageSent());
        Assertions.assertEquals(4, fakeBot.getMessages().size());
        Thread.sleep(1020);
        Assertions.assertEquals("Сработало напоминание: 'напоминание'", fakeBot.lastMessageSent());
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
        Assertions.assertEquals("Сработало напоминание: 'быстрое'", fakeBot.lastMessageSent());

        Thread.sleep(3020);
        Assertions.assertEquals("Сработало напоминание: 'долгое'", fakeBot.lastMessageSent());
    }

}