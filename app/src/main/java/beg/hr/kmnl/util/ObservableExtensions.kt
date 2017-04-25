package beg.hr.rxredux.kotlin.util

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.ReplaySubject

/**
 * Created by juraj on 16/03/2017.
 */

fun <R, T> Observable<T>.reduxWithFeedback(initState: R,
                                           reducer: (R, T) -> R,
                                           scheduler: Scheduler,
                                           vararg feedback: (Observable<R>) -> Observable<T>): Observable<R> {
  return Observable.defer {
    val replaySubject: ReplaySubject<R> = ReplaySubject.create(1)
    
    val output = replaySubject.share()
    
    val feedbacks = feedback.map { it(output) }
    
    val inputs = Observable.merge(listOf(this).plus(feedbacks)).observeOn(scheduler)
    
    inputs.scan(initState, reducer)
        .doOnNext { replaySubject.onNext(it) }
  }
}
