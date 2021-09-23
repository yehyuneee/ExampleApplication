package com.example.exampleapplication

class KotlinExample {
    val yayaresult = yayaya(10, { x, y -> x * y })
}
// 40-1. 고차함수
fun yayaya(num1: Int, yaho: (Int, Int) -> Int): Int {
    val result = yaho(10, 20)
    println("kotlin test : " + result)
    return result
}