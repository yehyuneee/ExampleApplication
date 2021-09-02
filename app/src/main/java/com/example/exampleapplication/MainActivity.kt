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
                TODO("Not yet implemented")
            }
        })

        // 14. companion object (동반객체)
        // 코틀린은 static을 지원하지 않는 대신 top-level function을 통해 같은 효과 낼 수 있다.
        // 이 function은 내부에 정의된 private 프로퍼티에는 접근 할 수 없다. --> 이를 개선 시켜줌!!!
        // 클래스의 내부정보에 접근할 수 있는 함수가 필요할 때 사용
        Companion.bar()

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
class Companion {
    companion object companiontest {
        fun bar() {
            println("Companion obejct test")
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