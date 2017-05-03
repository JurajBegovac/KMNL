package beg.hr.kmnl.util

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by juraj on 29/04/2017.
 */

interface Item {
  fun type(): Int
}

interface DelegateAdapter {
  fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
  fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item)
}

class GenericAdapter(private val delegateAdapters: SparseArrayCompat<DelegateAdapter>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var items: ArrayList<Item> = ArrayList()
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
      delegateAdapters[getItemViewType(position)].onBindViewHolder(holder, items[position])
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
      delegateAdapters[viewType].onCreateViewHolder(parent)
  
  override fun getItemCount() = items.size
  
  override fun getItemViewType(position: Int) = items[position].type()
  
  fun clearAndAddAll(newItems: List<Item>) {
    items.clear()
    items.addAll(newItems)
    notifyDataSetChanged()
  }
}