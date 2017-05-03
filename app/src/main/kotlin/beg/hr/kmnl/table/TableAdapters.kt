package beg.hr.kmnl.table

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import beg.hr.kmnl.R
import beg.hr.kmnl.util.DelegateAdapter
import beg.hr.kmnl.util.Item
import beg.hr.kmnl.util.inflate
import kotlinx.android.synthetic.main.item_player.view.*
import kotlinx.android.synthetic.main.item_team.view.*
import kotlinx.android.synthetic.main.item_title.view.*

/**
 * Created by juraj on 26/04/2017.
 */

class HeaderDelegateAdapter : DelegateAdapter {
  companion object {
    val TYPE = this.hashCode()
  }
  
  data class Header(val value: Unit) : Item {
    override fun type() = TYPE
  }
  
  class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_team))
  
  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ViewHolder(parent)
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {}
}

class TitleDelegateAdapter : DelegateAdapter {
  companion object {
    val TYPE = this.hashCode()
  }
  
  data class Title(val title: String) : Item {
    override fun type() = TYPE
  }
  
  class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_title)) {
    fun bind(item: Title) = with(itemView) {
      title.text = item.title
    }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ViewHolder(parent)
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
    holder as TitleDelegateAdapter.ViewHolder
    holder.bind(item as Title)
  }
}

class PlayerelegateAdapter : DelegateAdapter {
  companion object {
    val TYPE = this.hashCode()
  }
  
  data class Player(val name: String, val teamName: String, val goals: String) : Item {
    override fun type() = TYPE
  }
  
  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = ViewHolder(parent)
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
    holder as PlayerelegateAdapter.ViewHolder
    holder.bind(item as Player)
  }
  
  class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_player)) {
    fun bind(item: Player) = with(itemView) {
      playerName.text = item.name
      teamName.text = item.teamName
      value.text = item.goals
    }
  }
}
