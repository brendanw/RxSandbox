package codility.greedy

import java.util.*

fun main(args: Array<String>) {
    println(solution(4, intArrayOf(1, 1, 1, 4, 1, 1, 1, 4, 1, 1, 1, 4))) //3
}

/**
 * Return the maximum number of ropes that can exist greater than length K
 */
fun solution(K: Int, A: IntArray): Int {
    var count = 0
    var index = 0
    while (index < A.size) {
        if (A[index] >= K) {
            count++
            A[index] = -1
        }
        index++
    }
    index = 0
    var sum = 0
    while (index < A.size) {
        if (A[index] < 0) {
            sum = 0
            index++
        } else {
            sum += A[index]
            if (sum >= K) {
                count++
                sum = 0
            }
            index++
        }
    }
    return count
}