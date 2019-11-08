package codility.greedy

fun main(args: Array<String>) {
    println(solution(intArrayOf(1, 3, 7, 9, 9), intArrayOf(5, 6, 8, 9, 10)))
    println(solution(intArrayOf(1, 3, 7, 9, 9, 20), intArrayOf(5, 6, 8, 9, 10, 15)))
}

fun solution(A: IntArray, B: IntArray): Int {
    if (A.isEmpty()) return 0
    if (A.size == 1) return 1
    //count number of overlapping segments from the first segment. No way to do better than this by starting later
    //for each segment, see if this segment intersects the next segment. if so increment counter
    var counter = 0
    var leftPointer = 0
    while (leftPointer < (A.size - 1)) {
        var rightPointer = leftPointer + 1
        val seg1: Pair<Int, Int> = Pair(A[leftPointer], B[leftPointer])
        var seg2: Pair<Int, Int> = Pair(A[rightPointer], B[rightPointer])
        if (seg2.first > seg1.second) {
            // we do not have overlap, increment count
            counter++
        } else {
            // we have overlap, check next segment after this if there is one. if there is no next seg, return count
            var hasOverlap = true
            rightPointer++
            while(hasOverlap && rightPointer < B.size) {
                seg2 = Pair(A[rightPointer], B[rightPointer])
                if (seg2.first > seg1.second) {
                    counter++
                    hasOverlap = false
                } else {
                    rightPointer++
                }
            }
        }
        leftPointer = rightPointer
    }
    return counter + 1
}