package beg.hr.kmnl

import android.app.Application
import android.os.StrictMode
import beg.hr.kmnl.web.*
import beg.hr.rxredux.kotlin.util.system
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.squareup.leakcanary.LeakCanary
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by juraj on 24/04/2017.
 */

sealed class State {
  companion object
  class Start : State()
  class Fetching : State()
  data class Parsing(val html: String) : State()
  data class Error(val msg: String) : State()
  data class Data(val teams: List<Team>, val players: List<Player>, val games: List<Game>) : State()
}

sealed class Command {
  class Fetch : Command()
  class Error(val msg: String) : Command()
  class Parse(val html: String) : Command()
  class DataParsed(val teams: List<Team>,
                   val players: List<Player>,
                   val games: List<Game>) : Command()
}

// reducer
fun State.Companion.reduce(state: State, command: Command): State {
  when (command) {
    is Command.Parse -> {
      if (state is State.Parsing) return state
      else return State.Parsing(command.html)
    }
    is Command.Fetch -> {
      if (state is State.Fetching) return state
      else return State.Fetching()
    }
    is Command.DataParsed -> return State.Data(command.teams, command.players, command.games)
    is Command.Error -> return State.Error(command.msg)
    else -> throw IllegalArgumentException("Wrong command")
  }
}

//fun State.isFetching(): Boolean = this is State.Fetching
fun State.isParsing(): Boolean = this is State.Parsing

fun State.Companion.initialize(commands: Observable<Command>,
                               initState: State): Observable<State> {
  
  val startFeedBack: (Observable<State>) -> Observable<Command> = {
    it.map { it is State.Start }
        .distinctUntilChanged()
        .switchMap {
          if (it)
            Observable.just(Command.Fetch())
          else
            Observable.empty()
        }
  }
  
  val fetchFeedback: (Observable<State>) -> Observable<Command> = {
    it.map { it is State.Fetching }
        .distinctUntilChanged()
        .switchMap {
          if (it == false)
            Observable.empty()
          else
            getLeague1Html()
                .map<Command> { Command.Parse(it) }
                .onErrorReturn {
                  val message = it.message ?: "Unknown error"
                  Command.Error(message)
                }
        }
  }
  
  val parseFeedback: (Observable<State>) -> Observable<Command> = {
    it
        .map {
          if (it is State.Parsing) Pair(true, it.html)
          else Pair(false, "")
        }
        .distinctUntilChanged()
        .switchMap {
          if (it.first) {
            val html = it.second
            Observable.just(Command.DataParsed(parseTeams(html),
                                               parsePlayers(html),
                                               parseGames(html)))
          } else {
            Observable.empty()
          }
        }
  }
  
  val errorFeedBack: (Observable<State>) -> Observable<Command> = {
    it.map { it is State.Error }
        .distinctUntilChanged()
        .switchMap {
          if (it == false) Observable.empty()
          else {
            //todo check if it was network error
            ReactiveNetwork.observeInternetConnectivity()
                .switchMap {
                  if (it == true) Observable.just(Command.Fetch())
                  else Observable.empty()
                }
          }
        }
  }
  
  return system(initState, { s, c -> State.reduce(s, c) }, Schedulers.io(),
                { commands }, startFeedBack, fetchFeedback, parseFeedback, errorFeedBack
  )
}

class MyApplication : Application() {
  
  private val state: BehaviorSubject<State> = BehaviorSubject.create()
  private val commands: PublishSubject<Command> = PublishSubject.create()
  
  companion object {
    lateinit var component: AppComponent
  }
  
  override fun onCreate() {
    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                                     .detectAll()
                                     .penaltyLog()
                                     .penaltyFlashScreen()
                                     .build())
      StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                                 .detectAll()
                                 .penaltyLog()
                                 .build())
    }
    super.onCreate()
    
    if (BuildConfig.DEBUG) {
      if (LeakCanary.isInAnalyzerProcess(this)) {
        // This process is dedicated to LeakCanary for heap analysis.
        // You should not init your app in this process.
        return
      }
      LeakCanary.install(this)
    }
    
    component = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()
    
    val initState: State = State.Start()
    State.initialize(commands, initState)
        .subscribe { state.onNext(it) }
  }
  
  fun state(): Observable<State> = state
  fun dispatch(command: Command) = commands.onNext(command)
}
