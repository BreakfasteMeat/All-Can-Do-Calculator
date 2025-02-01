package com.example.all_can_do_calculator

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
        val mainCalculator = MainCalculator()
        mainCalculator.input = "-4+1"
        mainCalculator.calculate()
        println(mainCalculator.result)
    }
}