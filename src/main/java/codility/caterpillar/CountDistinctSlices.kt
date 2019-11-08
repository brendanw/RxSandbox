package codility.caterpillar

fun main(args: Array<String>) {
    println(newSol(5, intArrayOf(3, 4, 5, 5, 2))) //9
    println(newSol(5, intArrayOf(3, 4, 5, 5, 2, 2, 3))) //12
    println(newSol(10, intArrayOf(1, 2, 3, 4, 5, 5, 5, 6, 7))) //22
    println(newSol(10, intArrayOf(10, 10, 10, 3, 3, 3, 2, 2, 2))) //11
    println("---------")
    println(newSol(5, intArrayOf(1, 2, 3, 4, 2, 2, 5))) // 16
    println(newSol(5, intArrayOf(1, 2, 3, 4, 2, 2, 5, 1))) // 19
    println(newSol(5, intArrayOf(1, 2, 1, 2, 1, 1, 2, 2, 3))) // 15
}

fun newSol(M: Int, A: IntArray): Int {
    val accessed = IntArray(M + 1) { -1 }
    var back = 0
    var front = 0
    var count = 0
    for (i in 0 until A.size) {
        front = i
        if (accessed[A[front]] == -1) {
            accessed[A[front]] = front
        } else {
            //have seen before
            var newBack = accessed[A[front]] + 1
            // numDistinctSeqs items from old back to newBack
            count += ((numDistinctSeqs(front - back)) - numDistinctSeqs(front - newBack))
            if (count > 1_000_000_000) return 1_000_000_000
            for (j in back until newBack) {
                accessed[A[j]] = -1
            }
            accessed[A[front]] = i
            back = newBack
        }
    }
    count += numDistinctSeqs(front - (back - 1))
    if (count > 1_000_000_000) return 1_000_000_000
    return count
}

fun numDistinctSeqs(length: Int): Int {
    return (length * (length + 1)) / 2
}

fun oldSol(M: Int, A: IntArray): Int {
    val accessed = IntArray(M + 1) { -1 }
    var back = 0
    var front = 0
    var count = 0
    for (i in 0 until A.size) {
        front = i
        if (accessed[A[front]] == -1) {
            accessed[A[front]] = front
        } else {
            //have seen before
            var newBack = i
            // numDistinctSeqs items from old back to newBack
            count += numDistinctSeqs(newBack - back)
            for (j in back until newBack) {
                accessed[j] = -1
            }
            accessed[A[front]] = i
            back = newBack
        }
    }
    count += numDistinctSeqs(front - (back - 1))
    return count
}