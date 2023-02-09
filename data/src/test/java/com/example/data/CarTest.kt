package com.example.data

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CarTest {
    private lateinit var sut: Car

    @Before
    fun setUp() {
        sut = Car()
    }

    @Test
    fun move_test() {
        sut.move()
        assertEquals(1, sut.position)
    }
}