package flow

import javafx.application.Application.launch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

suspend fun <T, R> Flow<T>.mymap(transform: suspend (value: T) -> R): Flow<R> {
  return flow {
    this@mymap.collect { item ->
      emit(transform(item))
    }
  }
}

fun main(args: Array<String>) {
  GlobalScope.launch {
    flow {
      emit(1)
      emit(2)
      emit(3)
    }.map { it + 1 }

    flowOf(1, 2, 3).mymap {
      it + 1
    }.collect { item ->
      println(item)
    }
  }
  while(true) {

  }
}

