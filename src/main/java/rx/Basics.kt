package rx

import io.reactivex.Observable
import io.reactivex.schedulers.*
import io.reactivex.rxkotlin.subscribeBy
import java.util.*

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

  println("\n================\n")

  // query information about items on a background thread
  itemStream = Observable.fromIterable(fishList)
  itemStream.flatMap {
    getFishData(it).subscribeOn(Schedulers.io())
  }.subscribeBy(onNext = {
    println(it)
  })

  // Keeps the main thread alive indefinitely
  Thread.currentThread().join()
}

data class FishData(val name: String, val color: String, val size: String)

fun getFishData(name: String): Observable<FishData> = Observable.fromCallable {
  val duration = Random().nextInt(5)
  // Wait 0-5 seconds (whatever is returned by duration)
  Thread.sleep(duration * 1000L)
  FishData(name = name, color = "Blue", size = "Small")
}
