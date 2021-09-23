package com.example.exampleapplication;

import kotlin.jvm.functions.Function2;

public class JavaExample {
    // 40-1. 고차함수
    // 람다식 바로 사용하는 경우는 JAVA 8부터 가능
//    int result0 = KotlinExampleKt.yayaya(10, x -> x + 1);
    int result = KotlinExampleKt.yayaya(10, new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer invoke(Integer integer, Integer integer2) {
            return integer * integer2;
        }
    });
}
