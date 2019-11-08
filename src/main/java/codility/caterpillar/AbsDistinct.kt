package codility.caterpillar

import java.util.*
import kotlin.math.abs

fun main(args: Array<String>) {
    var input = intArrayOf(-2_147_483_648, -1, 0, 1)
    println("${solution(input)} vs ${sol(input)}")
    /*input = intArrayOf(-3, -2, -1, 0, 5)
    println("${solution(input)} vs ${sol(input)}")
    input = intArrayOf(-1, 0, 1, 2, 3, 4, 5)
    println("${solution(input)} vs ${sol(input)}")
    println("-----------")
    println(solution(intArrayOf(0))) // 1
    println(solution(intArrayOf(-1, 1))) // 1
    println(solution(intArrayOf(0, 1))) // 2
    println(solution(intArrayOf(-1, 0))) // 2
    println("-----------")
    println(solution(intArrayOf(-5, -3, -1, 0, 3, 6))) // 5
    println(solution(intArrayOf(0, 1, 2))) // 3
    println(solution(intArrayOf(-1, 0, 1, 2))) // 3
    println(solution(intArrayOf(-3, 1, 2))) // 3
    println("-----------")
    println(solution(intArrayOf(-5, -4, -3))) // 3
    println(solution(intArrayOf(-5, -5, -4, -3))) // 3
    println("--------------")
    input = intArrayOf(-6, -5, -1, 0, 2, 5, 6, 6, 6, 7)
    println("${solution(input)} vs ${sol(input)}")
    input = intArrayOf(-6, -5, -5, -4, -2, -2, -2, -1, 0, 2, 3, 5, 5, 6, 6, 6) //8
    println("${solution(input)} vs ${sol(input)}")
    input = generateInput()
    println("${solution(input)} vs ${sol(input)}")*/

}

fun generateInput(): IntArray {
    val random = Random()
    val arr = IntArray(1000) {
        val sign = if (random.nextBoolean()) 1 else -1
        random.nextInt(1000) * sign
    }
    arr.sort()
    return arr
}

fun solution(A: IntArray): Int {
    if (A.size == 1) return 1
    if (A[0] == Int.MIN_VALUE) return sol(A)
    var prevLeft: Int? =  null
    var prevRight: Int? = null
    var leftIndex = 0
    var rightIndex = A.size - 1
    var count = 0
    while(leftIndex <= rightIndex) {
        //neg vs pos
        if(A[leftIndex] >= 0 != A[rightIndex] >= 0) {
            if (abs(A[leftIndex]) == abs(A[rightIndex])) {
                if (prevRight != A[rightIndex] && prevLeft != A[leftIndex]) count++
                prevRight = A[rightIndex]
                prevLeft = A[leftIndex]
                leftIndex++
                rightIndex--
            } else if (abs(A[rightIndex]) > abs(A[leftIndex])) {
                if (A[rightIndex] != prevRight) count++
                prevRight = A[rightIndex]
                rightIndex--
            } else {
                if(A[leftIndex] != prevLeft) count++
                prevLeft = A[leftIndex]
                leftIndex++
            }
        }
        //both neg or both pos
        else {
            if(A[rightIndex] != prevRight) count++
            prevRight = A[rightIndex]
            rightIndex--
        }
    }
    return count
}

fun sol(A: IntArray): Int {
    val set = HashSet<Int>()
    for (a in A) {
        set.add(abs(a))
    }
    return set.size
}