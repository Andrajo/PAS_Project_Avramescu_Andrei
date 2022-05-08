package com.android.androidprojectapplication

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class ItemsAdaptor(
    val context: Context,
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
        val img = holder.itemView.findViewById<ImageView>(R.id.individualItemPhoto)
        img.setImageResource(currentItem.imageSrc)
        img.setOnClickListener {
            popupMenus(holder)
        }
    }

    private fun popupMenus(holder: ListViewHolder) {
        val popupMenu = PopupMenu(context, holder.itemView.individualItemPhoto)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.takePhotoButton -> {
                    Toast.makeText(context, "Take Photo ", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.addPhotoFromGallery -> {
                    Toast.makeText(context, "Add Photo From Gallery", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> true
            }
        }
        popupMenu.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenu)
        menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}