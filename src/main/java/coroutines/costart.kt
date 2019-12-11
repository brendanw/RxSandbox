package coroutines

import javafx.application.Application.launch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis


val ints: Flow<Int> = flow {
  for (i in 1..10) {
    delay(100)
    emit(i)
  }
}

fun <T> Flow<T>.buffer(size: Int = 0): Flow<T> = flow {
  coroutineScope {
    val channel = produce(capacity = size) {
      collect {
        send(it)
      }
    }
    channel.consumeEach {
      emit(it)
    }
  }
}

fun example1() {
  GlobalScope.launch {
    //trial 1
    val time = measureTimeMillis {
      ints.collect { println(it) }
    }
    println("collected in $time ms")

    // trial 2
    val time2 = measureTimeMillis {
      ints.collect {
        delay(100)
        println(it)
      }
    }
    println("collected in $time2 ms")

    // trial 3
    val time3 = measureTimeMillis {
      ints.buffer().collect {
        delay(100)
        println(it)
      }
    }
    println("Collected in $time ms")
  }
}

fun main(args: Array<String>) {
  //example1()
  val testFlow: Flow<Int> = flow {
    emit(22)
  }
  GlobalScope.launch {
    testFlow.collect { result ->
      println(result)
    }
  }
  while(true) {

  }
}
