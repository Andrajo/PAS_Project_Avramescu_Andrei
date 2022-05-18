package com.android.androidprojectapplication

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {

    lateinit var listAdapter : ItemsAdaptor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater!!.inflate(R.layout.fragment_list, container, false)

        view.itemsList.layoutManager = LinearLayoutManager(context)

        listAdapter = ItemsAdaptor(mutableListOf(), mutableListOf())
        view.itemsList.adapter = listAdapter

        view.addItem.setOnClickListener {
            val itemsName = newListItem.text.toString()
            if(itemsName.isNotEmpty()) {
                val item = ItemInList(itemsName, R.drawable.ic_baseline_image_24)
                listAdapter.addItemInList(item)
                newListItem.text.clear()
            }
        }

        view.deleteItem.setOnClickListener {
            listAdapter.deleteCheckedItems()
        }

        view.shareButton.setOnClickListener {
            var shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, "We are sharing data!")
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }

        view.searchAction.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if(query.isEmpty()){
                    listAdapter.recoverList()
                }
                listAdapter.filterList(query)
                return false
            }
        })

        return view
    }
}
