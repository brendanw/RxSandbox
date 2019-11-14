package rx

import async.KSExecutorService
import async.PausableThreadPoolExecutor
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.rxkotlin.subscribeBy

val computation = Schedulers.from(KSExecutorService.newComputationExecutor(2))
val io = Schedulers.from(PausableThreadPoolExecutor.newCachedThreadPool())

fun main(args: Array<String>) {
  // Create a list of fish
  val fishList: List<String> = listOf("dory", "clownfish", "tiger shark", "seahorse", "great white shark")

  // Create a stream that emits each item from the list
  var itemStream: Observable<String> = Observable.fromIterable(fishList)
  itemStream.subscribeBy(onNext = {
        println(it)
      })

  println("\n================\n")

  // Create a stream that emits one item: the list of fish
  val listStream: Observable<List<String>> = Observable.just(fishList)
  listStream.subscribeBy(onNext = {
    println(it)
  })

  println("\n================\n")

  // Convert item stream to all uppercase items
  itemStream = Observable.fromIterable(fishList)
  itemStream.map {
    it.toUpperCase()
  }.subscribeBy(onNext = {
    println(it)
  })

  // Keeps the main thread alive indefinitely
  Thread.currentThread().join()
}
