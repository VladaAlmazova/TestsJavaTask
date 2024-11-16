package example.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование класса Контейнер
 */
class ContainerTest {

    Container testContainer;

    /**
     * Инициализация тестового контейнера
     */
    @BeforeEach
    void initContainer() {
        testContainer = new Container();
    }

    /**
     * Тестирование добавления элемента в контейнер
     */
    @Test
    void addItemTest() {
        Item firstItem = new Item(12L);
        testContainer.add(firstItem);
        Assertions.assertEquals(1, testContainer.size());
        Assertions.assertTrue(testContainer.contains(firstItem));

        Item secondItem = new Item(45L);
        testContainer.add(secondItem);
        Assertions.assertEquals(2, testContainer.size());
        Assertions.assertTrue(testContainer.contains(secondItem));
    }

    /**
     * Тестирование удаления элемента из контейнера
     */
    @Test
    void removeItemTest() {
        Item firstItem = new Item(12L);
        testContainer.add(firstItem);
        Item secondItem = new Item(45L);
        testContainer.add(secondItem);

        testContainer.remove(firstItem);
        Assertions.assertEquals(1, testContainer.size());
        Assertions.assertFalse(testContainer.contains(firstItem));

        testContainer.remove(secondItem);
        Assertions.assertEquals(0, testContainer.size());
    }
}