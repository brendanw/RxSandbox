package coroutines

import hu.akarnokd.kotlin.flow.publish
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import java.util.*

@UseExperimental(InternalCoroutinesApi::class)
fun main(args: Array<String>) {
  GlobalScope.launch {
    conflatedBroadcastChannelTest()
  }

  while (true) {

  }
}

private val confChannel = ConflatedBroadcastChannel<BEvent>()

fun Flow<BEvent>.loadShops(): Flow<BEvent> {
  return flatMapLatest { event ->
    flow {
      println("network request ${event.javaClass.simpleName}")
      delay(2000)
      emit(event)
    }
  }.flowOn(Dispatchers.IO)
}

@InternalCoroutinesApi
suspend fun conflatedBroadcastChannelTest() {
  GlobalScope.launch {
    confChannel.asFlow()
        //.onEach { println("${it.javaClass.simpleName} emitted") }
        .publish { multicastFlow ->
          merge(
              multicastFlow.filterIsInstance<BEvent.NationEvent>().loadShops()
                  .onEach { println("receive response >>>>> ${it.javaClass.simpleName}") },
              multicastFlow.filterIsInstance<BEvent.CityEvent>().loadShops()
          )
        }
        .collect {
          //println(it)
        }
  }

  GlobalScope.launch {
    var counter = 0
    while (true) {
      delay(kotlin.random.Random.nextLong(0, 5) * 1000)
      counter++
      when (kotlin.random.Random.nextInt(0, 3)) {
        0 -> confChannel.send(BEvent.NationEvent)
        1 -> confChannel.send(BEvent.CityEvent)
        2 -> confChannel.send(BEvent.SchoolEvent)
      }
      //println("emit $counter")
    }
  }
}

sealed class BEvent {
  object CityEvent : BEvent()
  object SchoolEvent : BEvent()
  object NationEvent : BEvent()
}

fun <T> merge(vararg flows: Flow<T>): Flow<T> = flowOf(*flows).flattenMerge()
