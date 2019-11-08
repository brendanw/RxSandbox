package codility

fun main(args: Array<String>) {
}

fun binarySearch(A: IntArray, target: Int): Int {
    var start = 0
    var end = A.size - 1
    var result = -1
    while (start <= end) {
        val mid = (start + end) / 2
        if (A[mid] <= target) {
            start = mid + 1
            result = mid
        } else {
            end = mid - 1
        }
    }
    return result
}