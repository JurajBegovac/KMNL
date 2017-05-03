package beg.hr.kmnl.table

import android.os.Bundle
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import beg.hr.kmnl.MyApplication
import beg.hr.kmnl.R
import beg.hr.kmnl.State
import beg.hr.kmnl.team.TeamDelegateAdapter
import beg.hr.kmnl.util.DelegateAdapter
import beg.hr.kmnl.util.GenericAdapter
import beg.hr.kmnl.util.Item
import beg.hr.kmnl.util.inflate
import beg.hr.kmnl.web.Player
import beg.hr.kmnl.web.Team
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.RxFragment
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_table.*


class TableFragment : RxFragment() {
  
  companion object {
    fun newInstance(): TableFragment = TableFragment()
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                            savedInstanceState: Bundle?): View? = container.inflate(R.layout.view_table)
  
  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    table.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(context)
    }
    initAdapter()
  }
  
  override fun onStart() {
    super.onStart()
    val state = (MyApplication.component.application() as MyApplication)
        .state()
        .subscribeOn(Schedulers.io())
    
    state
        .bindUntilEvent(this, FragmentEvent.STOP)
        .filter { it is State.Data }
        .cast(State.Data::class.java)
        .distinctUntilChanged()
        .map {
          listOf<Item>()
              .plus(TitleDelegateAdapter.Title(getString(R.string.first_league)))
              .plus(HeaderDelegateAdapter.Header(Unit))
              .plus(mapToTeamItems(it.teams))
              .plus(TitleDelegateAdapter.Title(getString(R.string.top_scorers)))
              .plus(mapToBestScorers(it.players))
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          (table.adapter as GenericAdapter).clearAndAddAll(it)
        }
    
    state
        .bindUntilEvent(this, FragmentEvent.STOP)
        .map { it is State.Fetching || it is State.Parsing || it is State.Start }
        .distinctUntilChanged()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          loading.visibility = if (it) View.VISIBLE else View.GONE
        }
    
    state
        .bindUntilEvent(this, FragmentEvent.STOP)
        .map {
          if (it is State.Error) Pair(true, it)
          else Pair(false, Any())
        }
        .distinctUntilChanged()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          if (it.first) {
            error.visibility = View.VISIBLE
            error.text = (it.second as State.Error).msg
          } else {
            error.visibility = View.GONE
          }
        }
  }
  
  private fun mapToBestScorers(players: List<Player>): List<PlayerelegateAdapter.Player> =
      players
          .filter { it.goals > 0 }
          .sortedBy { it.goals }
          .asReversed()
          .take(7)
          .map { PlayerelegateAdapter.Player(it.name, it.teamName, it.goals.toString()) }
  
  private fun mapToTeamItems(teams: List<Team>): List<Item> =
      teams
          // todo move this sorting out of here
          .sortedWith(compareBy(Team::points,
                                Team::bonusPoints,
                                Team::goalDiff,
                                Team::goalsScored,
                                Team::wins))
          .asReversed()
          .mapIndexed { index, team ->
            TeamDelegateAdapter.Team((index + 1).toString(),
                                     team.name,
                                     team.games.toString(),
                                     team.wins.toString(),
                                     team.draws.toString(),
                                     team.loses.toString(),
                                     "${team.goalsScored}:${team.goalsAgainst}",
                                     team.points.toString())
          }
  
  private fun initAdapter() {
    if (table.adapter == null) {
      val delegateAdapters = SparseArrayCompat<DelegateAdapter>()
      delegateAdapters.apply {
        put(TeamDelegateAdapter.TYPE, TeamDelegateAdapter())
        put(HeaderDelegateAdapter.TYPE, HeaderDelegateAdapter())
        put(TitleDelegateAdapter.TYPE, TitleDelegateAdapter())
        put(PlayerelegateAdapter.TYPE, PlayerelegateAdapter())
      }
      table.adapter = GenericAdapter(delegateAdapters)
    }
  }
}
