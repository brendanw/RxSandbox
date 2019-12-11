package flow

import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.coroutineContext

fun main(args: Array<String>) {

}

internal class BuildFlow {

  //Collector is basically an observer. instead of calling observer#onNext, we call flowCollector#emit
  interface FlowCollector<in T> {
    suspend fun emit(value: T)
  }

  interface Flow<out T> {
    public suspend fun collect(collector: FlowCollector<T>)
  }

  /**
   * Base class for stateful implementations of `Flow`.
   */
  abstract class AbstractFlow<T> : Flow<T> {
    override suspend fun collect(collector: FlowCollector<T>) {
      //collectSafely(SafeCollector(collector, collectContext = coroutineContext))
    }

    public abstract suspend fun collectSafely(collector: FlowCollector<T>)
  }
}
