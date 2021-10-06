package com.example.exampleapplication

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
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
 * 사용할  Dispatcher을 결정하고 -> Dispatcher을 이용하여 CoroutineScope를 만들고 -> CoroutineScope의 launch 또는 async에 수행할 코드 블럭을 넘긴다.
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


    // [10]. Context, Dispatchers
    // 코루틴의 context는 여러요소의 set으로 구성되며, main요소는 Job과 Dispatcher이다.
    // 코루틴 context는 어떤 쓰레드에서 해당 코루틴을 실행할지에 대한 dispatcher 정보를 담고있다.
    // 이 dispatcher에 따라서 실행될 특정 thread를 지정하거나, thread pool을 지정하거나, 지정없이 사용할 수 있다.
    // lanch와 async같은 코루틴 빌더는 코루틴Context값을 옵션으로 지정할 수 있고, 이를 통해 dispatcher를 지정할 수 있다.
    fun test13() = runBlocking<Unit> {
        launch {
            println("main runBlocking : l'm working in thread ${Thread.currentThread().name}")
            // Main : UI를 구성하는 스레드를 메인으로 사용하는 플랫폼에서 사용된다.
        }
        launch(Dispatchers.Unconfined) {
            println("Unconfined : l'm working in thread ${Thread.currentThread().name}")
            // Unconfined : 특정 스레드, 특정 스레드 풀을 지정하지 않는다. 일반적으로 사용하지 않으며 특정 목적을 위해서만 사용됨
        }
        launch(Dispatchers.Default) {
            println("Default : l'm working in thread ${Thread.currentThread().name}")
            // Default : CPU사용량이 많은 작업에 사용된다. 주 스레드에서 작업하기에는 너무 긴 작업들에 적합하다.
            // GlobalScope.launch와 같은 역할을 한다.
        }
        launch(newSingleThreadContext("MyOwnThread")) {
            // will get its own new thread
            println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
            // 코루틴을 실행시킬 새로운 thread를 생성한다. --> 비싼 리소스를 사용하게 된다.
        }

        // [11]. Unconfined , confined Dispatcher
        // Dispatcher를 Unconfined로 설정하면, 해당 코루틴은 caller thread에서 시작된다.
        // 이 코루틴이 suspend되었다가 상태가 재시작되면 적절한 thread에 재할당되어 시작된다.
        // 따라서 Unconfined는 CPU time을 소비하지 않는 작업이나, 공유되는 데이터에 접근하지 않아야 하는 작업들에서 이용하는게 적절하다.
        // 즉 UI처럼 main thread에서만 수행되어야하는 작업들은 unconfined로 지정하면 안된다.
        // dispatcher의 기본값은 외부 CoroutineScope의 값을 상속받는다.
        // 특히 runBlocking의 기본 dispatcher은 이를 시작한 thread이다.
        // CoroutineScope에서 다른 코루틴을 launch시키면 해당 코루틴은 CoroutineScope.coroutineContext를 상속받고, 이때 같이 새로 생성되는
        // Job 역시 부모 코루틴 job의 자식이 됩니다.
        // 부모 코루틴이 취소되면 자식들 역시 재귀적으로 전부 취소된다.

        // 부모 코루틴은 자식 코루틴이 끝날때까지 항상 대기한다.

        // [12]. 코루틴 Naming
        // 코루틴에도 이름을 줄 수 있다.
        fun test14() = runBlocking(CoroutineName("yhCoroutine")) {
            println("Started main coroutine")
            val job = async(CoroutineName("yhAsync")) {
                println("Started async coroutine")
            }
        }
        // 출력값 -- > [main@yhCoroutine#1] Started main coroutine
        //       -- > [main@yhAsync#2] Started async coroutine

        // [13]. Combining context elements
        // context는 여러요소를 가질 수 있다. 복합적인 요소갖기 위해서는 + 연산자 이용한다.
        fun test15() = runBlocking {
            launch(Dispatchers.Default + CoroutineName("yhCoroutine")) {
                println("l'm working in thread ${Thread.currentThread().name}")
            }
        }

        // [14]. Job 생성
        // object에 종속적으로 동작해야하는 경우 Job을 직접 생성해서 사용할 수 있다.
        Activity()

        // [15]. Thread - local Data
        // 코루틴의 경우 특정 코루틴 내에서만 사용되는 local data를 지정할 수 있다.
        // 이 값은 context에 추가적인 요소로 들어가면서 코루틴의 context가 동일하다면 get()하여 값을 얻어갈 수 있고,
        // context가 switch되면 해당 context값으로 재설정 된다.
        // context에 값을 저장하기 위해서는 확장함수인 'ThreadLocal'과 'asContentElement' 가 있다.
        val threadLocal = ThreadLocal<String?>()
        fun test16() = runBlocking {
            threadLocal.set("main")
            println("Thread Local1 : ${threadLocal.get()}")
            val test1 = launch(Dispatchers.Default + threadLocal.asContextElement("launch")) {
                println("Thread Local2 : ${threadLocal.get()}")
                // 코루틴의 context가 변경되므로 asContextElement를 이용해 값 변경 가능!!
            }
        }

        val threadLocal2 = ThreadLocal<String?>()
        threadLocal2.set("main2")
        println("Thread Local2 : ${threadLocal2.get()}")
        threadLocal2.set("main3")
        // 같은 context에서는 값이 불가능하다!!!!! --> main2로 출력됨
        // 수정하고자 한다면 withContext 사용
        withContext(threadLocal2.asContextElement("main4")) {
            println("Thread Local3 : ${threadLocal2.get()}")
        }

        // [16]. Exception
        // 참고 : https://jaejong.tistory.com/66
        // Exception을 외부로 전파시킴 : launch, actor
        // Exception을 노출 시킴 : async, produce
        // launch의 경우 exception이 발생하면 바로 예외가 발생한다.
        // async의 경우 코드가 수행되어 exception이 있더라도 실제로 exception이 발생하는 부분은 await()를 만났을 때 입니다.
        // 코루틴은 취소를 제외한 다른 exception이 발생하면 부모의 코루틴까지 모두 취소시킨다.
        // 자식 코루틴에서 exception이 발생되면 다른 자식 코루틴이 모두 취소된 이후에 부모에 의해서 exception이 handling 됩니다.
        // 자식 coroutine에서 여러개의 exception이 발생할 경우 가장 먼저 발생한 exception이 handler로 전달됩니다.


        // [17]. SupervisorJob
        // 양방향 전파 : 아래 방향(부모 -> 자식) + 위 방향 (자식 -> 부모) 모두 전파
        // 부모 예외로 인한 취소 발생 시 모든 자식 종료
        // 자식 예외로 인한 취소 발생 시 부모 / 모든 자식 종료
        // 단방향 전파일 경우 SupervisorJob 사용
        // 단방향 전파 : 아래 방향(부모 -> 자식)만 전파되는 것
        // 부모 예외로 인한 취소 발생 시 모든 자식 종료
        // 자식 예외로 인한 취소 발생 시 해당 Job만 종료
        // **** 코루틴 빌더에 인자로 넘겨줘서 사용
//        fun test17() = runBlocking<Unit> {
//            val supervisor = SupervisorJob()
//            with(CoroutineScope(coroutineContext + supervisor)) {
//                val firstChild = launch(CoroutineExceptionHandler { coroutineContext, throwable ->  }) {
//                    println("First child is failing")
//                    throw AssertionError("First child is cancelled")
//                }
//                // launch the second child
//                val secondChild = launch {
//                    firstChild.join()
//                    // Cancellation of the first child is not propagated to the second child
//                    println("First child is cancelled: ${firstChild.isCancelled}, but second one is still active")
//                    try {
//                        delay(Long.MAX_VALUE)
//                    } finally {
//                        // But cancellation of the supervisor is propagated
//                        println("Second child is cancelled because supervisor is cancelled")
//                    }
//                }
//                // wait until the first child fails & completes
//                firstChild.join()
//                println("Cancelling supervisor")
//                supervisor.cancel()
//                secondChild.join()
//            }
//            // 결과를 보면 firstChild에서 예외를 발생시켰는데, secondChild는 영향없이 잘 작동하는걸 볼 수 있다.
//        }
    }
}

// CoroutineScope를 상속받았기 때문에 activity내에서는 코루틴 함수들을 잘 사용할 수 있다.
// override한 coroutineContext의 job을 coroutine context로 사용하도록 한다.
// activity 내부에서 시작되는 모든 coroutine들은 같은 context를 사용하고, activity 종료 시 전부 취소될 수 있다.
class Activity : CoroutineScope {
    lateinit var job: Job

    fun create() {
        job = Job()
    }

    fun destroy() {
        job.cancel()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun doSomething() {
        repeat(10) { i ->
            launch {
                delay((i + 1) * 200L)
                println("Coroutine is done")
            }
        }
    }
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