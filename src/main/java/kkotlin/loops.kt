package kkotlin

fun main(args: Array<String>) {
    println("45".toInt())
    val chunkPaths = arrayOf("delta-4", "cap-3", "beta-2", "alpha-1")

    chunkPaths.sortWith(Comparator { o1, o2 ->
        o1.takeLast(1).toInt() - o2.takeLast(1).toInt()
    })

    println(chunkPaths.toList())
}