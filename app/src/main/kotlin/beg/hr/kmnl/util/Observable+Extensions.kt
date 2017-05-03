package beg.hr.rxredux.kotlin.util

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.ReplaySubject

/**
 * Created by juraj on 16/03/2017.
 */
fun <S, C> system(initState: S, accumulator: (S, C) -> S, scheduler: Scheduler,
                  vararg feedbacks: (Observable<S>) -> Observable<C>): Observable<S> {
  return Observable.defer {
    val replaySubject: ReplaySubject<S> = ReplaySubject.create(1)
    
    val command = Observable.merge(feedbacks.map { it(replaySubject) })
        .observeOn(scheduler)
    
    command.scan(initState, accumulator)
        .doOnNext { replaySubject.onNext(it) }
  }
}
