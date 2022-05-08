package com.android.androidprojectapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var listAdapter : ItemsAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listAdapter = ItemsAdaptor(mutableListOf())

        itemsList.adapter = listAdapter
        itemsList.layoutManager = LinearLayoutManager(this)

        addItem.setOnClickListener {
            val itemsName = newListItem.text.toString()
            if(itemsName.isNotEmpty()) {
                val item = ItemInList(itemsName)
                listAdapter.addItemInList(item)
                newListItem.text.clear()
            }
        }

        deleteItem.setOnClickListener {
            listAdapter.deleteCheckedItems()
        }
    }
}