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
    (MyApplication.component.application() as MyApplication)
        .observeState()
        .bindUntilEvent(this, FragmentEvent.STOP)
        .filter { it is State.Data }
        .map { (it as State.Data).teams }
        .distinctUntilChanged()
        .map(this::mapToTeamItems)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          (table.adapter as TableAdapter).clearAndAddTeams(it)
        }
  }
  
  private fun mapToTeamItems(teams: List<Team>): List<Item.Team> =
      teams
          .sortedWith(compareBy(Team::points,
                                Team::bonusPoints,
                                Team::goalDiff,
                                Team::goalsScored))
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
