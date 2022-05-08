package com.android.androidprojectapplication

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class ItemsAdaptor(
    private val itemsList: MutableList<ItemInList>
) : RecyclerView.Adapter<ItemsAdaptor.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun toggleStrikeThrough(newItemTitle: TextView, isChecked: Boolean) {
        if (isChecked) {
            newItemTitle.paintFlags = newItemTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        }
        else {
            newItemTitle.paintFlags = newItemTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun addItemInList(item: ItemInList) {
        itemsList.add(item)
        notifyItemInserted(itemsList.size - 1)
    }

    fun deleteCheckedItems() {
        itemsList.removeAll { item ->
            item.isChecked
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = itemsList[position]
        holder.itemView.apply {
            individualItemName.text = currentItem.title
            individualItemCheckBox.isChecked = currentItem.isChecked
            toggleStrikeThrough(individualItemName, currentItem.isChecked)
            individualItemCheckBox.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(individualItemName, isChecked)
                currentItem.isChecked = !currentItem.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}