package com.example.circlecisample

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatorTest {
    private lateinit var sut: Calculator

    @Before
    fun setUp() {
        sut = Calculator()
    }

    @Test
    fun when_one_add_two_then_three() {
        val actual = sut.add(1, 2)
        assertEquals(3, actual)
    }

    @Test
    fun when_two_minus_one_then_one() {
        val actual = sut.minus(2, 1)
        assertEquals(1, actual)
    }
}