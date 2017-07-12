package beg.hr.kmnl.util

import io.reactivex.Observable
import io.reactivex.Observable.defer
import io.reactivex.Observable.merge
import io.reactivex.Scheduler
import io.reactivex.subjects.ReplaySubject

/**
 * Created by juraj on 16/03/2017.
 */
fun <S, C> system(initState: S, accumulator: (S, C) -> S, scheduler: Scheduler,
                  vararg feedbacks: (Observable<S>) -> Observable<C>): Observable<S> {
  return defer {
    val replaySubject: ReplaySubject<S> = ReplaySubject.createWithSize(1)
    val command = merge(feedbacks.map { it(replaySubject) }).observeOn(scheduler)
    command.scan(initState, accumulator).doOnNext { replaySubject.onNext(it) }
  }
}
