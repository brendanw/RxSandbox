package kkotlin

fun main(args: Array<String>) {
  val mutableList = mutableListOf("alpha", "beta", "delta")
  println(mutableList)
  mutableList.add("gamma")
  mutableList[0] = "kivia"
  println(mutableList)
  mutableList.remove("alpha")
  println(mutableList)
}
