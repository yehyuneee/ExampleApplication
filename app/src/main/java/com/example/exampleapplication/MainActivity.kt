package com.example.exampleapplication

import android.app.Person
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.File
import java.lang.Exception
import java.lang.reflect.Array.set
import kotlin.Comparator
import java.util.Comparator as Comparator1

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
        fun <T : Any> genericfunction2(t:T){
            println("test25 : " + t.hashCode())
        }
        // T는 Any의 상한제한을 갖기 때문에, T는 non null type이다



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
