package example.note;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Spliterators;

/**
 * Тестирование класса логики заметок
 */
class NoteLogicTest {

    private NoteLogic noteLogic;

    /**
     * Инициализация класса логики заметок
     */
    @BeforeEach
    void initNoteLogic(){
        noteLogic = new NoteLogic();
    }

    /**
     * Тестирование команды добавления заметки
     * и печати заметок пользователя
     */
    @Test
    void addAndPrintNoteTest(){
        String replyMessage = noteLogic.handleMessage("/add nameNote");
        Assertions.assertEquals(
                "Your notes:"+"\n"+"nameNote",
                noteLogic.handleMessage("/notes"));
        Assertions.assertEquals("Note added!", replyMessage);
    }

    /**
     * Тестирование команды изменения названия заметки
     */
    @Test
    void editNoteTest(){
        noteLogic.handleMessage("/add oldNameNote");
        String replyMessage = noteLogic.handleMessage("/edit oldNameNote newNameNote");
        Assertions.assertEquals(
                "Your notes:"+"\n"+"newNameNote",
                noteLogic.handleMessage("/notes"));
        Assertions.assertEquals("Note edited!", replyMessage);
    }

    /**
     * Тестирование команды удаления заметки
     */
    @Test
    void delNoteTest(){
        noteLogic.handleMessage("/add delNameNote");
        String pastNotes = noteLogic.handleMessage("/notes");
        String replyMessage = noteLogic.handleMessage("/del delNameNote");
        String nowNotes = noteLogic.handleMessage("/notes");

        Assertions.assertNotEquals(pastNotes, nowNotes);
        Assertions.assertEquals("Your notes:", nowNotes);
        Assertions.assertEquals("Note deleted!", replyMessage);
    }
}