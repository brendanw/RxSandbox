package rx

import async.KSExecutorService
import async.PausableThreadPoolExecutor
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.rxkotlin.subscribeBy

val computation = Schedulers.from(KSExecutorService.newComputationExecutor(2))
val io = Schedulers.from(PausableThreadPoolExecutor.newCachedThreadPool())


