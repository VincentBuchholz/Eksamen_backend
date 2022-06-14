package utils;

import errorhandling.DateFormatException;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class DateCheckerTest {

    @Test
    void CHECK_DATESTest() throws ParseException {
        assertTrue(DateChecker.CHECK_DATES("10/06/2022", "10/06/2023"));
        assertFalse(DateChecker.CHECK_DATES("10/05/2022", "10/06/2022"));
    }

    @Test
    void CORRECT_FORMATTest() {
            assertEquals(true,DateChecker.CORRECT_FORMAT("07/08/1999"));
    }
}