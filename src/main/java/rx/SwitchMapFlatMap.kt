package rx

import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import java.util.*
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
  // flatMap implicitly contains the merge operator
  // useful for multiple network requests in parallel
  // subscribes to upstream observable and returns an observable
  //flatMapExample()

  switchMapExample()
  while (true) {

  }
}

private fun flatMapExample() {
  val race = ArrayList(listOf("Alan", "Bob", "Cobb", "Dan", "Evan", "Finch"))
  Observable.fromIterable(race)
      .flatMap { s ->
        val delay = Random().nextInt(5)
        Observable.just(s).map { it.toUpperCase() }
            .delay(delay.toLong(), TimeUnit.SECONDS)
      }
      .subscribeBy(onNext = {
        println(it)
      })
}

private fun switchMapExample() {
  val race = ArrayList(listOf("Alan", "Bob", "Cobb", "Dan", "Evan", "Finch"))
  Observable.fromIterable(race)
      .switchMap { str ->
        val delay = Random().nextInt(2)
        Observable.just(str).map { str.toUpperCase() }
            .delay(delay.toLong(), TimeUnit.SECONDS)
      }.subscribeBy(onNext = {
        println(it)
      })
}
