package rx

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import java.util.stream.IntStream


@Volatile private var count = 0

fun main(args: Array<String>) {
  val source: PublishProcessor<Int> = PublishProcessor.create()

  source.observeOn(Schedulers.computation())
      .subscribeBy(onNext =
          {
            obj: Int -> ComputeFunction.compute(obj)
          },
          onError = {
            obj: Throwable -> obj.printStackTrace()
          }
      )

  IntStream.range(1, 1000000).forEach { t: Int -> source.onNext(t) }

  while (true) {

  }
}

object ComputeFunction {
  fun compute(v: Int) {
    try {
      println("compute integer v: $v")
      Thread.sleep(1000)
    } catch (e: InterruptedException) {
      e.printStackTrace()
    }
  }
}
