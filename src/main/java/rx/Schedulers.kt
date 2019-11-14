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


val computation = Schedulers.from(KSExecutorService.newComputationExecutor(2))
val io = Schedulers.from(PausableThreadPoolExecutor.newCachedThreadPool())

fun main(args: Array<String>) {

}
