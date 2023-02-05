package com.example.lib

class MyClass {
    private var _height = 0
    val height get() = _height

    fun fly() {
        _height++
    }
}