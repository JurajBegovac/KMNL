package beg.hr.kmnl.table

import android.graphics.Color
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import beg.hr.kmnl.R
import beg.hr.kmnl.util.inflate
import kotlinx.android.synthetic.main.item_table.view.*
import java.util.*

/**
 * Created by juraj on 26/04/2017.
 */

sealed class Item : ViewType {
  class Header : Item() {
    override fun viewType(): Int = AdapterConstants.HEADER
  }
  
  data class Team(val position: String, val name: String, val games: String, val wins: String,
                  val draws: String, val loses: String, val goals: String,
                  val points: String) : Item() {
    override fun viewType(): Int = AdapterConstants.TEAM
  }
}

object AdapterConstants {
  val HEADER = 1
  val TEAM = 2
}

interface ViewType {
  fun viewType(): Int
}

interface ViewTypeDelegateAdapter {
  fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
  fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)
}

class TableAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var items: ArrayList<Item>
  private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
  
  init {
    delegateAdapters.put(AdapterConstants.HEADER, HeaderDelegateAdapter())
    delegateAdapters.put(AdapterConstants.TEAM, TeamDelegateAdapter())
    items = ArrayList()
  }
  
  override fun getItemCount(): Int = items.size
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
      delegateAdapters[viewType].onCreateViewHolder(parent)
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
      delegateAdapters[getItemViewType(position)].onBindViewHolder(holder, items[position])
  
  override fun getItemViewType(position: Int): Int = items[position].viewType()
  
  fun clearAndAddTeams(teams: List<Item.Team>) {
    items.clear()
    items.add(Item.Header())
    items.addAll(teams)
    notifyDataSetChanged()
  }
}

class TeamDelegateAdapter : ViewTypeDelegateAdapter {
  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ViewHolder(parent)
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
    holder as TeamDelegateAdapter.ViewHolder
    holder.bind(item as Item.Team)
  }
  
  class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_table)) {
    fun bind(item: Item.Team) = with(itemView) {
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

class HeaderDelegateAdapter : ViewTypeDelegateAdapter {
  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ViewHolder(parent)
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {}
  
  class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_table))
}
