package rx

import async.KSExecutorService
import async.PausableThreadPoolExecutor
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Single
import io.reactivex.Flowable
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.subscribeBy


val computation = Schedulers.from(KSExecutorService.newComputationExecutor(2))
val io = Schedulers.from(PausableThreadPoolExecutor.newCachedThreadPool())

/*fun upload(): Observable<Int> {
    return Observable.defer<Int> {
        println("upload ${Thread.currentThread().name}")
        Observable.fromCallable {
            println("upload observable on ${Thread.currentThread().name}")
            42
        }.subscribeOn(io)
                .subscribe(behaviorSubject)
        behaviorSubject
    }
}*/

fun backgroundTask(letter: String): Observable<Char> {
    return Observable.fromCallable {
        Thread.sleep(1000)
        println("backgroundTask $letter currentThread=${Thread.currentThread().name}")
        letter[0]
    }
}

fun main(args: Array<String>) {
    val myRelay = BehaviorRelay.create<String>().toSerialized()
    val single: Single<String> = myRelay.toFlowable(BackpressureStrategy.BUFFER).firstOrError()
    single.subscribeBy(onSuccess = {
        println("success: $it")
    }, onError = {
        println("error")
    })
    Thread.sleep(2000)
    myRelay.accept("temp")
    myRelay.accept("lava")
    while(true) {

    }
}

fun blockingSubscribeExperiment() {
    val alphabet = listOf("a", "b", "c", "d", "e", "f")
    Observable.fromIterable(alphabet)
            .flatMap { backgroundTask(it) }
            .subscribeOn(computation)
            .blockingSubscribe()
    println("after blockingSubscribe")
}

/*
val behaviorSubject: BehaviorSubject<Int> = BehaviorSubject.create()
fun main(args: Array<String>) {
    println("Hello World! on thread: ${Thread.currentThread().name}")
    val main = Schedulers.single()

    //val uiScheduler = Schedulers.from { uiThread.run { it } }

    Observable.fromCallable {
        println("outer observable on ${Thread.currentThread().name}")
        "24"
    }
            .flatMap {
                println("flatMap on ${Thread.currentThread().name}")
                upload()
            }
            .subscribeOn(computation)
            .subscribeBy {
                println("subscribeBy on ${Thread.currentThread().name}")
            }

    Single.fromCallable {
        println("Reading file on Thread: ${Thread.currentThread().name}")
    }.subscribeOn(io)
            .subscribeBy(onSuccess = {
                println("Handling success on Thread: ${Thread.currentThread().name}")
            })

    Single.fromCallable {
        println("Reading File on thread: ${Thread.currentThread().name}")
        Thread.sleep(1000)
        "file1"
    }.subscribeOn(io)
            .flatMapObservable {
                Observable.fromCallable {
                    println("Flowable thread ${Thread.currentThread().name}")
                    ""
                }
            }
            .map {
                println("Mapping on thread: ${Thread.currentThread().name}")
                ""
            }
            .ofType(String::class.java)
            .observeOn(computation)
            .map {
                println("Parsing xml on thread: ${Thread.currentThread().name}")
                Thread.sleep(2000)
            }
            .observeOn(computation)
            .map {
                println("Savine file to disk on: ${Thread.currentThread().name}")
                Thread.sleep(2000)
            }
            .observeOn(io)
            .subscribeBy(onNext = {
                println("Observing success on: ${Thread.currentThread().name}")
            }, onError = {
                println("Observing error on: ${Thread.currentThread().name}")
            })

    while (true) {

    }
}*/