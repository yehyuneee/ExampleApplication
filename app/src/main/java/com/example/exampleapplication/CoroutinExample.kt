package com.example.exampleapplication

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * 코루틴의 핵심 - light-weight thread
 * launch는 코루틴의 빌더이며, 이를 이용해서 couroutineScope안에서 실행시킴
 * GlobalScope에서 코루틴 실행
 *
 * **** GlobalScope
 * lifetime은 전체 application의 process의 의존하므로, application이 끝나면 같이 종료되므로, sleep 걸고 기다려야 lauch 내부의 동작 실행할 수 있음
 *
 * 기초 설명 참고 : https://medium.com/@limgyumin/%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%BD%94%EB%A3%A8%ED%8B%B4%EC%9D%98-%EA%B8%B0%EC%B4%88-cac60d4d621b
 */
fun couroutinMain() {
    GlobalScope.launch {
        delay(1000L)
        println("World~")
    }
    // **** GlobalScope
    // 어플이 동작하는 동안 별도의 생명주기를 관리하지않고 사용할 수 있다.
    // 최초 접근 시 생성된다!!

    println("Hello")
    Thread.sleep(2000L)

    // GlobalScope.lauch == thread
    // delay == Thread.sleep

    // [1]. Waiting job
    // 작업완료를 기다리기 위해 실제로는 'join'을 사용한다. ---> non-blocking 코드
    fun main() = runBlocking {
        val job = GlobalScope.launch {
            delay(1000L)
        }

        job.join()
        // launch의 리턴값으로 Job이 반환됩니다.
        // 이는 Job의 스케쥴관리나 상태등을 확인할 때 사용된다.
        // ********* Job
        // Job은 코루틴으 핸들이다. 각 코루틴은 코루틴을 고유하게 식별하고 수명주기를 관리하는 job인스턴스를 반환한다.
    }


    // GlobalScope는 process가 살아있는 한 계속 유지되므로, 계속 lauch하는 방식은 메모리나 관리면에서 에러를 발생시킬 수 있다.
    // 이러한 수동작업을 해결하기 위해 Structured concurrency(구조적 동시성)
    // 이는 특정한 Couroutine scope를 만들고 그안에서 새로운 couroutine 빌더로 코루틴을 시작하면 생성된 CoroutineScope이 코드블럭의
    // couroutineScope에 더해진다. 따라서 외부 scope는 내부의 코루틴들이 종료되기 전까지는 종료되지않으므로 join 없는 간단한 코드 생성 가능!!

    // [2]. join()을 통한 실행 보장
    // ** 코루틴 (외부)블럭은 내부에서 실행되는 코루틴이 모두 완료되어야만 (외부블럭이)완료된다.
    fun test1() {
        runBlocking {
            val job = List(10) {
                launch {
                    delay(1000L)
                    println("aaa")
                }
            }

            // join을 하고 안하고에 따라 End runBlock이 먼저 찍힐지 끝나고 찍힐지가 결정된다.
            // jobs.forEach { it.join() }

            // *** join() 은 실행을 보장시켜준다!!

            println("End RunBlock")
        }
        println("End Function")
    }

    // [3]. Couroutine scope 내부 생성해 순서 보장
    // ** 코루틴 (외부)블럭은 내부에서 실행되는 코루틴이 모두 완료되어야만 (외부블럭이)완료된다. ==>>> 중요한 사실!!!
    fun test2() = runBlocking {
        launch {
            delay(2000L)
            println("Task from runBlocking#2")
        }

        // Create
        // 매번 생성된다.
        coroutineScope {
            launch {
                delay(1000L)
                println("Task from runBlocking#3")
            }
            delay(1000L)
            println("Task from runBlocking#1")
        }
        println("Task from runBlocking#4")
    }

    // [4]. Extract function refactoring ( 추출함수 리팩토링 )
    // 코루틴에서 사용하는 코드를 외부로 빼내려면 'suspend' 키워드를 사용한다
    // 내부에서 코루틴 api를 사용할 수 있음.
    fun test3() = runBlocking {
        launch {
            doWorld()
        }
        println("Hello~")
    }

    // [5]. Global couroutines are like demon threads
    // GlobalScope에서 수행하는 코루틴은 demon thread와 같다
    // 이는 process가  kill되면 같이 멈춘단느걸 의미한다.
    fun test4() = runBlocking {
        GlobalScope.launch {
            repeat(1000) { i ->
                println("i'm sleeping $i")
                delay(500L)
            }
        }
        delay(1300L)
    }

    // runBlocking은 내부에서 발생한 모든 코루틴의 동작을 보장한다.
    // 내부에서 GlobalScope를 이용하여 lauch하였기 때문에 runBlocking와는 다른 scope를 갖는다.
    // 따라서 runBlock은 1.3초만 대기하고 종료하고, main 함수가 종료되면서 application process 역시 종료된다.

    // [6]. lauch 함수가 return하는 job을 통한 취소 처리
    // backgroud 작업을 진행중에 페이지가 넘어가서 더이상 결과가 필요하지 않다면 취소해야 될 경우
    fun test5() = runBlocking {
        val job =
            launch {
                repeat(1000) { i ->
                    println("I'm sleeping $i ...")
                    delay(500L)
                }
            }
        delay(1300L) // delay a bit

        println("main: I'm tired of waiting!")
        job.cancel() // cancels the job
        job.join() // waits for job's completion
        println("main: Now I can quit.")
    }

    // [7]. Timeout
    // 일일이 Job으로 취소하지 않고 , 특정시간 이후에 취소하게 하려면 withTimeout을 사용하면 된다.
    fun test6() = runBlocking {
        withTimeout(1300L) {
            repeat(1000) { i ->
                println("i'm sleeping $i")
                delay(500L)
            }
        }
    }

    // withTimeout을 사용할 때는 꼭 try-catch로 묶어서 사용해야 앱이 중지되는걸 방지한다.
    fun test7() = runBlocking {
        try {
            withTimeout(1300L) {
                repeat(1000) { i ->
                    println("i'm sleeping $i")
                    delay(500L)
                }
            }
        } catch (te: TimeoutCancellationException) {
            println("timeout")
        }
    }

    // withTimeoutOrNull을 사용하면 exception throw대신 null을 반환한다.
    // withTimeoutOrNull, withTimeout은 마지막값이 return값이 된다.
    fun test8() = runBlocking {
        val result = withTimeoutOrNull(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i")
                delay(500L)
            }
            "Done"
        }
        println("Result is $result")
    }

    // [8]. async
    // 기본적으로 코드는 순차적인 특징을 갖지만, 이러한 경우 소요되는 시간이 길어질 수 있다.
    // 이러한 경우에 async를 사용하면 두개의 작업을 분리 및 동시진행 시킬 수 있다.
    // *** return 하는 객체가 다름!!
    // lauch -> job , async -> deferred
    // job은 lauch시킨 코루틴의 상태를 관리하는 용도로 사용되는 반면 코루틴의 결과를 리턴받을 수 없으나,
    // deferred는 async 블럭에서 수행된 결과를 return 받을 수 있다. 또한 job을 상속받아 구현되어 상태제어 또한 가능하다.
    fun test9() = runBlocking {
        val time = measureTimeMillis {
            val one = async { doSomethingOne() }
            val two = async { doSomethingTwo() }
            println("plus ${one.await() + two.await()} ")
        }
        println("Completed in $time ms")
    }

    // *** async로 시작되는 코루틴은 블럭내 코드의 수행을 지연시킬 수 있다!!
    // optional 파라미터인 start에 CoroutineStart.LAZY 을 주면 작업을 바로 실행하지 않는다.
    // 작업 수행하려면 start() 나 await()를 명시적으로 호출해주면 된다.
    fun test10() = runBlocking {
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) { doSomethingOne() }
            val two = async(start = CoroutineStart.LAZY) { doSomethingTwo() }
            println("plus ${one.await() + two.await()} ")
            // await()는 실행히 완료할 때까지 대기하므로, 순차적으로 실행된다.
        }
        println("Completed in $time ms")
    }

    // [9]. async style functions
    // GlobalScope에서 async couroutine builder를 사용하여 명시적으로 비동기 형식의 함수 만들 수 있다.
    fun test11() {
        val time = measureTimeMillis {
            val one = doSomethingOneGlobal()
            val two = doSomethingTwoGlobal()

            runBlocking {
                println("the answer is ${one.await() + two.await()}")
            }
        }
        println("completed in $time is")
    }
    // 실제로 값을 얻기 위해서 await()는 runBlocking에서 받아온다.
    // 만약 val one .. 과 await()사이에서 exception이 발생한다면 프로그램이 종료된다.
    // 외부로 try catch 처리해서 exception handling은 할 수 있으나 비동기처리 상태로 종료되게 된다.
    // ---------> 이러한 방법은 kotlin couroutine에서 비추천

//    fun test12() = runBlocking {
//        val time =
//            measureTimeMillis { println("The answer is ${concurrentSum()}") }
//        println("Completed in $time ms")
//    }


}

//suspend fun concurrentSum(): Int = coroutineScope {
//    val one = async { doSomethingOneGlobal() }
//    val two = async { doSomethingTwoGlobal() }
//    one.await() + two.await()
//}

// async 테스트 함수 1,2
suspend fun doSomethingOne(): Int {
    delay(1000L)
    return 13
}

suspend fun doSomethingTwo(): Int {
    delay(1000L)
    return 29
}

// async GlobalScope couroutine builder
// GlobalScope를 이용해 명시적으로 async함수를 만들었고, suspend function은 아니다.
// 어디서든 호출되서 사용할 수 있다!
fun doSomethingOneGlobal() = GlobalScope.async {
    doSomethingOne()
}

fun doSomethingTwoGlobal() = GlobalScope.async {
    doSomethingTwo()
}

// 코루틴에서 사용하는 코드를 외부로 빼내려면 'suspend' 키워드를 사용한다
suspend fun doWorld() {
    delay(1000L)
    println("World~")
}