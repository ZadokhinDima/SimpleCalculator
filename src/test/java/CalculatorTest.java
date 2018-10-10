import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {


    @Test
    void calculateTest() throws Exception {
        assertEquals(26,Calculator.calculate("4+3*8-20/10"));
    }


}