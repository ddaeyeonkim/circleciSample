package com.example.lib

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MyClassTest {
    private lateinit var sut: MyClass

    @Before
    fun setUp() {
        sut = MyClass()
    }

    @Test
    fun test() {
        sut.fly()
        Assert.assertEquals(1, sut.height)
    }
}