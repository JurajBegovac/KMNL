package beg.hr.kmnl.team

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import beg.hr.kmnl.R
import beg.hr.kmnl.util.DelegateAdapter
import beg.hr.kmnl.util.Item
import beg.hr.kmnl.util.inflate
import kotlinx.android.synthetic.main.item_team.view.*

/**
 * Created by juraj on 29/04/2017.
 */
class TeamDelegateAdapter : DelegateAdapter {
  companion object {
    val TYPE = this.hashCode()
  }
  
  data class Team(val position: String, val name: String, val games: String, val wins: String,
                  val draws: String, val loses: String, val goals: String,
                  val points: String) : Item {
    override fun type() = TYPE
  }
  
  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ViewHolder(parent)
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
    holder as TeamDelegateAdapter.ViewHolder
    holder.bind(item as Team)
  }
  
  class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_team)) {
    fun bind(item: Team) = with(itemView) {
      position.text = item.position
      position.setTextColor(Color.BLACK)
      name.text = item.name
      name.setTextColor(Color.BLACK)
      games.text = item.games
      games.setTextColor(Color.BLACK)
      wins.text = item.wins
      wins.setTextColor(Color.BLACK)
      draws.text = item.draws
      draws.setTextColor(Color.BLACK)
      loses.text = item.loses
      loses.setTextColor(Color.BLACK)
      goals.text = item.goals
      goals.setTextColor(Color.BLACK)
      points.text = item.points
      points.setTextColor(Color.BLACK)
    }
  }
}
