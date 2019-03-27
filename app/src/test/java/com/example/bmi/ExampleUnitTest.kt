import com.example.bmi.logic.BmiForKgCm
import com.example.bmi.logic.BmiForLbIn
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun for_valid_data_should_count_bmi_kg_cm(){
        val bmi1 = BmiForKgCm(65, 170)
        val bmi2 = BmiForKgCm(0, 170)
        val bmi3 = BmiForKgCm(-3, 170)
        val bmi4 = BmiForKgCm(65, 0)
        val bmi5 = BmiForKgCm(65, -7)
        val bmi6 = BmiForKgCm(-4, -8)

        assertEquals(22.4913, bmi1.countBmi(), 0.001)
        assertEquals(-1.0, bmi2.countBmi(), 0.0)
        assertEquals(-1.0, bmi3.countBmi(), 0.0)
        assertEquals(-2.0, bmi4.countBmi(), 0.0)
        assertEquals(-2.0, bmi5.countBmi(), 0.0)
        assertEquals(-3.0, bmi6.countBmi(), 0.0)
    }

    @Test
    fun for_valid_data_should_count_bmi_lb_in(){
        val bmi1 = BmiForLbIn(163, 71)
        val bmi2  = BmiForLbIn(0, 71)
        val bmi3 = BmiForLbIn(-5, 71)
        val bmi4  = BmiForLbIn(163, 0)
        val bmi5 = BmiForLbIn(163, -5)
        val bmi6 = BmiForLbIn(-5, -5)

        assertEquals(22.7314, bmi1.countBmi(), 0.0001)
        assertEquals(-1.0, bmi2.countBmi(), 0.0)
        assertEquals(-1.0, bmi3.countBmi(), 0.0)
        assertEquals(-2.0, bmi4.countBmi(), 0.0)
        assertEquals(-2.0, bmi5.countBmi(), 0.0)
        assertEquals(-3.0, bmi6.countBmi(), 0.0)
    }

}

