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
        val bmi = BmiForKgCm(65, 170)

        assertEquals(22.4913, bmi.countBmi(), 0.001)
    }

    @Test
    fun for_valid_data_should_count_bmi_lb_in(){
        val bmi = BmiForLbIn(163, 71)

        assertEquals(22.7314, bmi.countBmi(), 0.0001)
    }



}

