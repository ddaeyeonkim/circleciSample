package com.example.data

class Car {
    private var _position = 0
    val position get() = _position

    fun move() {
        _position++
    }
}