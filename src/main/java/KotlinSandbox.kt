import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

fun main(args: Array<String>) {
    println("Hello World!")
    Observable.fromCallable {
        throw RuntimeException("Error")
    }
            .retry(2)
            .doOnError { println("doOnError ") }
            .subscribeBy(onNext = {

            }, onError = {

            })
}