package codility.greedy

fun main(args: Array<String>) {
    println(greedyCoinChanging(1, 3, 4, amountToBePaid = 6))
}

fun greedyCoinChanging(vararg denominations: Int, amountToBePaid: Int): List<Pair<Int, Int>> {
    val n = denominations.size
    val list = ArrayList<Pair<Int, Int>>()
    var k = amountToBePaid
    for (i in (n - 1) downTo 0) {
        list.add(Pair(denominations[i], k / denominations[i]))
        k %= denominations[i]
    }
    return list
}