package com.example.exampleapplication

import android.app.Activity
import android.app.Person
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os
import java.io.File
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import java.lang.reflect.Type
import java.math.BigDecimal
import kotlin.Comparator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 확장함수
        // 클래스의 멤버 메서드처럼 호출되지만 클래스 밖에 호출되는 함수
        // 확장함수는 override 할 수 없다. (static의 성격을 띄기 때문)
        // 확장 프로퍼티도 확장 함수처럼 추가 가능 --> 상태 저장할 수 없기 때문에 단순히 확장 함수를 짧게 만드는 역할함
        println("test1 : " + "KotlinTest".test1().toString())
        val test1 = StringBuilder("Kotlin")
        test1.lastChar = '!'
        println("test1 : " + test1.toString())


        // 2. 가변인자
        // 자바에서는 '...' 코틀린에서는 vararg 키워드 사용 (파라미터 앞에 붙인다)
        test2("김예현", "예현", age = "29")

        // 3. 중위함수 (infix)
        // 변수와 변수사이에 함수를 넣어 연산자처럼 사용하는 것
        println("test3 : " + 1.multiplex(2).toString())
        println("test3 : " + 1.to("one"))

        // 4. 정규식 사용 여부 (toRegex())
        // 5. 삼중 따음표
        val kotlinLogo = """| // 
            .|//
            .|// \\""".trimMargin()
        println("test4 : " + kotlinLogo)

        // 6. 중첩함수
        // 함수안에 함수를 넣는 것 ( 자바에서는 불가능 )
        // 중복을 없애거나, 긴함수를 분리해서 사용할 때 유용하다.
        // 가독성 좋아지고 코드 중복 여지가 사라진다.
        saveUser(User("yehyune", "into1023@naver.com"))

        // 7. 부생성자
        // 생성자 여러개 필요한 경우 부생성자 사용
        // 부모의 생성자는 super(), 내 생성자 중 다른 생성자는 this()로 호출이 가능

        // 8. 인터페이스
        // 인터페이스에 property 추가 ( 자바에서는 불가능 )

        // 9. get() set() 한정자 제어

        // 10. 클래스의 기본 method 구현
        // toString(), equal override 해야 정상 동작 한다.

        // 11. data class
        // class 앞에 data 붙여줄 경우 10번에서 얘기한 것들 자동생성 된다.
        val bob = Client("Bob", 34567)
        println("test11 : " + bob.copy(postalCode = 111111))
        // copy : data 클래스를 불변 클래스로 만들면서 일부 값이 변경되어야 할 때 사용

        // 12. class 위임 : by
        // 코틀린은 기본적으로 class 상속을 제한한다.
        // 상속 원하는 경우 open class로 만들어야 한다.
        // by 는 클래스의 기능의 일부를 재구현하는 클래스를 만들 때 사용한다.


        // 13. object키워드
        // 싱글톤 , companion object를 이용한 팩토리 메서드 구현
        // 무명클래스 (익명 클래스)의 선언
        // 객체 이름을 통해 프로퍼티나 객체에 직접 접근할 수 있다!!!
//        Payment.pay.add(Person("",""))
//        Payment.cacluatePay(Person(""))
        CaseInsensitiveFileComparator.compare(File("/User"), File("/Library"))
        // 익명클래스에서도 object 사용가능
        // 상속의 의미가 존재한다!!
        // 클래스와 다르게 생성자가 존재하지 않는다
        setClickListener(object : ClickListener {
            override fun onClick() {

            }
        })

        // 14. companion object (동반객체)
        // 코틀린은 static을 지원하지 않는 대신 top-level function을 통해 같은 효과 낼 수 있다.
        // 이 function은 내부에 정의된 private 프로퍼티에는 접근 할 수 없다. --> 이를 개선 시켜줌!!!
        // 클래스의 내부정보에 접근할 수 있는 함수가 필요할 때 사용
        Companion.bar()
        Companion2.sleepy("두시")
        // 1. companion object 에 이름 명명
        // 2. companion object 내부에 확장함수나 property 정의
        // 3. 인터페이스 상속

        // ** 나이가 많은 한사람을 뽑는다.
        getPerson(listOf(Personn("김예현", 29), Personn("김예혀니", 30)))
        // maxByOrNull로 간단하게 구현 가능 - Collection의 확장 함수
        val peopleList = listOf(Personn("김예현", 29), Personn("김예혀니", 30))
        peopleList.maxByOrNull { it.age }

        // 15. 람다식 문법 ( 자바8부터 사용 가능
        // 1. 중괄호로 감싼다
        // 2. 인자와 본문은 -> 로 구분한다
        // 3. 인자는 ()로 감싸지 않는다.
        // 4. 인자는 형식추론이 가능하므로 타입 생략 가능
        // 5. 변수에 람다식을 담는 경우에는 인자의 타입 생략 가능

        peopleList.maxByOrNull({ p: Personn -> p.age })
        peopleList.maxByOrNull() { p: Personn -> p.age }
        peopleList.maxByOrNull { p: Personn -> p.age }
        peopleList.maxByOrNull { it.age }

        //함수의 맨 마지막 인자가 람다라면 () 안에서 빼내서 밖에 람다를 표현할 수 있다.
        //인자가 하나라면 그 인자는 람다식 내부에서 it으로 받을 수 있다.
        //인자가 하나이면서 그 인자가 람다타입 이라면 ()를 생략할 수 있다.

        // 15-1. 람다의 closure
        // 자바에서는 람다를 함수 내부에서 접근할 때 final로 선언해준다. - stack영역에 메모리 영역이 잡히고,
        // 함수가 끝나면 메모리 영역에서 해제되기 때문
        // Kotlin의 경우 해당 변수값을 복사해서 사용 lamda capturing이라 한다.

        // 15-2. 멤버 참조
        // 람다를 넘길 때 프로퍼티나 다른함수가 같은 signature를 가지고 있다면 간단하게 ::로 표현 가능
//        /////// ** 클래스의 멤버 표현 ( 클래스이름::멤버변수 )
//        peopleList.maxBy { Parent::age }
//        // ** 최상위 함수 표현 ( ::최상위함수 )
//        ::getPerson
//        /////// ** 변수에 람다대신 멤버 참조 저장 ( ::함수 )
//        val killer = { person: Personn -> Person("예혀니") }
//        val mulder = ::Person
//        /////// ** 생성자 ( ::클래스 )
//        /////// 생성자를 변수에 저장할 수 있고, 생성을 지연시킬 수 있다.
//        val personInit = ::Personn
//        val personData = personInit("김예현", 29)
//        /////// ** 확장함수 ( 클래스::함수 )
//        fun Personn.isAdult() = this.age > 10
//        val adultinit = Personn::isAdult


        // 16. Collection API
        // 1. filter
        // ( 람다식을 인자로 받아 조건에 따라 filtering 한다 )
        val list = listOf(2, 3, 4, 5, 6)
        val resultlist = list.filter { it % 2 == 0 }
        // 2. map
        // 원소를 원하는 형태로 변환 , 반환값 list
        val people = listOf(Personn("김예현", 29), Personn("예현", 30))
        val resultpeople = people.map { it.age > 29 }
        val maxAgePeople = people.maxByOrNull { it.age }!!.age
        val resultMaxAge = people.filter { it.age == maxAgePeople }
        // filterKey, mapKeys, filterValues, mapValues 제공!
        val numbers = mapOf(0 to "zero", 1 to "one")
        println("test16 numbers : " + numbers.mapValues { it.value.toString().toUpperCase() })
        // 3. all : collection 전체가 주어진 조건을 만족하는지 판단, 반환값 boolean
        val canBeInClub27 = { p: Personn -> p.age > 29 }
        val peopleresult2 = people.all(canBeInClub27)
        // 4. any : collection 원수중에 하나라도 주어진 조건을 만족하는지 판단, 반환값 boolean
        // 5. count : 주어진 조건을 만족하는 원소의 갯수 반환, 반환값 int
        // 6. find : 주어진 조건에 만족하는 첫번째 원소 반환, 반환값 t
        // 7. group by , 반환값 Map<Int, List<T>> --> 나이별로 묶인다.
        val peoplegroup = listOf(Personn("Alice", 31), Personn("Bob", 29), Personn("Carol", 31))
        println("test17 group : " + peoplegroup.groupBy { it.age })
        val alpabetgroup = listOf("a", "ab", "a")
        println("test17 group2 : " + alpabetgroup.groupBy(String::first))
        // 반환값 Map<String, List<String>>
        // 8. flatMap , flatten
        // flatMap : Map을 처리하고 난 다음의 결과가 list인 경우 이 list의 원소를 다시 펼쳐서 하나의 list로 만듬
        val strings = listOf("abc", "def")
        strings.flatMap { it.toList() } // list("abc") , list("def") --> list("a","b","c","d","e","f")
        // 9. Collection의 lazy execution
        // asSequence() --> 중간 연산결과 없이 연산을 한다, 최종 결과를 요청하는 시점에 모든 계산이 수행된다.
        listOf(1, 2, 3, 4).asSequence().map { println("asSequence test : " + "map($it)"); it * it }
            .filter { it % 2 == 0 }.toList()
        // 리스트 원소 하나씩 flow를 거친다. --> 원소의 수가 많을 때 메모리면에서 효율적이다

        // 17. 자바 functional interface
        // 코틀린에서는 이러한 함수 호출 시 람다식 사용 가능하다.

        post(1000, object : Runnable {
            override fun run() {
                println("test17 : " + "Functional Interface Test")
            }
        })
        // 호출할 때마다 Runnuable 생성된다 (비효율적)
        // 람다식으로 생성하면 한번만 객체 생성 후 재사용한다.
        // 내부적으로 람다식은 익명클래스로 치환된다.
        val runnuble = Runnable { println("test17 : " + "Functional Interface Test") }
        post(1000, runnuble)

        // lambda capturing이 발생하면 해당변수를 lambda에서 포함해야한다.
        // 이럴 경우에는 매번 객체가 생성되어 사용된다.
        handleComputation("test17")

        // 18. SAM (Single Abstract Method) 생성자 : 람다를 함수형 인터페이스로의 명시적 변형
        fun createAllDoneRunnable(): Runnable {
            return Runnable { println("test18 : return Runnuable") }
        }
        createAllDoneRunnable().run()
        // Runnuable 객체 자체를 반환하는 함수

        fun createAllDoneRunnable2(): Runnable {
            return object : Runnable {
                override fun run() {
                    println("test18 : return Runnuable")
                }
            }
        }
        // SAM 생성자 이용해 Runnable 반환!
        // ** 람다의 listsner 등록, 해제
        // 람다 내부에서 this를 사용하면 , 해당 this는 람다를 둘러싸고 있는 외부 객체를 가르킵니다.
        // 이와 다르게 익명클래스 내부에서는 this는 익명클래스 자체를 가리킵니다.
        // Event listner가 이벤트를 처리하고 나서 자기 자신을 해제해야 하는 경우 람다식이라면 this를 사용할 수 없다.
        // 이경우 익명클래스를 작성하여 this를 이용해 자기 자신 해제하도록 한다.

        // 19. 수신객체지정 람다 (apply, with)
        // with
        // with 파라미터로 넘겨준 객체는 람다내부에서 this로 접근해 사용 가능 , 마지막 return 값이 람다 값이 된다.
        println("test19 with  : " + alpabetOut())
        // apply
        // with과 거의 동일한 기능이지만, 차이점이 있다.
        // 1. 객체의 확장함수로 동작한다 2. return값은 객체 자신이다 (람다 내부의 마지막 값이 아님)
        println("test19 apply : " + alpabetOut2())

        // 20. null처리
        // 1. type 뒤에 ? 붙임으로써 null이 가능한 변수임을 명시한다.
        // 2. null safe operator : ?.
        // --> 앞의 변수가 null이 아닐 때만 오른쪽 함수가 실행되고 아니면 null이 반환된다.
        fun nulloper(s: String?) {
            val operator: String? = s?.toUpperCase()
            // s가 null이 아닐때만 대문자 처리 실행된다.
        }
        // property 접근시에도 ?. 사용
        employeemain()

        // 3. Elvis operator (?:)
        // null인 경우 default값을 지정하고 싶을 때 사용
        fun DuaLipa(song: String?) {
            val songname = song ?: "Unknown"
            println("test20-3 : " + songname)
        }
        DuaLipa("Physical")

        //21. safe cast
        //스마트 캐스트인 is를 이용하면 as를 사용하지 않고도 type변환 가능
        //as를 바로 사용하여 casting할 때 type이 맞지 않으면 classcastexception발생
        //--> as? : casting을 시도하고 casting이 불가능하면 null을 반환한
        Personsmart("김", "예현").firstname
        //22. 강제 not null처리
        // !! 연산자 : 강제로 null이 아님을 명시해준다.
        fun boombayaa(s: String?) {
            val notNull: String = s!!
        }

        //22. let 함수
        //not null인 경우에만 실행되도록 한다.
        //null인 경우에는 람다식 안의 구문이 실행되지 않는다.
        var email: String? = "into1023@naver.com"
        fun sendemail(sendmail: String?) {
            println("test22 : " + sendmail)
        }
        email = null
        email.let { sendemail(it) }
        // null로 값이 입력되며 실행된다.
        email?.let { sendemail(it) }
        // ?. 연산자 사용하여 let안 구문이 실행되지 않는다.
        // 중첩이 늘어나면 가독성이 떨어지므로 그럴땐 if구문을 사용하는 것이 효율적임

        //23. property의 초기화 지연
        //프로퍼티 초기화는 생성자에서 이루어져 생성자에서 초기화하면 끝이다.
        //프로퍼티 초기화는 생성자에서 이루어져 생성자에서 초기화하면 끝이다.
        //lateinit 연산자 : 나중에 초기화 할 수 있다.
        Myservice().service()

        //24. nullable type의 extension function
        //널이 가능한 객체에 확장함수를 선언할 수 있습니다.
        // ** String? type에서는 isNullOrBlank() 또는 isNullOrEmpty()함수 지원한다.
        fun userInput(user: String?) {
            if (user.isNullOrEmpty()) {
                println("test24 : not null")
            }
        }

        //25. Generic의 nullable
        // Generic을 사용하면 무조건 nullable로 인식된다.
        fun <T> genericfunction(t: T) {
            println("test25 : " + t?.hashCode())
        }

        // T에 ? 가 붙지 않았지만 , 이는 ? 붙은 것과 같다 --> 함수 내부에서 null check 반드시 필요
        fun <T : Any> genericfunction2(t: T) {
            println("test25 : " + t.hashCode())
        }
        // T는 Any의 상한제한을 갖기 때문에, T는 non null type이다


        // ** 코틀린의 타입은 컴파일 시 자바의 primitive 또는 wrapper 타입으로 변환됩니다.
        // ** coerceln() 범위를 한정하는 함수
        fun progress(cnt: Int) {
            var percent = cnt.coerceIn(0, 100)
            // 0 부터 100까지 제한
        }

        // 27. 널타입 Int?, Boolean?
        // 널이 가능한 타입은 primitiave type으로 변경이 불가능, 널이 불가능한 타입은 가능
        data class Personnn(val name: String, val age: Int?) {
            fun isOlder(other: Personnn): Boolean? {
                if (age == null || other.age == null) {
                    return null
                } else {
                    return age > other.age
                }
            }
        }
        println(Personnn("김예현", 29).isOlder(Personnn("예현", 30)))
        // 28. 숫자변환
        // 자동으로 숫자 변환시켜주지 않기 때문에 toByte(), toInt()... 등등 존재
        val numberInt: Int = 10
        val numberLong: Long = numberInt.toLong()
        // 29. Any, Any? 최상위 타입
        // 코틀린에는 Object가 없는 대신 Any, Any? 타입이 존재
        // Object타입처럼 모든 객체의 상위객체, Boolean/Int의 상위객체
        // * Any타입에는 null이 존재할 수 없으므로 null이 들어가야되는 곳에는 Any? 로 사용해야함
        // 자바의 Object와는 다르게 Any는 wait(), notify()는 제공하지 않는다 --> 사용하려면 Object로 캐스팅해야함

        // 30. Unit Type
        // void와 같은 역할을 한다
        // 제너릭타입에 명시 가능하며, return을 명시적으로 넣지 않아도 됨
        class noResultProcessor : test<Unit> {
            override fun testUnit() {
                println("Unit Test")
                // return 필요하지 않음
            }
        }

        // 31. Nothing Type
        // 함수가 정상적으로 끝나지 않는다는 걸 명시적으로 표현
        // return 타입이나 , 인자로만 쓸 수 있다.
        // Nothing은 아무것도 저장할 수 없으므로 변수로 사용이 불가능하다
        fun fail(message: String): Nothing {
            throw Throwable()
        }

        fun nothingtest() {
            fail("nothing")
        }

        // 32. Collection의 널처리
        // List<Int> : List와 원소 모두 null이 없다.
        // List<Int?> : List엔 null이 없고 원소에는 null이 있다.
        // List<Int>? : List엔 null이 있고, 원소에는 null이 없다.
        // List<Int?>? : List와 원소 모두 null이 있다.

        // 33. 자바 Collection과의 연결
        // 코틀린은 읽기 쓰기의 interface를 구분하지만 , 자바는 둘다 허용하기 때문에 자바의 collection은 MutableCollection을 상속받은 것으로 취급
        // Map 역시 Map과 MutableMap으로 구분된다.
        // 읽기전용: listOf, setOf, mapOf
        // 수정가능: mutableListOf, mutableSetOf, mutableMapOf (arrayListOf, hashSetOf, sortedSetOf, linkedSetOf, hashMapOf, linkedMapOf,sortedMapOf)
        // ** 따라서 읽기전용인 Collection을 자바에 넘기더라도, 이는 자바에서 변경 가능함을 염두해 두어야 한다.
        // 또한 원소가 null이 아닌 타입으로 지정하였으나, 자바에서 이 collection을 받아 null을 넣을 수 있다는 점도 주의해야 한다

        // 34. 객체의 배열과 원시 타입의 배열
        // 1. arrayOf(원소1, 원소2..)
        // 2. arrayOfNulls (개수) : 해당 개수 만큼 null을 넣어 배열 생성
        // 3. Array(개수, 생성식-lambda) : 해당 개수 만큼 람다를 이용해서 배열 생성
        fun arrayfunction(args: Array<String>) {
            for (i in args.indices) { // 배열의 인덱스를 읽음
                println("test34 Arrays : $i" + args.get(i))
            }
        }

        val arrays = listOf("a", "b", "c")
        arrayfunction(arrays.toTypedArray()) // 컬렉션을 배열로 변환
        // forEachIndexed() 확장함수 : 각 인덱스와 값을 가져올 수 있다.
        fun arrayIndex(args: Array<String>) {
            args.forEachIndexed { index, s ->
                println("test34 Arrays Index : " + index + ":" + s)
            }
        }

        // 35. 이항 산술 연산자 오버로딩
        // 산술 연산자를 코틀린에서는 overriding해서 사용 가능
        // operator 키워드로 함수 앞에 붙이고 연산자 사용
        // + : plus , - : minus , * : times , / : div , % : rem
        println("test 35 1: " + plusPoint(10, 20))
        // operator 함수도 오버로딩이 가능하기 때문에 같은 이름에 파라미터 타입이 서로 다른 연산자 함수를 여러개 만들 수 있다.
        // 비트 연산자는 오버로딩이 불가능, 중위함수 제공
        // << : shl , >> : shr , >>> : ushr , & : and , | : or , ^ : xor , ~ : inv
        val initpp = timePoint(10, 20)
        val pp = initpp * 1.5
        println("test 35 2: " + pp)
        println("test 35 3: " + mixPoint(10, 20))

        // 36. 복합연산자
        // =+ : plusAssign ... minusAssign, timeAssign, divAssign..
        // plus와 plusAssign을 동시에 구현하면 컴파일 오류 발생

        // 37. 단항연산자
        // 단항연산자 역시 overriding이 가능하며, 인자가 없다
        val shortp = mixPoint(30, 30)
        println("test 37 1: " + -shortp)
        var bd = BigDecimal.ZERO
        println("test 37 2: " + bd++)
        println("test 37 3: " + ++bd)

        // 38. equals
        // == 는 equal()로 치환된다. != 역시 마찬가지이다.
        // a == b -> a?.equals(b)?:(b==null) ---> a와 b가 둘다 null이면 true를 반환
        // equals는 Any안에 operator키워드가 붙어 구현되어 있어 하위 클래스에서는 override 키워드 이용해서 == 와 치환할 수 있다.

        // 39. compareTo
        // Comparable의 compareTo 함수 호출
        // a >= b -> a.compareTo(b) >= 0
        // compareValueBy(객체1, 객체2, 비교조건1, 비교조건2) : 두개의 조건을 우선순위 비교조건에 따라 처리
        // 1. 두 객체 비교 : equals -> 0 , 2. 비교 조건 1 사용 -> 0이 안나올 때까지 비교 , 3. 만약 비교조건1이 모두 0이라면 비교조건2 사용 -> 0이 안나올 때까지 비교
        val p1 = comparePerson("Alice", "Smith")
        val p2 = comparePerson("Jay", "Park")
        val resultp = p1 < p2
        println("test 39 : " + resultp)

        // 40. Collection과 range의 convention
        // 1. Index operator
        // 배열은 array[index] 형태로 접근하며, collection에서는 get/set으로 index에 접근한다.
        val bye = Bye(10, 20)
        println("test 40-1 : " + bye[0])
        var my = My(10, 20)
        my[0] = 20
        my[1] = 10
        println("test 40-2 : " + my.toString())

        // 2. In convention
        // a in b == a.contains(b)
        val containTest = Two(One(10, 20), One(50, 50))
        val one1 = One(20, 30) in containTest
        val one2 = One(5, 5) in containTest
        println("test 40-3 : " + one1)
        println("test 40-3 : " + one2)

        // 3. range to convention
        // .. 연산자이며 rangeTo 함수로 표현된다.
        // start .. end == start.rangeTo(end)
        Range()

        // 4. iterator for loop
        // for문에서 in 연산자를 이용해 loop를 실행한다.
        // in은 iterator 함수와 연결된다.
        // in 연산자가 iterator의 next() 와 hasNext() 로 이루어진다.

        // 5. Destructing declaration과 component함수
//        val mp = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")
//        printEntries(mp)
        val p = Print(10, 20)
        val (x, y) = p
        println("test 40-5 : " + x)
        println("test 40-5 : " + y)
        // val (x,y) = p
        // val x = p.component1 , val y = p.component2

        // 6. property의 위임
        // property의 위임이란 property에 대한 get(), set() 동작을 특정 객체가 처리하도록 위임하는 것을 말한다.
        // 이를 이용하면 property의 값을 field가 아니라 db table이나 브라우저의 세션, 맵등에 저장 가능
        // by를 이용해서 위임한다.
//        class Foo {
//            var p: type by Delegate()
//        }

        // 7. by lazy를 이용한 초기화 지연
        // property 중에 초기화를 미뤄야 하는것들이 존재한다.
        // 예를 들면 데이터를 네트워크나, DB에서 읽어와 사용하고 한번만 사용하면 읽어와서 사용하는 경우 아래와 같이 코드를 구성할 수 있다
        val pe = PersonEmail("Alice")
        println("test 40-7 : " + pe.emails)
        println("test 40-7 : " + pe.emails)
        // by lazy를 이용하면!!!!!!!!
        // 1. 한번만 초기화 한다
        // 2. 초기화 시 람다 구문이 사용된다
        // 3. Thread-safe 하다
        // 4. lock이 따로 필요하다면 넘길 수 있다
        // 5. Thread-safe할 필요가 없다면 없게 할 수 있다.

        // 8. Delegation property with observing pattern
        // Property가 변경될 때 UI에 변경을 알리거나, 다른 곳에 알림을 주기 위해 PropertyChangeSupport와 PropertyChangeEvent 클래스를
        // 사용하는 경우가 많다.
        // 기본적으로는 event 발생 시 subscriber에게 notify 해주는 observer pattern 이다.


        // 41. 고차함수
        // * 매개변수로 함수를 전달 받거나 함수를 반환하는 함수를 말한다.
        // 고차함수란 함수의 인자나 반환값이 lambda인 경우를 말한다.
        // 예를 들면 list의 filter나 map은 인자로 람다를 받는다. 이 둘은 고차함수이다.
        // 함수타입은 생략할 수 있으나, 명시하려면 (인자1:타입, 인자2:타입) 반환타입으로 표기할 수 있다.
//        val returnNull = (Int, String) -> String? = { null }
        val lambdaNull: ((Int, String) -> String)? = null
        // 함수 타입에 인자를 넣더라도 컴파일시 무시된다. 하지만 인자가 있음으로 가독성을 높일 수 있다.( 타입의 인자와 사용된 인자의 이름 달라도 상관없음)
        // 41-1. 인자로 받은 함수 호출
        yayayaya(10, { x -> x * x })
        // 41-2. 자바에서 코틀린 고차 함수 호출
        // 자바에서 코틀린의 함수를 호출할 때 인자를 함수 타입으로 넣어야한다면, 이를 대신할 인터페이스를 구현하는 객체 넣어야한다.
        // interface의 이름은 FuctionN<N1,N2...N,R>이며 invoke 함수하나만을 추상으로 갖는다
        // Function0<R> : 인자가 없을때
        // Function1<T,R>: 인자가 하나일때
        // Function2<T,V,R>: 인자가 두개일때
        // FunctionN<N1,N2...,R> 인자가 N개일때

        // 42. lambda인수에 default값 지정
        // 함수타입 인자로 기본값 지정할 수 있다.

        // 43. 함수를 반환
        // 인자외에 return값에 함수 타입을 반환하도록 할 수도 있다.
        val caculate = getShippingCoast(Delivery.EXPEDITED)
        println("test43 : ${caculate(Order(3))}")
        // 44. 람다를 이용한 중복제거
        println("test44 : " + averageWindowDuration)
        // top level function 수정
        println("test44 : " + log.averageWindowTopLevel(OS.WINDOWS))
        println("test44 : " + log.averageWindowTopLevel(OS.MAC))

        // 45. inline function
        // 참고 : https://thdev.tech/kotlin/2020/09/29/kotlin_effective_04/
        // ****** 코드 라인 자체가 안으로 들어간다는 뜻, 함수의 내용을 통해 호출하는 것이 아닌 , 호출하는 코드 자체가 함수 내용의 코드가 되는 효과있음
        // 람다를 사용하면 컴파일러가 내부적으로 익명 class로 치환하여 객체를 생성, 이는 java 1.6과 호환하기 위함이며,
        // 성능저하를 위해서 한번만 생성하여 재사용하는 방법등을 이용하여 성능에 신경쓴다.
        // 하지만 적어도 한번은 객체를 생성해야한다는 부담이있어 이럴 경우 inline function 사용한다.
        // inline function 은 컴파일 시 해당함수의 코드가 호출된 부분으로 그대로 들어가 바이트 코드로 변경된다.
        // 45-1. lnline function limitation
        // inline으로 사용하려는 함수가 함수타입 인자를 바로 실행하지 않고, 어딘가에 저장해 놓았다가 사용한다면 나중에 그 변수를 사용할 경우에는
        // inline 함수로 만들 수 없다.
        // ex ) list 를 opration할 때 sequence로 만들 경우  filter, map 함수
        // 여러 인자를 받을 때 람다만 inline하고 싶지 않다면 , noinline키워드를 붙이면 된다.

        val password: String = "your password"
        val password2 = Password("your password")

        // 46. 람다 내부의 return
        // 람다 내부에서 return을 사용하면 람다뿐만 아니라 외부의 function까지도 종료된다. ===> non-local return (자신의 외부함수까지 종료)
        // 람다 내부에 return쓸 수 있는건 inline 함수 뿐이다.

        // 47. label을 이용한 local return
        // 람다내부에 label을 사용한다면 람다내부에서만 빠져나올 수 있다.
        fun labelling(living: String) {
            living.forEach label@{ return@label }
        }

        // 48. Generic 함수
        // 코틀린은 반드시 타입을 정의하고 사용하여야한다.
        // 48-1. Generic함수와 프로퍼티
        // fun <T> List <T>.slice(indices: IntRange): List<T>
        // 호출할때 type 인자를 명시적으로 넣어도 되지만 넣지 않더라도 컴파일러가 알아서 타입을 추론합니다.
        val letters = ('A'..'Z').toList()
        println("test 48 : " + letters.slice(0..2))
        // 48-2. Generic Class
        // 자바에서는 super나 extends 사용해 사용한 타입을 제한할 수 있다.
        // 코틀린에서는 : 를 이용해 상한 type을 설정할 수 있다.
        // 자바 : < T extends Number > T sum(List<T> list)
        // 코틀린 : fun <T:Number> List<T>.sum() : T
        fun <T : Comparable<T>> max(first: T, second: T): T {
            return if (first > second) first else second
        }
        // 위 예제에서 T는 Comparable을 상한 타입으로 가지고 있다.
        // 즉 max함수는 Comparable을 구현하고 있어야한다. ---> String은 Comparable을 구현하므로 가능하다.
        println("test 48-2 : " + max("Kotlin", "KO"))

        // 48-3. Non-Null Type 파라미터 설정
        // 코틀린에서 타입의 기본은 Non null 이다. 다만 Generic인 경우에만 기본값이 Nullable이다.
        // class Friend<T> {
        //    fun getUnique(value: T) {
        //        value?.hashCode()
        //    }
        //}
        // val friend = Friend<Type>()
        // friend.getUnique(null) // 가능

        // 49. Generic의 런타임
        // 자바에서는 실행시점에 instance의 타입인자를 확인할 수 없다. 이는 JVM이 type erase하기 때문이다.
        // 다만 코틀린에서는 inline을 통해 이를 피할 수 있다.
        // 49-1. Generic의 run time
        // 클래스가 생성되어 instance가 되면 더이상 인자정보를 가지고 있지 않다.
        // 예를 들어 List<String> 을 객체로 만들었다면, 이는 List 타입만 알 뿐 내부에 저장된 원소의 type은 알 수 없다.
        // 이미 컴파일러가 type에 맞는 원소만 담고있을거라는 가정하여 run time에서는 이미 맞는 type만 들어있다고 생각한다.
        // if(value is List<String>) ---> List<String> 일때만 문제없고, List<Int> 라면 컴파일 실패한다.
        // 원소의 타입과 상관없이 List인지 아닌지 구분하려면 '*' 연산자 사용.
        fun printSum(c: Collection<*>) {
            val intList = c as? List // 여기서 warning 발생
                ?: throw IllegalArgumentException("List is Exception")
        }
        // 인자가 두개이상이라면 * 를 두개이상 표현
        // 49-2. reified로 타입 실체화
        // 인라인 함수를 이용하면 type 정보를 남길 수 있다.
        println("test 49-2 : " + isA<String>("abc"))
        println("test 49-2 : " + isA<Int>(123))
        // filterIsInstance<String>() 함수 사용하면 특정 타입의 인자만 분리해냄
        // 내부적으로 for문을 돌면서 if(element is T)를 체크한다.
        val listlist = listOf("a", 1, "b")
        println("test 49-2 : " + listlist.filterIsInstance<Int>())

        // 49-3. 실체화한 타입으로 클래스 참조
        // class를 인자로 넣어야 하는 경우
        // MainActivity.class ---> 코틀린 : MainActivity::class.java
        // reified로 표현하면
        println("test 49-3 : " + startActivity<MainActivity>())
        // *** Reified 사용가능/불가능
        // ** 사용가능 case
        // 1. type 검사와 캐스팅 (is, !is, as, as?)
        // 2. 추후 언급되는 코틀린 리플렉션API(::class)
        // 3. 코틀린타입에 대응하는 java.lang.Class 얻기 (::class.java)
        // 4. 다른 함수를 호출할 대 타입 인자로 사용
        // ** 사용 불가 case
        // 1. Type 파라미터 클래스의 인스턴스 생성
        // 2. Type 파라미터의 companion object method 호출
        // 3. reified 되지 않는 type을 받아 reified type을 받는 함수에 넘기기
        // 4. 클래스, property, inline 함수가 아닌 함수의 타입 파라미터를 reified로 지정하기


    }
}


// 1. 확장함수 this는 생략 가능하다
fun String.test1(): Char = this.get(this.length - 1)
var StringBuilder.lastChar: Char
    // 확장된 프로퍼티는 기본 getter를 꼭 구현해줘야한다!
    get() =
        get(length - 1)
    set(value: Char) {
        this.setCharAt(length - 1, value)
    }

// 2. 가변인자
fun test2(vararg name: String, age: String) {
    println("test2 : " + name.toString())
    println("test2 : " + age)
}

// 3. 중위함수
// 특정 두수를 곱한다.
infix fun Int.multiplex(num: Int): Int = this * num
infix fun Any.to(other: Any) = Pair(this, other)

// 6. 중첩함수
class User(val user: String, val email: String)

fun saveUser(user: User) {
    user.checkValiable()
}

fun User.checkValiable() { // User의 확장함수로 정의
    fun check(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw Exception("Empty value")
        }
    }

    // 확장 함수로 자신을 감싼 외부 함수의 리소스에 바로 접근 가능
    check(user, "yehyune")
    check(email, "into1023@naver.com")
}

// 7. 주생성자와 초기화
class Cafe constructor(_name: String) {
    // constructor은 주생성자를 나타내기 위한 키워드이나, 생략가능하다.
    val name: String

    init {
        // 초기화 작업할 때 사용
        // 주생성자 파라미터에 default값 정의해도 무방하
        name = _name
    }
}

// 상속받을땐 부모의 생성자를 사용하겠단 의미로 () 붙인다.
open class Parent(val age: Int)
class ParentChild(val ages: Int) : Parent(0)

// 생성자 외부에 노출시키지 않으려면 private 붙인다.
class ParentChild2 private constructor()


// 8. 인터페이스
interface Users {
    val nickname: String
}

class PrivateUser(override val nickname: String) : Users
class SubScribeUser(val email: String) : Users {
    override val nickname: String
        get() = email.substringBefore("@")
}

class FacebookUser(val accountId: Int) : Users {
    override val nickname: String
        get() = getFacebookName(accountId)
}

fun getFacebookName(accountId: Int) = "fb:$accountId"


// 9. 한정자 제어
class LengthCounter {
    var counter: Int = 0
        private set
    // private 정의해 외부에서 접근 못하도록 한다.
}

// 11. data class
data class Client(val name: String, val postalCode: Int)

// 12. 클래스 위임
// https://medium.com/til-kotlin-ko/kotlin%EC%9D%98-%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%9C%84%EC%9E%84%EC%9D%80-%EC%96%B4%EB%96%BB%EA%B2%8C-%EB%8F%99%EC%9E%91%ED%95%98%EB%8A%94%EA%B0%80-c14dcbbb08ad 참고
interface A {}
class B : A {}

val c = B()

// C를 생성하고 , A에서 정의하는 B의 모든 메서드를 C에 위임한다.
class C : A by c

// 13. object (싱글톤)
object Payment {
    val pay = arrayListOf<String>()
    fun cacluatePay(person: Person) {
        for (person in pay) {

        }
    }
}

fun Person(vararg name: String) {}

// comparator는 여러개 구현할 필요가 없으므로 싱글톤으로 구현하는 것이 좋다.
object CaseInsensitiveFileComparator : Comparator<File> {
    override fun compare(
        file1: File,
        file2: File,
    ): Int {
        return file1.path.compareTo(file2.path, ignoreCase = true)
    }
}

// Person 객체를 정렬하는 Comparator를 Parent 객체 내부에 구현하고 싶다면 중첩된 class 형태로 구현 가능
//data class Person1(val name: String){
//    object comparatorPerson : Comparator<Person>{
//        override fun compare(o1: Person?, o2: Person?): Int {
//            return o1.name.compareTo(o2.name)
//        }
//    }
//}
// 익명 클래스를 구현할 때도 object를 사용한다!!!
interface ClickListener {
    fun onClick()
}

fun setClickListener(clickListener: ClickListener) {
    clickListener.onClick()
}

// 14. companion object
// 클래스 내부에 static 정의와 같다
// private 프로퍼티라도 접근 가능하다!!
class Companion {
    companion object companiontest {
        fun bar() {
            println("Companion obejct test")
        }
    }
}

class Companion2 private constructor() {
    companion object {
        fun sleepy(time: String) {
            println("Companion2 private property")
        }
    }
}

interface JSONFactory<T> {
    fun fromJson(jsonText: String): T
}

class Person(val name: String) {
    companion object : JSONFactory<String> {
        override fun fromJson(jsonText: String): String {
            TODO("Not yet implemented")
        }
    }
}

// 13. 람다 문법
data class Personn(val name: String, val age: Int)

fun getPerson(people: List<Personn>) {
    var oldPerson: String? = null
    var maxAge = 0
    for (init in people) {
        if (init.age > maxAge) {
            maxAge = init.age
            oldPerson = init.name
        }
    }

    println("Old Person : " + oldPerson)
}


// 17. function interface 호출
fun handleComputation(msg: String) {
    post(1000, println(msg))
}

fun post(time: Int, msg: Unit) {}
fun post(time: Int, runnuble: Runnable) {}

// 19. 수신객체지정 람다 (with)
fun alpabetOut(): String {
    var resultBuffer = StringBuilder()
    return with(resultBuffer) {
        for (alpabet in 'A'..'Z') {
            this.append(alpabet)
        }
        this.toString()
    }
}

// apply
fun alpabetOut2() = StringBuilder().apply {
    for (alpabet in 'A'..'Z') {
        this.append(alpabet)
    }
}.toString()

///// buidString : StringBuilder를 생성하고, toString()를 자동 호출합니다.
fun alpabetOut3() = buildString {
    for (letter in 'A'..'Z') {
        this.append(letter)
    }
}

// 20. null 처리
// ?. 연산자
class Employee(val name: String, val employeee: Employee?)

fun employeefun(employee: Employee): String? = employee.employeee?.name
fun employeemain() {
    val ceo: Employee = Employee("예혀니", null)
    val ceoin: Employee = Employee("효니", ceo)
    println("test20-2 : " + employeefun(ceo))
    println("test20-2 : " + employeefun(ceoin))
}

// 21. smart cast
class Personsmart(val firstname: String, val lastName: String) {
    override fun equals(other: Any?): Boolean {
        val otherPerson = other as? Personsmart ?: return false
        // java version
//        Personsmart o = null;
//        if(o instanceof Personsmart){
//            o = (Personsmart)o;
//        }else{
//            return false;
//        }

        return otherPerson.firstname == firstname &&
                otherPerson.lastName == lastName
    }
}

// 23. property 초기화 지연
// 초기화 이전에 접근한다면 에러가 발생한다.
class Myservice {
    fun service(): String = "myservice"
}

class MyTest {
    private lateinit var myservice: Myservice

    fun setup() {
        myservice = Myservice()
        myservice.service()
    }

    val beforeservice: String? = "beforeservice"
}

//30. Unit Type
interface test<T> {
    fun testUnit(): T
}

//35. 산술 연산자
data class plusPoint(val num1: Int, val num2: Int)

operator fun plusPoint.plus(other: plusPoint): plusPoint {
    return (plusPoint(num1 + other.num1, num2 + other.num2))
}

// 연산자의 타입이 다르고 return값 다르게 설정해도 문제없이 실행된다.
val initp = timePoint(10, 20)
val p = initp * 1.5
// 교환 법칙이 성립하므로 1.5 * initp 일 경우
// operator fun Double.times(time: timePoint) : timePoint{ ... }  로 선언해야 한다.

data class timePoint(val num1: Int, val num2: Int)

operator fun timePoint.times(time: Double): timePoint {
    return (timePoint(num1 * time.toInt(), num2 * time.toInt()))
}

// 36. 복합 연산자
data class mixPoint(val num1: Int, val num2: Int)

fun mixPoint.plusAssign(mix: mixPoint): mixPoint {
    return mixPoint(num1 + mix.num1, num2 + mix.num2)
}

// 37. 단항 연산자
operator fun mixPoint.unaryMinus(): mixPoint {
    return mixPoint(-num1, -num2)
}

operator fun BigDecimal.inc() = this + BigDecimal.ONE

// 39. compareTo
class comparePerson(val firstname: String, val lastName: String) : Comparable<comparePerson> {
    override fun compareTo(other: comparePerson): Int {
        return compareValuesBy(this, other, comparePerson::firstname, comparePerson::lastName)
    }
}

// 40. Collection 과 range의 convetion
// 40-1. index operator
data class Bye(val x: Int, val y: Int)
data class My(var x: Int, var y: Int)

operator fun Bye.get(index: Int) {
    when (index) {
        0 -> x
        1 -> y
        else ->
            throw IndexOutOfBoundsException("indexOutException")
    }
}

operator fun My.set(index: Int, value: Int) {
    when (index) {
        0 -> x = value
        1 -> y = value
        else -> throw IndexOutOfBoundsException("indexOutException2")
    }
}

// 40-2. in convention
data class One(val x1: Int, val y1: Int)
data class Two(val x2: One, val y2: One)

operator fun Two.contains(one: One): Boolean {
    return one.x1 in x2.x1 until y2.x1 && one.y1 in y2.y1 until y2.y1
}

// 40-3. range To convention
fun Range() {
    val n = 9
    println("test 40-4 : " + 0..(n + 1).toString())
    (0..n).forEach { println("test 40-4 : " + it) }
}

//// 40-4. iterator for loop
//operator fun ClosedRange.iterator(): Iterator<String> = object : Iterator<String> {
//    var current = start
//    override fun hasNext() =
//        current <= endInclusive
//
//    override fun next() = current.apply { current = plusDays(1) }
//}

// 40-5. Destucting declaration 과 component 함수
//fun printEntries(map: Map) {
//    for ((key, value) in map) {
//        println("$key -> $value")
//    }
//}

data class Print(val x: Int, val y: Int)

// 40-7. by lazy를 이용한 초기화 지연
class Email {}

class PersonEmail(val name: String) {
//    var _email: List<String>? = null
//    val email: List<String>?
//        get() {
//            if (_email == null) {
//                _email = loadEmail(this)
//            }
//            return _email
//        }


    // by lazy()로 한줄로 바꿀 수 있다.
    val emails by lazy { loadEmail(this) }
}

fun loadEmail(person: PersonEmail): List<String>? {
    println("test 40-6 : " + "Load Email : ${person.name}")
    return listOf()
}

// 40-1. 고차함수
// 자바에서 사용법 참고
fun yayayaya(x1: Int, argFun: (Int) -> Int) {
    val result = argFun(10)
}

// 42. lambda인수에 default값 지정
//fun Collection<List<String>>.joinToString(
//    separator: String = "",
//    prefix: String = "",
//    postfix: String = "",
//)

// 43. 함수 반환
enum class Delivery { STANDARD, EXPEDITED }
class Order(val itemCount: Int)

fun getShippingCoast(delivery: Delivery): (Order) -> Double {
    if (delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount }
    }

    return { order -> 1.2 * order.itemCount }
}

data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS,
)

enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }

val log = listOf(SiteVisit("/", 34.0, OS.WINDOWS),
    SiteVisit("/", 22.0, OS.MAC),
    SiteVisit("/login", 12.0, OS.WINDOWS),
    SiteVisit("/signup", 8.0, OS.IOS),
    SiteVisit("/", 16.3, OS.ANDROID))

val averageWindowDuration = log.filter { it.os == OS.WINDOWS }.map(SiteVisit::duration).average()

// top-level function으로 수정
fun List<SiteVisit>.averageWindowTopLevel(os: OS) =
    log.filter { os == it.os }.map(SiteVisit::duration).average()

// 45. inline class, function
inline class Password(val value: String)

// 49-2. inline Generic reified
inline fun <reified T> isA(value: Any) = value is T // 컴파일 가능

// 49-3. 실체화한 타입으로 클래스 참조
inline fun <reified T : Activity> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}