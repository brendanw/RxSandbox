package real

/**
 * Problem is to remove duplicates without using a second data structure.
 * Time complexity: O(N) + O(N) = O(2N) = O(N)
 *  Input  : arr[] = {2, 2, 2, 2, 2}
    Output : arr[] = {2, _, _, _, _}
    new size = 1

    Input  : arr[] = {1, 2, 2, 3, 4, 4, 4, 5, 5}
    Output : arr[] = {1, 2, 3, 4, 5, _, _, _, _}

    Input : arr[] = {2, 3, 3, 3, 3}
                        P1
                           P2
    Output: arr[] = {1, 2, 3, 4, 5, 6, _, _, _}
 */

//From 2nd facebook phone screen
fun main(args: Array<String>) {
  val inputA = arrayOf(1, 1, 2, 2, 3, 3, 4, 4)
  printOutcome(inputA)
  val inputB = arrayOf(2, 2, 2, 2, 2)
  printOutcome(inputB)
  val inputC = arrayOf(2, 3, 3, 3)
  printOutcome(inputC)
  val inputD = arrayOf(2, 2, 3, 3, 3, 4, 5, 6, 6, 6, 6)
  printOutcome(inputD)
}

fun printOutcome(input: Array<Int>) {
  println("${distinct(input)}: ${input.asList()}")
}

fun distinct(input: Array<Int>): Int {
  var lastNum = -1
  var p1 = 0
  var p2 = 0
  while (p2 < (input.size - 1)) {
    if (input[p1] > lastNum) {
      lastNum = input[p1]
      p1++
    } else {
      while (p2 < (input.size - 1)) {
        p2++
        if (input[p2] > lastNum) {
          input[p1] = input[p2]
          lastNum = input[p1]
          p1++
          break
        }
      }
    }
    if (p2 < p1) p2 = p1
  }
  return p1
}
