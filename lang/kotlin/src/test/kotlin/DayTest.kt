package test.kotlin

import Day
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

abstract class DayTest<T : Day>(private val day: T) {

    private val inputTestString by lazy { readTestFileAsStringFromTest(day.fileName) }

    private val inputString by lazy { readFileAsStringFromTest(day.fileName) }

    @TestFactory
    fun tests(): Collection<DynamicTest> =
        listOf(
            if (day.firstTestAnswer != null) firstTest() else nullDynamicTest("1. firstTestAnswer"),
            firstRealCalculate(),
            if (day.secondTestAnswer != null) secondTest() else nullDynamicTest("3. secondTestAnswer"),
            secondRealCalculate()
        )

    private fun firstTest() = dynamicTest("1. First task -> Test") {
        assertEquals(day.fistPart(inputTestString), day.firstTestAnswer)
    }

    private fun firstRealCalculate() = dynamicTest("2. First task -> Answer") {
        println("Answer: ${day.fistPart(inputString)}")
    }

    private fun secondTest() = dynamicTest("3. Second task -> Test") {
        assertEquals(day.secondPart(inputTestString), day.secondTestAnswer)
    }

    private fun secondRealCalculate() = dynamicTest("4. Second task -> Answer") {
        println("Answer: ${day.secondPart(inputString)}")
    }

    private fun nullDynamicTest(variableName: String) = dynamicTest("$variableName - not specified") {
        throw NotImplementedError("$variableName is null !")
    }
}