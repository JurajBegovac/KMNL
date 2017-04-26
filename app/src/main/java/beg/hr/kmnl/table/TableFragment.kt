package beg.hr.kmnl.table


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import beg.hr.kmnl.MyApplication
import beg.hr.kmnl.R
import beg.hr.kmnl.State
import beg.hr.kmnl.web.Team
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.RxFragment
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_table.*


class TableFragment : RxFragment() {
  
  companion object {
    fun newInstance(): TableFragment = TableFragment()
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_table, container, false)
  
  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    table.apply {
      setHasFixedSize(true)
      val linearLayoutManager = LinearLayoutManager(context)
      layoutManager = linearLayoutManager
    }
    initAdapter()
  }
  
  override fun onStart() {
    super.onStart()
    val state = (MyApplication.component.application() as MyApplication)
        .state()
    
    state
        .bindUntilEvent(this, FragmentEvent.STOP)
        .filter { it is State.Data }
        .map { (it as State.Data).teams }
        .distinctUntilChanged()
        .map(this::mapToTeamItems)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          (table.adapter as TableAdapter).clearAndAddTeams(it)
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
  
  private fun mapToTeamItems(teams: List<Team>): List<Item.Team> =
      teams
          // todo move this sorting out of here
          .sortedWith(compareBy(Team::points,
                                Team::bonusPoints,
                                Team::goalDiff,
                                Team::goalsScored,
                                Team::wins))
          .asReversed()
          .mapIndexed { index, team ->
            Item.Team((index + 1).toString(),
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
      table.adapter = TableAdapter()
    }
  }
}
