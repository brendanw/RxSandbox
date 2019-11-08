package codility

fun main(args: Array<String>) {
    println(gcd(4, 4))
    println(gcd(8, 4))
    println(gcd(24, 9))
    println(gcd(25, 24))
}



fun gcd(a: Int, b: Int): Int {
    if (a == b) return a
    if (a > b) return gcd(a - b, b)
    if (b > a) return gcd(a, b - a)
    return 0
}