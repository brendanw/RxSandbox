package rx

import async.KSExecutorService
import async.PausableThreadPoolExecutor
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Single
import io.reactivex.Flowable
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.subscribeBy
import java.util.*
import java.util.concurrent.TimeUnit

/** Every user tap, screen rotation, keyboard tap emits an event here */
private val eventEmitter: PublishRelay<Event> = PublishRelay.create()

sealed class Event (val name: String) {
  object TapEvent : Event(name = "TapEvent")
  object SearchEvent : Event(name = "SearchEvent")
}

sealed class Result {
  object TapResult : Result()
  object SearchResult : Result()
}

fun Observable<Event.TapEvent>.onTap(): Observable<Result> {
  return Observable.just(Result.SearchResult)
}

fun Observable<Event.SearchEvent>.onSearch(): Observable<Result> {
  return switchMap {
    Observable.defer { Observable.just(Result.SearchResult) }
  }
}


/*fun main(args: Array<String>) {
    eventEmitter
        .doOnNext { print("--- event fired: ${it.name}")}
        .publish { inputObservable ->
            Observable.merge(
                inputObservable.ofType(Result.TapResult::class.java).onTap(),
                inputObservable.ofType(Result.SearchResult::class.java).onSearch()
            )
        }

    while(true) {

    }
}*/


fun main (args: Array<String>) {


  while (true) {

  }
}
