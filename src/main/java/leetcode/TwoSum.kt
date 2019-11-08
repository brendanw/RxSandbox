package leetcode

fun main(args: Array<String>) {
    val list: List<String> = listOf("item1", "item2", "item3")
    for ((index, value) in list.withIndex()) {
        println("index=$index -> value=$value")
    }
    test()
}

fun test() {
    println("hello world")
}